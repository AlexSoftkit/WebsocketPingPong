package softkit.service;

import javax.servlet.http.HttpServletRequest;

import softkit.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import softkit.exception.CustomException;
import softkit.model.User;
import softkit.repository.UserRepository;
import softkit.security.JwtTokenProvider;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                         JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public String signIn(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRole());
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @Override
  public User create(User user) {
    if (!userRepository.existsByUsername(user.getUsername())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      return userRepository.save(user);
    } else {
      throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @Override
  public User update(User user) {
    return userRepository.findById(user.getId()).map(existedUser -> {
      UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (isUpdateAllowed(userDetails, existedUser)) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
      } else {
        throw new AccessDeniedException("You are not allowed to update this user");
      }
    }).orElseThrow(() -> new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND));
  }

  private boolean isUpdateAllowed(UserDetails userDetails, User existedUser) {
    return hasRole(userDetails, Role.ROLE_ADMIN)
            || hasRole(userDetails, Role.ROLE_OPERATOR) && existedUser.getRole() == Role.ROLE_USER
            || userDetails.getUsername().equals(existedUser.getUsername());
  }

  private boolean hasRole(UserDetails userDetails, Role role) {
    return userDetails.getAuthorities().contains(role);
  }

  @Override
  public void saveNewPoints(String username, List<Integer> points) {
    User user = userRepository.findByUsername(username);
    user.setPoints(points);
    userRepository.save(user);
  }

  @Override
  public void delete(String username) {
    userRepository.deleteByUsername(username);
  }

  @Override
  public User search(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
    }
    return user;
  }

  @Override
  public User whoami(HttpServletRequest req) {
    return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
  }

  @Override
  public String refresh(String username) {
    return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRole());
  }

}
