package top.phakeandy.youchat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@ActiveProfiles("integration-test")
class YouchatApplicationTests {

  @Container
  @ServiceConnection
  @SuppressWarnings("resource")
  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:latest");

  @Container
  @ServiceConnection
  @SuppressWarnings("resource")
  private static final GenericContainer<?> redis =
      new GenericContainer<>("redis:latest").withExposedPorts(6379);

  @Test
  void contextLoads() {}
}
