package softkit.service;

import softkit.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    String signIn(String username, String password);

    User create(User user);

    User update(User user);

    void saveNewPoints(String username, List<Integer> points);

    void delete(String username);

    User search(String username);

    User whoami(HttpServletRequest req);

    String refresh(String username);
}
