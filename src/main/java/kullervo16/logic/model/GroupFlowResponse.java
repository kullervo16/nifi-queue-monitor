package kullervo16.logic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class GroupFlowResponse {
    private String id;
    private String uri;
    private Flow flow;
}
