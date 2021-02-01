package softkit.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ROLE_ADMIN,
  ROLE_OPERATOR,
  ROLE_USER;

  public String getAuthority() {
    return name();
  }

}
