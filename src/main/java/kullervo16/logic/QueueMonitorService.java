package kullervo16.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import kullervo16.logic.model.Connection;
import kullervo16.logic.model.ConnectionAggregates;
import kullervo16.logic.model.GroupResponse;
import kullervo16.logic.model.ProcessGroup;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.parsers.SAXParser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class QueueMonitorService {

    private List<ServerStatus> serverStatuses = new ArrayList<>();
    private ObjectMapper objectMapper;

    @Qualifier("counterService")
    @Autowired
    private CounterService counterService;

    @Qualifier("gaugeService")
    @Autowired
    private GaugeService gaugeService;


    public QueueMonitorService(ObjectMapper objectMapper,
                               @Value("${config.location}")String configPath) {
        this.counterService = counterService;
        this.objectMapper = objectMapper;
        try {
            Document configDoc = new SAXBuilder().build(new File(configPath));

            for(Element configEl : configDoc.getRootElement().getChildren()) {
                String url = configEl.getAttributeValue("url");
                if(!url.endsWith("/")) {
                    url += "/";
                }
                ServerStatus server = new ServerStatus(configEl.getAttributeValue("name"), url, State.UNKNOWN);
                server.setId(configEl.getAttributeValue("id"));
                serverStatuses.add(server);
                for(Element excludeEl : configEl.getChildren("exclude")) {
                    server.getExcludeList().add(excludeEl.getAttributeValue("id"));
                }
            }
            //this.gaugeService.submit("numNifiServersMonitored", serverStatuses.size());
        } catch (JDOMException e) {
            log.error("Invalid configuration document : "+e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            log.error("Unable to read the configuration file : "+e.getMessage());
            System.exit(2);
        }

    }

    // =======================================================
    // Status access methods
    // =======================================================
    public List<ServerStatus> getServerStatus() {
        return this.serverStatuses;
    }

    // =======================================================
    // Data retrieval methods
    // =======================================================

    @Scheduled(initialDelay = 1000, fixedRateString = "${collection.interval.seconds:60}000")
    private void executeRetrieval() {
        this.gaugeService.submit("numNifiServersMonitored", serverStatuses.size());
        this.counterService.increment("nifiMetricCollection");
        List<ServerStatus> newList = new ArrayList<>();
        for(ServerStatus walker : this.serverStatuses) {
            ServerStatus newState = new ServerStatus(walker.getName(), walker.getUrl(),State.UNKNOWN);

            fetchStatusForGroup(newState, walker.getUrl()+"nifi-api/flow/process-groups/root", walker.getExcludeList());
            if(newState.getState().equals(State.UNKNOWN)) {
                // nothing to report, so set to idle
                newState.setState(State.IDLE);
            }
            Collections.sort(newState.getQueues(), (queueStatus, other) -> {
                return other.getFilledPercentage() - queueStatus.getFilledPercentage();
            });
            newState.getExcludeList().addAll(walker.getExcludeList());
            newList.add(newState);
        }
        this.serverStatuses = newList;
    }

    private void fetchStatusForGroup(ServerStatus serverStatus, String uri, List<String> excludeList) {

        try {
            URL url = new URL(uri);
            if(log.isDebugEnabled()) {
                log.debug("Reading "+url);
            }

            GroupResponse groupJson = this.objectMapper.readValue(url, GroupResponse.class);
            for(ProcessGroup pg : groupJson.getChildren()) {
                this.fetchStatusForGroup(serverStatus, pg.getUri().replaceAll("/process-groups/","/flow/process-groups/"), excludeList);
            }
            for(Connection conn : groupJson.getConnections()) {
                ConnectionAggregates counters = conn.getStatus().getAggregateSnapshot();
                // check whether this queue is to be ignored
                if(excludeList.contains(conn.getComponent().getId())) {
                    serverStatus.countIgnored();
                    continue;
                }
                if(counters.getFlowFilesQueued() > 0) {
                    if(log.isDebugEnabled()) {
                        log.debug(conn.getStatus().getAggregateSnapshot().isBlocked() + " : " + conn.getStatus().getAggregateSnapshot().getPercentage() + "%");
                    }
                    String displayName = conn.getStatus().getSourceName()+" -["+conn.getStatus().getName()+"]-> "+conn.getStatus().getDestinationName();
                    String queueUrl = serverStatus.getUrl()+"nifi/?processGroupId="+conn.getComponent().getParentGroupId()+"&componentIds="+conn.getComponent().getId();
                    serverStatus.getQueues().add(new QueueStatus(counters.getPercentage(), counters.isBlocked(), conn.getId(), displayName, queueUrl));
                    if(counters.isBlocked()) {
                        serverStatus.setState(State.BLOCKED);
                        serverStatus.countBlocked();
                    } else {
                        if(!State.BLOCKED.equals(serverStatus.getState())) {
                            serverStatus.setState(State.BUSY);
                        }
                        serverStatus.countBusy();
                    }
                } else {
                    serverStatus.countIdle();
                }
            }
            serverStatus.touch();
            this.gaugeService.submit(serverStatus.getId()+".idle", serverStatus.getQueuesIdle());
            this.gaugeService.submit(serverStatus.getId()+".busy", serverStatus.getQueuesBusy());
            this.gaugeService.submit(serverStatus.getId()+".blocked", serverStatus.getQueuesBlocked());
            this.gaugeService.submit(serverStatus.getId()+".ignored", serverStatus.getQueuesIgnored());

        } catch (Exception e) {
            e.printStackTrace();
            serverStatus.setState(State.UNREACHABLE);
        }
    }


}
