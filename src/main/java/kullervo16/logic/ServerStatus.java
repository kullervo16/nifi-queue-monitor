package kullervo16.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class ServerStatus {
    @NonNull
    private String name;
    @NonNull
    private String url;
    @NonNull
    private State state;
    @NonNull
    private List<QueueStatus> queues = new ArrayList<>();
    @JsonIgnore
    private List<String> excludeList = new ArrayList<>();
    @JsonIgnore
    private Map<String,String> metricsMap = new HashMap<>();
    private String id;
    private int queuesIdle;
    private int queuesBusy;
    private int queuesBlocked;
    private long collectionTime;
    private int queuesIgnored;

    public void countBusy() {
        this.queuesBusy++;
    }

    public void countBlocked() {
        this.queuesBlocked++;
    }

    public void countIdle() {
        this.queuesIdle++;
    }

    public void touch() {
        this.collectionTime = System.currentTimeMillis();
    }

    public void countIgnored() {
        this.queuesIgnored++;
    }

}
