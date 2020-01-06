package kullervo16.logic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueueStatus {
    private int filledPercentage;
    private boolean blocked;
    private String id;
    private String displayName;
    private String url;
}
