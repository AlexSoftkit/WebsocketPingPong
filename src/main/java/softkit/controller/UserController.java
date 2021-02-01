package softkit.controller;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import softkit.dto.UserDataDTO;
import softkit.dto.UserResponseDTO;
import softkit.model.User;
import softkit.service.UserService;
import softkit.service.UserServiceImpl;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

  private final UserService userService;
  private final ModelMapper modelMapper;

  public UserController(UserService userService, ModelMapper modelMapper) {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/signin")
  @ApiOperation(value = "${UserController.signin}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
  public String login(//
      @ApiParam("Username") @RequestParam String username, //
      @ApiParam("Password") @RequestParam String password) {
    return userService.signIn(username, password);
  }

  @PostMapping("/update")
  @ApiOperation(value = "${UserController.signup}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied")})
  public UserResponseDTO update(@ApiParam("Update User") @RequestBody UserDataDTO user) {
    User result = userService.update(modelMapper.map(user, User.class));
    return modelMapper.map(result, UserResponseDTO.class);
  }

  @PostMapping("/create")
  @ApiOperation(value = "${UserController.signup}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 422, message = "Username is already in use")})
  public UserResponseDTO create(@ApiParam("Create User") @RequestBody UserDataDTO user) {
    User result = userService.create(modelMapper.map(user, User.class));
    return modelMapper.map(result, UserResponseDTO.class);
  }

  @DeleteMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.delete}", authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("Username") @PathVariable String username) {
    userService.delete(username);
    return username;
  }

  @GetMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
    return modelMapper.map(userService.search(username), UserResponseDTO.class);
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
  @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
  }

  @GetMapping("/refresh")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
  public String refresh(HttpServletRequest req) {
    return userService.refresh(req.getRemoteUser());
  }

}
