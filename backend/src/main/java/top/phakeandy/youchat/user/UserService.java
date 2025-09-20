package top.phakeandy.youchat.user;

import org.springframework.security.core.Authentication;

public interface UserService {

  /** 创建新用户 */
  CreateUserResponse createUser(CreateUserRequest request);

  /** 获取当前用户信息 */
  UserResponse getCurrentUser(Authentication authentication);

  /** 删除当前用户账户 */
  void deleteCurrentUser(Authentication authentication);
}
