package softkit.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import softkit.model.Role;

public class UserResponseDTO {

  @ApiModelProperty(position = 0)
  private Integer id;
  @ApiModelProperty(position = 1)
  private String username;
  @ApiModelProperty(position = 2)
  private String email;
  @ApiModelProperty(position = 3)
  private String firstName;
  @ApiModelProperty(position = 4)
  private String lastName;
  @ApiModelProperty(position = 5)
  private Role role;
  @ApiModelProperty(position = 6)
  private List<Integer> points;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<Integer> getPoints() {
    return points;
  }

  public void setPoints(List<Integer> points) {
    this.points = points;
  }
}
