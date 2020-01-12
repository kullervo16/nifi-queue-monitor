package kullervo16.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
