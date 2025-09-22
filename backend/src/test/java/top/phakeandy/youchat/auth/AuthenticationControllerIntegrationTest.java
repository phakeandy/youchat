package top.phakeandy.youchat.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import top.phakeandy.youchat.auth.request.LoginRequest;
import top.phakeandy.youchat.config.TestBase;
import top.phakeandy.youchat.mapper.UsersMapper;
import top.phakeandy.youchat.model.Users;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("integration-test")
@Testcontainers
class AuthenticationControllerIntegrationTest extends TestBase {

  @Container
  private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

  @Container
  private static final GenericContainer<?> redis = new GenericContainer<>("redis:latest").withExposedPorts(6379);

  @Autowired private WebApplicationContext context;
  @Autowired private UsersMapper usersMapper;
  @Autowired private PasswordEncoder passwordEncoder;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    // Create test user for login tests
    Users testUser = new Users();
    testUser.setId(1L);
    testUser.setUsername("testuser");
    testUser.setPassword(passwordEncoder.encode("password123"));
    testUser.setNickname("测试用户");
    testUser.setAvatarUrl("default-avatar.png");
    usersMapper.insert(testUser);
  }

  @Test
  void shouldLoginSuccessfully_whenValidCredentialsProvided() throws Exception {
    LoginRequest loginRequest = new LoginRequest("testuser", "password123");

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "testuser",
                      "password": "password123"
                    }
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.authorities").isArray());
  }

  @Test
  void shouldReturn400_whenLoginRequestIsInvalid() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "",
                      "password": ""
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").exists());
  }
}
