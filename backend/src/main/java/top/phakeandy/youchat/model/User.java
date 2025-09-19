package top.phakeandy.youchat.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends Users implements UserDetails {

    public User() {
        super();
    }

    public User(Users users) {
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