package kullervo16.logic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerStatus {
    private String name;
    private String url;
    private State state;
}
