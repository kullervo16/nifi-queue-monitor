package kullervo16.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import kullervo16.logic.model.Connection;
import kullervo16.logic.model.GroupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class QueueMonitorService {

    private List<ServerStatus> serverStatuses = new ArrayList<>();
    private ObjectMapper objectMapper;

    public QueueMonitorService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        // TODO : read from config file
        serverStatuses.add(new ServerStatus("localhost","http://localhost:8080/",State.UNKNOWN));
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
    @Scheduled(initialDelay = 1000, fixedRate = 20000)
    private void executeRetrieval() {
        List<ServerStatus> newList = new ArrayList<>();
        for(ServerStatus walker : this.serverStatuses) {
            ServerStatus newState = new ServerStatus(walker.getName(), walker.getUrl(),State.UNKNOWN);

            fetchStatusForGroup(newState, "root");
            if(newState.getState().equals(State.UNKNOWN)) {
                // nothing to report, so set to idle
                newState.setState(State.IDLE);
            }
            newList.add(newState);
        }
        this.serverStatuses = newList;
    }

    private void fetchStatusForGroup(ServerStatus serverStatus, String group) {

        try {
            URL url = new URL(serverStatus.getUrl()+"nifi-api/flow/process-groups/"+group);
            if(log.isDebugEnabled()) {
                log.debug("Reading "+url);
            }

            GroupResponse groupJson = this.objectMapper.readValue(url, GroupResponse.class);
            for(Connection conn : groupJson.getConnections()) {
                System.out.println(conn.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
            serverStatus.setState(State.UNREACHABLE);
        }
    }


}
