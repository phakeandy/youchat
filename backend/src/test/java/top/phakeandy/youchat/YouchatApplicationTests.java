package top.phakeandy.youchat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class YouchatApplicationTests {

  @Test
  void contextLoads() {
    // Basic context loading test with H2 in-memory database
    // Docker Compose is disabled for this test
  }
}
