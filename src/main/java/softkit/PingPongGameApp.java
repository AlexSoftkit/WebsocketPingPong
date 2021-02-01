package softkit;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import softkit.model.Role;
import softkit.model.User;
import softkit.service.UserServiceImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PingPongGameApp implements CommandLineRunner {

  private final UserServiceImpl userService;

  public PingPongGameApp(UserServiceImpl userService) {
    this.userService = userService;
  }

  public static void main(String[] args) {
    SpringApplication.run(softkit.PingPongGameApp.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Override
  public void run(String... params) throws Exception {
    User admin = new User();
    admin.setUsername("admin");
    admin.setPassword("admin");
    admin.setFirstName("admin first name");
    admin.setFirstName("admin last name");
    admin.setEmail("admin@email.com");
    admin.setRole(Role.ROLE_ADMIN);

    userService.create(admin);

    User user = new User();
    user.setUsername("user");
    user.setPassword("user");
    user.setFirstName("user first name");
    user.setFirstName("user last name");
    user.setEmail("user@email.com");
    user.setRole(Role.ROLE_USER);

    userService.create(user);

    User operator = new User();
    operator.setUsername("operator");
    operator.setPassword("operator");
    operator.setFirstName("operator first name");
    operator.setFirstName("operator last name");
    operator.setEmail("operator@email.com");
    operator.setRole(Role.ROLE_OPERATOR);

    userService.create(operator);
  }

}
