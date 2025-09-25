package top.phakeandy.youchat.chat;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Locale;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
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
import top.phakeandy.youchat.chat.request.CreateChatRequest;
import top.phakeandy.youchat.chat.request.UpdateChatRequest;
import top.phakeandy.youchat.mapper.ChatGroupsMapper;
import top.phakeandy.youchat.mapper.UsersMapper;
import top.phakeandy.youchat.model.ChatGroups;
import top.phakeandy.youchat.model.Users;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("integration-test")
@Testcontainers
@Transactional
class ChatControllerIntegrationTest {

  @Container @ServiceConnection
  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:latest");

  @Container
  @ServiceConnection
  @SuppressWarnings("resource")
  private static final GenericContainer<?> redis =
      new GenericContainer<>("redis:latest").withExposedPorts(6379);

  @Autowired private WebApplicationContext context;

  @Autowired private ChatGroupsMapper chatGroupsMapper;

  @Autowired private UsersMapper usersMapper;

  @Autowired private ObjectMapper objectMapper;

  private MockMvc mockMvc;
  private Users testUser;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    testUser = new Users();
    testUser.setUsername("testuser");
    testUser.setPassword(
        "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi"); // encoded "password123"
    testUser.setNickname(new Faker(Locale.CHINA).name().fullName());
    testUser.setAvatarUrl("default-avatar.png");
    usersMapper.insertSelective(testUser);
  }

  @Test
  @WithAnonymousUser
  void createChat_ShouldReturn401_WhenUserIsNotAuthenticated() throws Exception {
    CreateChatRequest request =
        new CreateChatRequest("测试群聊", "这是一个测试群聊", "https://example.com/avatar.jpg", 1L);

    mockMvc
        .perform(
            post("/api/v1/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void createChat_ShouldReturn201AndChatResponse_WhenUserIsAuthenticated() throws Exception {
    CreateChatRequest request =
        new CreateChatRequest(
            "测试群聊", "这是一个测试群聊", "https://example.com/avatar.jpg", testUser.getId());

    mockMvc
        .perform(
            post("/api/v1/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("测试群聊"))
        .andExpect(jsonPath("$.description").value("这是一个测试群聊"))
        .andExpect(jsonPath("$.ownerId").value(testUser.getId()))
        .andExpect(jsonPath("$.id").exists());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void createChat_WithInvalidData_ShouldReturn400_WhenUserIsAuthenticated() throws Exception {
    CreateChatRequest request =
        new CreateChatRequest(
            "", // 空群名
            "这是一个测试群聊",
            "https://example.com/avatar.jpg",
            testUser.getId());

    mockMvc
        .perform(
            post("/api/v1/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithAnonymousUser
  void getChat_ShouldReturn401_WhenUserIsNotAuthenticated() throws Exception {
    // 首先创建一个群聊
    ChatGroups chatGroup = new ChatGroups();
    chatGroup.setName("测试群聊");
    chatGroup.setDescription("测试描述");
    chatGroup.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup);

    mockMvc
        .perform(get("/api/v1/chats/{chatId}", chatGroup.getId()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void getChat_ShouldReturn200AndChatResponse_WhenUserIsAuthenticated() throws Exception {
    // 首先创建一个群聊
    ChatGroups chatGroup = new ChatGroups();
    chatGroup.setName("获取测试群聊");
    chatGroup.setDescription("这是一个用于获取测试的群聊");
    chatGroup.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup);

    mockMvc
        .perform(get("/api/v1/chats/{chatId}", chatGroup.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("获取测试群聊"))
        .andExpect(jsonPath("$.description").value("这是一个用于获取测试的群聊"))
        .andExpect(jsonPath("$.id").value(chatGroup.getId()));
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void getChat_WithNonexistentId_ShouldReturn404_WhenUserIsAuthenticated() throws Exception {
    mockMvc.perform(get("/api/v1/chats/999999")).andExpect(status().isNotFound());
  }

  @Test
  @WithAnonymousUser
  void updateChat_ShouldReturn401_WhenUserIsNotAuthenticated() throws Exception {
    // 首先创建一个群聊
    ChatGroups chatGroup = new ChatGroups();
    chatGroup.setName("原始群聊名称");
    chatGroup.setDescription("原始描述");
    chatGroup.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup);

    UpdateChatRequest updateRequest =
        new UpdateChatRequest("更新后的群聊名称", "更新后的描述", "https://example.com/new-avatar.jpg");

    mockMvc
        .perform(
            put("/api/v1/chats/{chatId}", chatGroup.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
                .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void updateChat_ShouldReturn200AndUpdatedChatResponse_WhenUserIsAuthenticated() throws Exception {
    // 首先创建一个群聊
    ChatGroups chatGroup = new ChatGroups();
    chatGroup.setName("原始群聊名称");
    chatGroup.setDescription("原始描述");
    chatGroup.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup);

    UpdateChatRequest updateRequest =
        new UpdateChatRequest("更新后的群聊名称", "更新后的描述", "https://example.com/new-avatar.jpg");

    mockMvc
        .perform(
            put("/api/v1/chats/{chatId}", chatGroup.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("更新后的群聊名称"))
        .andExpect(jsonPath("$.description").value("更新后的描述"))
        .andExpect(jsonPath("$.avatarUrl").value("https://example.com/new-avatar.jpg"));
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void updateChat_WithNonexistentId_ShouldReturn404_WhenUserIsAuthenticated() throws Exception {
    UpdateChatRequest request =
        new UpdateChatRequest("更新名称", "更新描述", "https://example.com/avatar.jpg");

    mockMvc
        .perform(
            put("/api/v1/chats/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void updateChat_WithInvalidData_ShouldReturn400_WhenUserIsAuthenticated() throws Exception {
    // 首先创建一个群聊
    ChatGroups chatGroup = new ChatGroups();
    chatGroup.setName("测试群聊");
    chatGroup.setDescription("测试描述");
    chatGroup.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup);

    // 然后用无效数据更新
    UpdateChatRequest updateRequest =
        new UpdateChatRequest(
            "", // 空群名
            "更新描述",
            "https://example.com/avatar.jpg");

    mockMvc
        .perform(
            put("/api/v1/chats/{chatId}", chatGroup.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
                .with(csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithAnonymousUser
  void deleteChat_ShouldReturn401_WhenUserIsNotAuthenticated() throws Exception {
    // 首先创建一个群聊
    ChatGroups chatGroup = new ChatGroups();
    chatGroup.setName("待删除群聊");
    chatGroup.setDescription("这是一个将要被删除的群聊");
    chatGroup.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup);

    mockMvc
        .perform(delete("/api/v1/chats/{chatId}", chatGroup.getId()).with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void deleteChat_ShouldReturn204_WhenUserIsAuthenticated() throws Exception {
    // 首先创建一个群聊
    ChatGroups chatGroup = new ChatGroups();
    chatGroup.setName("待删除群聊");
    chatGroup.setDescription("这是一个将要被删除的群聊");
    chatGroup.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup);

    // 然后删除这个群聊
    mockMvc
        .perform(delete("/api/v1/chats/{chatId}", chatGroup.getId()).with(csrf()))
        .andExpect(status().isNoContent());

    // 验证群聊已被删除
    mockMvc
        .perform(get("/api/v1/chats/{chatId}", chatGroup.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void deleteChat_WithNonexistentId_ShouldReturn404_WhenUserIsAuthenticated() throws Exception {
    mockMvc.perform(delete("/api/v1/chats/999999").with(csrf())).andExpect(status().isNotFound());
  }

  @Test
  @WithAnonymousUser
  void getChats_ShouldReturn401_WhenUserIsNotAuthenticated() throws Exception {
    mockMvc.perform(get("/api/v1/chats")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(
      username = "testuser",
      roles = {"USER"})
  void getChats_ShouldReturn200AndChatList_WhenUserIsAuthenticated() throws Exception {
    // 创建几个测试群聊
    ChatGroups chatGroup1 = new ChatGroups();
    chatGroup1.setName("群聊1");
    chatGroup1.setDescription("描述1");
    chatGroup1.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup1);

    ChatGroups chatGroup2 = new ChatGroups();
    chatGroup2.setName("群聊2");
    chatGroup2.setDescription("描述2");
    chatGroup2.setOwnerId(testUser.getId());
    chatGroupsMapper.insert(chatGroup2);

    // 获取群聊列表
    mockMvc
        .perform(get("/api/v1/chats"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(greaterThanOrEqualTo(2)))
        .andExpect(jsonPath("$.totalElements").value(greaterThanOrEqualTo(2)))
        .andExpect(jsonPath("$.totalPages").value(greaterThanOrEqualTo(1)))
        .andExpect(jsonPath("$.size").exists())
        .andExpect(jsonPath("$.number").exists());
  }
}
