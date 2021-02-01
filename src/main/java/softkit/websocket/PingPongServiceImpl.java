package softkit.websocket;

import softkit.model.User;
import softkit.service.UserServiceImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class PingPongServiceImpl implements PingPongService {

    private static final List<Integer> ORIGINAL_ARRAY = Arrays.asList(10, 15, 20);
    public static final String SECURED_TOPIC = "/secured/topic";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserServiceImpl userService;

    public PingPongServiceImpl(SimpMessagingTemplate simpMessagingTemplate, UserServiceImpl userService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }

    public void startGame(Principal user) {
        ArrayList<Integer> newPoints = new ArrayList<>(ORIGINAL_ARRAY);

        userService.saveNewPoints(user.getName(), newPoints);

        Executors.newSingleThreadScheduledExecutor()
                .schedule(processNextStep(user, new ArrayList<>(newPoints)), 10, TimeUnit.SECONDS);
        simpMessagingTemplate.convertAndSendToUser(user.getName(), SECURED_TOPIC, newPoints);
    }

    public void processHint(Principal user, Integer message) {
        User searched = userService.search(user.getName());
        if(nonNull(searched.getPoints())) {
            List<Integer> newState = searched.getPoints().stream()
                    .map(integer -> integer.equals(message) ? ++integer : integer)
                    .collect(Collectors.toList());
            userService.saveNewPoints(user.getName(), newState);
        }
    }

    private Runnable processNextStep(Principal user, List<Integer> previousState) {
        return () -> {
            List<Integer> points = userService.search(user.getName()).getPoints();
            if(previousState.equals(points)) {
                simpMessagingTemplate.convertAndSendToUser(user.getName(), SECURED_TOPIC, "You are not answered");
            } else {
                simpMessagingTemplate.convertAndSendToUser(user.getName(), SECURED_TOPIC, points);
                Executors.newSingleThreadScheduledExecutor()
                        .schedule(processNextStep(user, new ArrayList<>(points)), 10, TimeUnit.SECONDS);
            }
        };
    }
}
