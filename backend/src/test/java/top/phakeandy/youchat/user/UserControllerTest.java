package top.phakeandy.youchat.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import top.phakeandy.youchat.user.exception.InvalidPasswordException;
import top.phakeandy.youchat.user.exception.PasswordMismatchException;
import top.phakeandy.youchat.user.exception.UsernameAlreadyExistsException;
import top.phakeandy.youchat.user.request.CreateUserRequest;
import top.phakeandy.youchat.user.responcse.CreateUserResponse;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

  @Mock private UserService userService;

  @Mock private UserDetails userDetails;

  @Mock private Authentication authentication;

  @InjectMocks private UserController userController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    mockMvc =
        MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(exceptionHandler)
            .build();
    objectMapper = new ObjectMapper();

    Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    when(userDetails.getUsername()).thenReturn("testuser");
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authentication.isAuthenticated()).thenReturn(true);
  }

  @Test
  void shouldCreateUserSuccessfully() throws Exception {
    CreateUserRequest request =
        new CreateUserRequest("newuser", "Password123!", "新用户", "Password123!");

    CreateUserResponse response = new CreateUserResponse("用户注册成功", 1L, "newuser", "新用户");

    when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("用户注册成功"))
        .andExpect(jsonPath("$.userId").value(1))
        .andExpect(jsonPath("$.username").value("newuser"))
        .andExpect(jsonPath("$.nickname").value("新用户"));

    verify(userService).createUser(any(CreateUserRequest.class));
  }

  @Test
  void shouldHandleUsernameAlreadyExistsException() throws Exception {
    CreateUserRequest request =
        new CreateUserRequest("existinguser", "Password123!", "已有用户", "Password123!");

    when(userService.createUser(any(CreateUserRequest.class)))
        .thenThrow(new UsernameAlreadyExistsException("existinguser"));

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict());
  }

  @Test
  void shouldHandlePasswordMismatchException() throws Exception {
    CreateUserRequest request =
        new CreateUserRequest("newuser", "Password123!", "新用户", "DifferentPassword123!");

    when(userService.createUser(any(CreateUserRequest.class)))
        .thenThrow(new PasswordMismatchException());

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldHandleInvalidPasswordException() throws Exception {
    CreateUserRequest request = new CreateUserRequest("newuser", "weak", "新用户", "weak");

    when(userService.createUser(any(CreateUserRequest.class)))
        .thenThrow(new InvalidPasswordException("密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符"));

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
