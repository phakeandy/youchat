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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import top.phakeandy.youchat.mapper.UsersMapper;
import top.phakeandy.youchat.model.Users;
import top.phakeandy.youchat.test.TestDataFaker;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("integration-test")
@Testcontainers
@Transactional
class AuthenticationControllerIntegrationTest {

  @Container @ServiceConnection
  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:latest");

  @Container
  @ServiceConnection
  @SuppressWarnings("resource")
  private static final GenericContainer<?> redis =
      new GenericContainer<>("redis:latest").withExposedPorts(6379);

  @Autowired private WebApplicationContext context;
  @Autowired private UsersMapper usersMapper;
  @Autowired private PasswordEncoder passwordEncoder;

  private MockMvc mockMvc;
  private Users testUser;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    // Create test user for login tests using DataFaker
    testUser = TestDataFaker.createRandomUser(passwordEncoder);
    usersMapper.insertSelective(testUser);
  }

  @Test
  void shouldLoginSuccessfully_whenValidCredentialsProvided() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "%s",
                      "password": "password123"
                    }
                    """
                        .formatted(testUser.getUsername())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(testUser.getUsername()))
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

  @Test
  void shouldRegisterSuccessfully_whenValidDataProvided() throws Exception {
    TestDataFaker.UserWithRawPassword newUserWithPassword =
        TestDataFaker.createRandomUserWithRawPassword(passwordEncoder);

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "%s",
                      "password": "%s",
                      "nickname": "%s",
                      "confirmPassword": "%s"
                    }
                    """
                        .formatted(
                            newUserWithPassword.user.getUsername(),
                            newUserWithPassword.rawPassword,
                            newUserWithPassword.user.getNickname(),
                            newUserWithPassword.rawPassword)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("用户注册成功"))
        .andExpect(jsonPath("$.username").value(newUserWithPassword.user.getUsername()))
        .andExpect(jsonPath("$.nickname").value(newUserWithPassword.user.getNickname()))
        .andExpect(jsonPath("$.userId").exists());
  }

  @Test
  void shouldReturn400_whenRegisterRequestIsInvalid() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/auth/register")
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
    TestDataFaker.UserWithRawPassword firstUserWithPassword =
        TestDataFaker.createRandomUserWithRawPassword(passwordEncoder);

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "%s",
                      "password": "%s",
                      "nickname": "%s",
                      "confirmPassword": "%s"
                    }
                    """
                        .formatted(
                            firstUserWithPassword.user.getUsername(),
                            firstUserWithPassword.rawPassword,
                            firstUserWithPassword.user.getNickname(),
                            firstUserWithPassword.rawPassword)))
        .andExpect(status().isCreated());

    // Try to create another user with same username
    TestDataFaker.UserWithRawPassword secondUserWithPassword =
        TestDataFaker.createRandomUserWithRawPassword(passwordEncoder);

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "username": "%s",
                      "password": "%s",
                      "nickname": "%s",
                      "confirmPassword": "%s"
                    }
                    """
                        .formatted(
                            firstUserWithPassword.user.getUsername(), // 使用相同的用户名
                            secondUserWithPassword.rawPassword,
                            secondUserWithPassword.user.getNickname(),
                            secondUserWithPassword.rawPassword)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(
            jsonPath("$.detail")
                .value("用户名 %s 已存在。".formatted(firstUserWithPassword.user.getUsername())));
  }
}
