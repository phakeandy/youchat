package top.phakeandy.youchat.user;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static top.phakeandy.youchat.mapper.UsersDynamicSqlSupport.username;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.phakeandy.youchat.auth.CustomUserDetails;
import top.phakeandy.youchat.auth.CustomUserDetailsService;
import top.phakeandy.youchat.auth.request.RegisterRequest;
import top.phakeandy.youchat.auth.response.RegisterResponse;
import top.phakeandy.youchat.mapper.UsersMapper;
import top.phakeandy.youchat.user.exception.InvalidPasswordException;
import top.phakeandy.youchat.user.exception.PasswordMismatchException;
import top.phakeandy.youchat.user.exception.UsernameAlreadyExistsException;
import top.phakeandy.youchat.user.responcse.UserResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final CustomUserDetailsService customUserDetailsService;
  private final UsersMapper usersMapper;

  @Override
  @Transactional
  public RegisterResponse createUser(RegisterRequest request) {
    log.debug("Creating new user: {}", request.username());

    // 验证密码确认
    if (!request.password().equals(request.confirmPassword())) {
      throw new PasswordMismatchException();
    }

    // 检查用户名是否已存在
    if (customUserDetailsService.existsByUsername(request.username())) {
      throw new UsernameAlreadyExistsException(request.username());
    }

    try {
      // 创建用户
      CustomUserDetails createdUser =
          customUserDetailsService.createUser(
              request.username(),
              request.password(),
              request.nickname(),
              null // avatarUrl is optional
              );

      log.info("User created successfully: {}", createdUser.getUsername());

      return new RegisterResponse(
          "用户注册成功", createdUser.getId(), createdUser.getUsername(), createdUser.getNickname());

    } catch (IllegalArgumentException e) {
      // 处理密码验证错误
      throw new InvalidPasswordException(e.getMessage());
    }
  }

  @Override
  public UserResponse getCurrentUser(UserDetails userDetails) {
    return new UserResponse(userDetails.getUsername(), userDetails.getAuthorities());
  }

  @Override
  @Transactional
  public void deleteCurrentUser(@NonNull UserDetails userDetails) {
    final var usernameValue = userDetails.getUsername();
    log.debug("Deleting current user: {}", usernameValue);
    usersMapper.delete(c -> c.where(username, isEqualTo(usernameValue)));
    log.info("User deleted successfully: {}", usernameValue);
  }
}
