package top.phakeandy.youchat.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.SecurityContextRepository;
import top.phakeandy.youchat.auth.exception.AuthenticationException;
import top.phakeandy.youchat.auth.exception.InvalidCredentialsException;
import top.phakeandy.youchat.auth.exception.UserNotFoundException;
import top.phakeandy.youchat.auth.request.LoginRequest;
import top.phakeandy.youchat.auth.response.LoginResponse;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock private AuthenticationManager authenticationManager;

  @Mock private SecurityContextRepository securityContextRepository;

  @Mock private SecurityContextHolderStrategy securityContextHolderStrategy;

  @Mock private UserDetails userDetails;

  @Mock private Authentication authentication;

  @Mock private SecurityContext securityContext;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @InjectMocks private AuthenticationServiceImpl authenticationService;

  private Collection<GrantedAuthority> authorities;

  @BeforeEach
  void setUp() {
    authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Test
  void shouldAuthenticateSuccessfully() throws Exception {
    LoginRequest loginRequest = new LoginRequest("testuser", "password123");

    when(userDetails.getUsername()).thenReturn("testuser");
    doReturn(authorities).when(userDetails).getAuthorities();
    when(authentication.getPrincipal()).thenReturn(userDetails);
    lenient().when(authentication.isAuthenticated()).thenReturn(true);
    when(securityContextHolderStrategy.createEmptyContext()).thenReturn(securityContext);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    LoginResponse result = authenticationService.authenticate(loginRequest, request, response);

    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo("testuser");
    assertThat(result.authorities()).hasSize(1);
    assertThat(result.authorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(securityContextHolderStrategy).createEmptyContext();
    verify(securityContext).setAuthentication(authentication);
    verify(securityContextHolderStrategy).setContext(securityContext);
    verify(securityContextRepository).saveContext(securityContext, request, response);
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenAuthenticationFails() {
    LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Bad credentials"));

    assertThatThrownBy(() -> authenticationService.authenticate(loginRequest, request, response))
        .isInstanceOf(InvalidCredentialsException.class)
        .extracting(ex -> ((InvalidCredentialsException) ex).getBody().getDetail())
        .isEqualTo("用户名或密码错误");
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
    LoginRequest loginRequest = new LoginRequest("nonexistent", "password123");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new UsernameNotFoundException("User not found"));

    assertThatThrownBy(() -> authenticationService.authenticate(loginRequest, request, response))
        .isInstanceOf(UserNotFoundException.class)
        .extracting(ex -> ((UserNotFoundException) ex).getBody().getDetail())
        .isEqualTo("用户不存在");
  }

  @Test
  void shouldValidateLoginRequestFields() {
    LoginRequest loginRequest = new LoginRequest("", "");

    assertThatThrownBy(() -> authenticationService.authenticate(loginRequest, request, response))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("用户名和密码不能为空");
  }

  @Test
  void shouldThrowExceptionWhenUsernameIsNull() {
    LoginRequest loginRequest = new LoginRequest(null, "password123");

    assertThatThrownBy(() -> authenticationService.authenticate(loginRequest, request, response))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("用户名和密码不能为空");
  }

  @Test
  void shouldThrowExceptionWhenPasswordIsNull() {
    LoginRequest loginRequest = new LoginRequest("testuser", null);

    assertThatThrownBy(() -> authenticationService.authenticate(loginRequest, request, response))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("用户名和密码不能为空");
  }

  @Test
  void shouldThrowExceptionWhenAuthenticationManagerThrowsGeneralException() {
    LoginRequest loginRequest = new LoginRequest("testuser", "password123");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new RuntimeException("Database connection failed"));

    assertThatThrownBy(() -> authenticationService.authenticate(loginRequest, request, response))
        .isInstanceOf(AuthenticationException.class)
        .extracting(ex -> ((AuthenticationException) ex).getBody().getDetail())
        .isEqualTo("认证过程中发生错误: Database connection failed");
  }

  @Test
  void shouldThrowExceptionWhenSecurityContextSaveFails() {
    LoginRequest loginRequest = new LoginRequest("testuser", "password123");

    lenient().when(userDetails.getUsername()).thenReturn("testuser");
    lenient().doReturn(authorities).when(userDetails).getAuthorities();
    lenient().when(authentication.getPrincipal()).thenReturn(userDetails);
    lenient().when(authentication.isAuthenticated()).thenReturn(true);
    lenient().when(securityContextHolderStrategy.createEmptyContext()).thenReturn(securityContext);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    // Mock security context save failure
    doThrow(new RuntimeException("Session save failed"))
        .when(securityContextRepository)
        .saveContext(any(), any(), any());

    assertThatThrownBy(() -> authenticationService.authenticate(loginRequest, request, response))
        .isInstanceOf(AuthenticationException.class)
        .extracting(ex -> ((AuthenticationException) ex).getBody().getDetail())
        .isEqualTo("认证过程中发生错误: Session save failed");
  }
}
