package top.phakeandy.youchat.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
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

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("integration-test")
@Testcontainers
@Transactional
class UserControllerIntegrationTest {

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

    testUser = new Users();
    testUser.setUsername("testuser"); // 固定用户名，匹配 @WithMockUser
    testUser.setPassword(passwordEncoder.encode("password123"));
    testUser.setNickname(new Faker(Locale.CHINA).name().fullName());
    testUser.setAvatarUrl("default-avatar.png");
    usersMapper.insertSelective(testUser);
  }

  @Test
  @WithAnonymousUser
  void shouldReturn401_whenGetCurrentUserWithoutAuthentication() throws Exception {
    mockMvc.perform(get("/api/v1/users/current")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void shouldGetCurrentUser_whenUserIsAuthenticated() throws Exception {
    mockMvc
        .perform(get("/api/v1/users/current").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.authorities").isArray());
  }

  @Test
  @WithAnonymousUser
  void shouldReturn401_whenDeleteCurrentUserWithoutAuthentication() throws Exception {
    mockMvc
        .perform(delete("/api/v1/users/current").with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void shouldDeleteCurrentUser_whenUserIsAuthenticated() throws Exception {
    mockMvc.perform(delete("/api/v1/users/current").with(csrf())).andExpect(status().isNoContent());
  }
}
