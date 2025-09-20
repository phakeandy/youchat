package top.phakeandy.youchat.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Schema(description = "用户登录成功响应")
public record LoginResponse(
    @Schema(description = "登录成功的用户名", example = "testuser") String username,
    @Schema(description = "用户的权限列表", example = "[\"ROLE_USER\"]")
        Collection<? extends GrantedAuthority> authorities) {}
