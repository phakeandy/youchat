package top.phakeandy.youchat.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Schema(description = "当前用户信息响应")
public record UserResponse(
    @Schema(description = "当前登录用户名", example = "testuser") String username,
    @Schema(description = "当前用户的权限列表", example = "[\"ROLE_USER\"]")
        Collection<? extends GrantedAuthority> authorities) {}
