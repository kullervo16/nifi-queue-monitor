package kullervo16.logic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ConnectionStatus {
    private String id;
    private String groupId;
    private String name;
    private String sourceName;
    private String destinationName;
    private Map aggregateSnapshot;
}
