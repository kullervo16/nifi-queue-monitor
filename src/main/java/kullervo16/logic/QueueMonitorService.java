package kullervo16.logic;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueueMonitorService {
    public List<ServerStatus> getServerStatus() {
        List<ServerStatus> result = new ArrayList<>();
        result.add(new ServerStatus("localhost","http://localhost:8080/nifi",State.BLOCKED));
        return result;
    }
}
