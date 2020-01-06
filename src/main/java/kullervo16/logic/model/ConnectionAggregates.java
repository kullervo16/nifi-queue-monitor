package kullervo16.logic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ConnectionAggregates {
    private long flowFilesQueued;
    private long flowFilesOut;
    private int percentUseCount;
    private int percentUseBytes;

    /**
     * Determine blocked as being non-empty with no data leaving.
     * @return
     */
    public boolean isBlocked() {
        return this.flowFilesQueued > 0 && this.flowFilesOut == 0;
    }

    public int getPercentage() {
        return this.percentUseCount > this.percentUseBytes ? this.percentUseCount : this.percentUseBytes;
    }
}
