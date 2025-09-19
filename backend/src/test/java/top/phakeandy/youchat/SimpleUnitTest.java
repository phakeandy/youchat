package top.phakeandy.youchat;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class SimpleUnitTest {

  @Test
  void contextLoads() {
    // Simple unit test that verifies Spring context loads
    assertThat(true).isTrue();
  }
}
