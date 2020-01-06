package kullervo16.logic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ServerStatus {
    private String name;
    private String url;
    private State state;
    private List<QueueStatus> queues;
}
