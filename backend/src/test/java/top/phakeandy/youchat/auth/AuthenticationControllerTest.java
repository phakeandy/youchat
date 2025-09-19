package top.phakeandy.youchat.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationControllerTest {

  @Mock private AuthenticationManager authenticationManager;

  @Mock private SecurityContextRepository securityContextRepository;

  @Mock private SecurityContextHolderStrategy securityContextHolderStrategy;

  @Mock private UserDetails userDetails;

  @Mock private Authentication authentication;

  @Mock private SecurityContext securityContext;

  @InjectMocks private AuthenticationController authenticationController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    objectMapper = new ObjectMapper();

    Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    when(userDetails.getUsername()).thenReturn("testuser");
    doReturn(authorities).when(userDetails).getAuthorities();
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authentication.isAuthenticated()).thenReturn(true);
    doReturn(authorities).when(authentication).getAuthorities();
    when(securityContextHolderStrategy.createEmptyContext()).thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
  }

  @Test
  void shouldLoginSuccessfully() throws Exception {
    Map<String, String> loginRequest = Map.of("username", "testuser", "password", "password123");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(securityContext.getAuthentication()).thenReturn(authentication);

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Login successful"))
        .andExpect(jsonPath("$.authenticated").value(true))
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.timestamp").exists());

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(securityContextHolderStrategy).setContext(securityContext);
    verify(securityContextRepository)
        .saveContext(
            eq(securityContext), any(HttpServletRequest.class), any(HttpServletResponse.class));
  }

  @Test
  void shouldThrowExceptionForInvalidCredentials() throws Exception {
    Map<String, String> loginRequest = Map.of("username", "testuser", "password", "wrongpassword");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Authentication failed: Invalid credentials"))
        .andExpect(jsonPath("$.authenticated").value(false))
        .andExpect(jsonPath("$.timestamp").exists());

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(securityContextHolderStrategy, never()).setContext(any());
    verify(securityContextRepository, never()).saveContext(any(), any(), any());
  }

  @Test
  void shouldThrowExceptionForMissingUsername() throws Exception {
    Map<String, String> loginRequest = Map.of("password", "password123");

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Username and password are required"))
        .andExpect(jsonPath("$.authenticated").value(false))
        .andExpect(jsonPath("$.timestamp").exists());

    verify(authenticationManager, never()).authenticate(any());
  }

  @Test
  void shouldThrowExceptionForMissingPassword() throws Exception {
    Map<String, String> loginRequest = Map.of("username", "testuser");

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Username and password are required"))
        .andExpect(jsonPath("$.authenticated").value(false))
        .andExpect(jsonPath("$.timestamp").exists());

    verify(authenticationManager, never()).authenticate(any());
  }

  @Test
  void shouldThrowExceptionForEmptyCredentials() throws Exception {
    Map<String, String> loginRequest = Map.of("username", "", "password", "");

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Username and password are required"))
        .andExpect(jsonPath("$.authenticated").value(false))
        .andExpect(jsonPath("$.timestamp").exists());

    verify(authenticationManager, never()).authenticate(any());
  }

  @Test
  void shouldLogoutSuccessfully() throws Exception {
    when(securityContextHolderStrategy.getContext()).thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);

    mockMvc
        .perform(post("/api/v1/auth/logout"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Logout successful"))
        .andExpect(jsonPath("$.timestamp").exists());

    verify(securityContextHolderStrategy).clearContext();
    verify(securityContextRepository)
        .saveContext(
            any(SecurityContext.class),
            any(HttpServletRequest.class),
            any(HttpServletResponse.class));
  }

  @Test
  void shouldGetCurrentUserWhenAuthenticated() throws Exception {
    mockMvc
        .perform(get("/api/v1/auth/me").principal(authentication))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.authenticated").value(true))
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.authorities[0].authority").value("ROLE_USER"));
  }

  @Test
  void shouldReturnNotAuthenticatedWhenNoPrincipal() throws Exception {
    mockMvc
        .perform(get("/api/v1/auth/me"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.authenticated").value(false))
        .andExpect(jsonPath("$.message").value("Not authenticated"));
  }

  @Test
  void shouldCreateUnauthenticatedTokenCorrectly() throws Exception {
    Map<String, String> loginRequest = Map.of("username", "testuser", "password", "password123");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(securityContext.getAuthentication()).thenReturn(authentication);

    mockMvc.perform(
        post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)));

    ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
        ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
    verify(authenticationManager).authenticate(tokenCaptor.capture());

    UsernamePasswordAuthenticationToken capturedToken = tokenCaptor.getValue();
    assertThat(capturedToken.getPrincipal()).isEqualTo("testuser");
    assertThat(capturedToken.getCredentials()).isEqualTo("password123");
    assertThat(capturedToken.isAuthenticated()).isFalse();
  }
}
