package kullervo16.rest;

import kullervo16.logic.QueueMonitorService;
import kullervo16.logic.ServerStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NQMRestController {

    private final QueueMonitorService monitorService;

    public NQMRestController(QueueMonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @GetMapping("/servers/")
    public List<ServerStatus> getServerStatus() {
        return this.monitorService.getServerStatus();
    }
}
