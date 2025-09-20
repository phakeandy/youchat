package top.phakeandy.youchat.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import top.phakeandy.youchat.config.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationControllerTest {

  @Mock private AuthenticationService authenticationService;

  @Mock private UserDetails userDetails;

  @Mock private Authentication authentication;

  @InjectMocks private AuthenticationController authenticationController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    mockMvc =
        MockMvcBuilders.standaloneSetup(authenticationController)
            .setControllerAdvice(exceptionHandler)
            .build();
    objectMapper = new ObjectMapper();

    Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    when(userDetails.getUsername()).thenReturn("testuser");
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authentication.isAuthenticated()).thenReturn(true);
  }

  @Test
  void shouldLoginSuccessfully() throws Exception {
    LoginRequest loginRequest = new LoginRequest("testuser", "password123");
    LoginResponse loginResponse =
        new LoginResponse("testuser", List.of(new SimpleGrantedAuthority("ROLE_USER")));

    when(authenticationService.authenticate(
            any(LoginRequest.class), any(HttpServletRequest.class), any(HttpServletResponse.class)))
        .thenReturn(loginResponse);

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.authorities[0].authority").value("ROLE_USER"));

    verify(authenticationService)
        .authenticate(
            any(LoginRequest.class), any(HttpServletRequest.class), any(HttpServletResponse.class));
  }

  @Test
  void shouldHandleAuthenticationServiceException() throws Exception {
    LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

    when(authenticationService.authenticate(
            any(LoginRequest.class), any(HttpServletRequest.class), any(HttpServletResponse.class)))
        .thenThrow(new InvalidCredentialsException("用户名或密码错误"));

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }
}
