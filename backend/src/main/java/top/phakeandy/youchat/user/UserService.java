package top.phakeandy.youchat.user;

import top.phakeandy.youchat.auth.CustomUserDetails;
import top.phakeandy.youchat.auth.request.RegisterRequest;
import top.phakeandy.youchat.auth.response.RegisterResponse;
import top.phakeandy.youchat.user.responcse.UserResponse;

public interface UserService {

  /** 创建新用户 */
  RegisterResponse createUser(RegisterRequest request);

  /** 获取当前用户信息 */
  UserResponse getCurrentUser(CustomUserDetails customUserDetails);

  /** 删除当前用户账户 */
  void deleteCurrentUser(CustomUserDetails customUserDetails);
}
