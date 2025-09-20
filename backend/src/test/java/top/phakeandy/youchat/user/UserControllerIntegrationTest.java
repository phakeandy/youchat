package top.phakeandy.youchat.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import top.phakeandy.youchat.config.TestBase;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest extends TestBase {

  @Autowired private WebApplicationContext context;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @Test
  void shouldCreateUserSuccessfully_whenValidDataProvided() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "newuser123",
                      "password": "Password123!",
                      "nickname": "测试用户",
                      "confirmPassword": "Password123!"
                    }
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("用户注册成功"))
        .andExpect(jsonPath("$.username").value("newuser123"))
        .andExpect(jsonPath("$.nickname").value("测试用户"))
        .andExpect(jsonPath("$.userId").exists());
  }

  @Test
  void shouldReturn400_whenCreateUserRequestIsInvalid() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "",
                      "password": "weak",
                      "nickname": "",
                      "confirmPassword": "different"
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").exists());
  }

  @Test
  void shouldReturn409_whenUsernameAlreadyExists() throws Exception {
    // First create a user
    mockMvc
        .perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "duplicateuser",
                      "password": "Password123!",
                      "nickname": "用户1",
                      "confirmPassword": "Password123!"
                    }
                    """))
        .andExpect(status().isCreated());

    // Try to create another user with same username
    mockMvc
        .perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "duplicateuser",
                      "password": "Password123!",
                      "nickname": "用户2",
                      "confirmPassword": "Password123!"
                    }
                    """))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.detail").value("用户名 duplicateuser 已存在。"));
  }

  @Test
  void shouldReturn400_whenPasswordConfirmationMismatch() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "testuser",
                      "password": "Password123!",
                      "nickname": "测试用户",
                      "confirmPassword": "DifferentPassword123!"
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").value("密码和确认密码不一致。"));
  }

  @Test
  @WithAnonymousUser
  void shouldReturn401_whenGetCurrentUserWithoutAuthentication() throws Exception {
    mockMvc
        .perform(get("/api/v1/users/current"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void shouldGetCurrentUser_whenUserIsAuthenticated() throws Exception {
    mockMvc
        .perform(get("/api/v1/users/current"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.authorities").isArray());
  }

  @Test
  @WithAnonymousUser
  void shouldReturn401_whenDeleteCurrentUserWithoutAuthentication() throws Exception {
    mockMvc
        .perform(delete("/api/v1/users/current"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void shouldDeleteCurrentUser_whenUserIsAuthenticated() throws Exception {
    mockMvc.perform(delete("/api/v1/users/current").with(csrf())).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn400_whenUsernameFormatIsInvalid() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "user@name",
                      "password": "Password123!",
                      "nickname": "测试用户",
                      "confirmPassword": "Password123!"
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  void shouldReturn400_whenPasswordIsTooShort() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "testuser",
                      "password": "short",
                      "nickname": "测试用户",
                      "confirmPassword": "short"
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
  }
}
