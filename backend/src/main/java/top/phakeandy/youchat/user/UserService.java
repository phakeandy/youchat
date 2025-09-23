package top.phakeandy.youchat.user;

import org.springframework.security.core.userdetails.UserDetails;
import top.phakeandy.youchat.auth.request.RegisterRequest;
import top.phakeandy.youchat.auth.response.RegisterResponse;
import top.phakeandy.youchat.user.responcse.UserResponse;

public interface UserService {

  /** 创建新用户 */
  RegisterResponse createUser(RegisterRequest request);

  /** 获取当前用户信息 */
  UserResponse getCurrentUser(UserDetails userDetails);

  /** 删除当前用户账户 */
  void deleteCurrentUser(UserDetails userDetails);
}
