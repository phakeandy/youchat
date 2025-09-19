package top.phakeandy.youchat.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.phakeandy.youchat.model.Users;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomUserDetails extends Users implements UserDetails {

  public CustomUserDetails() {
    super();
  }

  public CustomUserDetails(Users users) {
    this.setId(users.getId());
    this.setUsername(users.getUsername());
    this.setPassword(users.getPassword());
    this.setNickname(users.getNickname());
    this.setAvatarUrl(users.getAvatarUrl());
    this.setSettings(users.getSettings());
    this.setCreatedAt(users.getCreatedAt());
    this.setUpdatedAt(users.getUpdatedAt());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
