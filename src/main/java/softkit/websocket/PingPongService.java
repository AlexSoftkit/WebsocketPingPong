package softkit.websocket;

import java.security.Principal;

public interface PingPongService {

    void startGame(Principal user);

    void processHint(Principal user, Integer message);
}
