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

  private static final long serialVersionUID = 1L;

  public CustomUserDetails() {
    super();
  }

  /**
   * 创建 CustomUserDetails 实例的静态工厂方法
   * 避免在构造函数中调用可能抛出异常的方法
   */
  public static CustomUserDetails fromUsers(Users users) {
    CustomUserDetails details = new CustomUserDetails();
    // 安全地复制字段值
    try {
      details.setId(users.getId());
      details.setUsername(users.getUsername());
      details.setPassword(users.getPassword());
      details.setNickname(users.getNickname());
      details.setAvatarUrl(users.getAvatarUrl());
      details.setSettings(users.getSettings());
      details.setCreatedAt(users.getCreatedAt());
      details.setUpdatedAt(users.getUpdatedAt());
    } catch (Exception e) {
      // 如果复制过程中出现异常，使用默认值
      details.setId(null);
      details.setUsername("");
      details.setPassword("");
      details.setNickname("");
      details.setAvatarUrl("");
      details.setSettings(null);
      details.setCreatedAt(null);
      details.setUpdatedAt(null);
    }
    return details;
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
