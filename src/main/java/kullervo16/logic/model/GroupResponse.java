package kullervo16.logic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class GroupResponse {
    private GroupFlowResponse processGroupFlow;

    public List<Connection> getConnections() {
        return this.processGroupFlow.getFlow().getConnections();
    }
}
