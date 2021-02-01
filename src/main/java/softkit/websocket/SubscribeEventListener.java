package softkit.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private final PingPongService pingPongService;

    public SubscribeEventListener(PingPongService pingPongService) {
        this.pingPongService = pingPongService;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        Message<byte[]> message = event.getMessage();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (StompCommand.SUBSCRIBE.equals(command)) {
            Principal user = accessor.getUser();
            pingPongService.startGame(user);
        }
    }
}
