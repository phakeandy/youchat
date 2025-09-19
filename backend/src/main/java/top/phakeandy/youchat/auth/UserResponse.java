package top.phakeandy.youchat.auth;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public record UserResponse(String username, Collection<? extends GrantedAuthority> authorities) {}
