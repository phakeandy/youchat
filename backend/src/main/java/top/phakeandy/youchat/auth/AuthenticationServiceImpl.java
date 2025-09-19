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
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
      UsernamePasswordAuthenticationToken authenticationToken =
          UsernamePasswordAuthenticationToken.unauthenticated(
              loginRequest.username(), loginRequest.password());

      Authentication authentication = authenticationManager.authenticate(authenticationToken);

      SecurityContext context = securityContextHolderStrategy.createEmptyContext();
      context.setAuthentication(authentication);

      securityContextHolderStrategy.setContext(context);
      securityContextRepository.saveContext(context, request, response);

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      log.info("User {} logged in successfully", loginRequest.username());

      return new LoginResponse(userDetails.getUsername(), userDetails.getAuthorities());

    } catch (BadCredentialsException ex) {
      throw new InvalidCredentialsException("用户名或密码错误");
    } catch (UsernameNotFoundException ex) {
      throw new UserNotFoundException("用户不存在");
    } catch (Exception ex) {
      throw new AuthenticationException("认证过程中发生错误: " + ex.getMessage(), ex);
    }
  }

  @Override
  public UserResponse getCurrentUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AuthenticationException("用户未认证");
    }

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return new UserResponse(userDetails.getUsername(), userDetails.getAuthorities());
  }

  private void validateLoginRequest(LoginRequest loginRequest) {
    if (!StringUtils.hasText(loginRequest.username())
        || !StringUtils.hasText(loginRequest.password())) {
      throw new IllegalArgumentException("用户名和密码不能为空");
    }
  }
}
