package kullervo16.logic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class GroupResponse {
    private GroupFlowResponse processGroupFlow;

    public List<Connection> getConnections() {
        if(this.processGroupFlow == null || this.processGroupFlow.getFlow() == null) {
            return new ArrayList<>();
        }
        return this.processGroupFlow.getFlow().getConnections();
    }

    public List<ProcessGroup> getChildren() {
        if(this.processGroupFlow == null || this.processGroupFlow.getFlow() == null) {
            return new ArrayList<>();
        }
        return this.processGroupFlow.getFlow().getProcessGroups();
    }
}
