package softkit.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class PingPongController {

    private final PingPongService pingPongService;

    public PingPongController(PingPongServiceImpl pingPongService) {
        this.pingPongService = pingPongService;
    }

    @MessageMapping("/secured/ping")
    public void send(final Integer message, Principal user) {
        pingPongService.processHint(user, message);
    }
}
