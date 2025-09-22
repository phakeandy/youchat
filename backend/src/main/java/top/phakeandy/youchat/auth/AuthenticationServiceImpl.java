package top.phakeandy.youchat.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.phakeandy.youchat.auth.exception.AuthenticationException;
import top.phakeandy.youchat.auth.exception.InvalidCredentialsException;
import top.phakeandy.youchat.auth.exception.UserNotFoundException;
import top.phakeandy.youchat.auth.request.LoginRequest;
import top.phakeandy.youchat.auth.response.LoginResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;
  private final SecurityContextHolderStrategy securityContextHolderStrategy;

  @Override
  public LoginResponse authenticate(
      LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
    validateLoginRequest(loginRequest);

    try {
      //   UsernamePasswordAuthenticationToken authenticationToken =
      //       UsernamePasswordAuthenticationToken.unauthenticated(
      //           loginRequest.username(), loginRequest.password());

      //   Authentication authentication = authenticationManager.authenticate(authenticationToken);

      //   SecurityContext context = securityContextHolderStrategy.createEmptyContext();
      //   context.setAuthentication(authentication);

      //   securityContextHolderStrategy.setContext(context);
      //   securityContextRepository.saveContext(context, request, response);

      //   UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      //   log.info("User {} logged in successfully", loginRequest.username());

      //   return new LoginResponse(userDetails.getUsername(), userDetails.getAuthorities());
      // 1. 创建未认证的 token
      UsernamePasswordAuthenticationToken authenticationToken =
          UsernamePasswordAuthenticationToken.unauthenticated(
              loginRequest.username(), loginRequest.password());

      // 2. 委托 AuthenticationManager 进行认证
      // 如果认证失败，这里会抛出异常
      Authentication authentication = authenticationManager.authenticate(authenticationToken);

      // 3. ！！！关键步骤：获取一个新的、干净的 SecurityContext 实例
      //    不要再用 SecurityContextHolder.getContext()，因为它可能包含旧状态
      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authentication);

      // 4. ！！！关键步骤：将新的上下文存入 Holder 和 Repository
      SecurityContextHolder.setContext(context);
      securityContextRepository.saveContext(context, request, response);

      // 5. 提取用户信息并返回
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      log.info("User {} logged in successfully", loginRequest.username());
      return new LoginResponse(userDetails.getUsername(), userDetails.getAuthorities());

    } catch (BadCredentialsException ex) {
      throw new InvalidCredentialsException("用户名或密码错误", ex);
    } catch (UsernameNotFoundException ex) {
      throw new UserNotFoundException("用户不存在", ex);
    } catch (Exception ex) {
      throw new AuthenticationException("认证过程中发生错误: " + ex.getMessage(), ex);
    }
  }

  private void validateLoginRequest(LoginRequest loginRequest) {
    if (!StringUtils.hasText(loginRequest.username())
        || !StringUtils.hasText(loginRequest.password())) {
      throw new IllegalArgumentException("用户名和密码不能为空");
    }
  }
}
