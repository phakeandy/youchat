package top.phakeandy.youchat.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("integration-test")
class PasswordEncoderIntegrationTest extends TestBase {

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void shouldEncodePassword_whenRawPasswordProvided() {
    String rawPassword = "password123";
    String encodedPassword = passwordEncoder.encode(rawPassword);

    assertThat(encodedPassword).isNotNull();
    assertThat(encodedPassword).isNotEqualTo(rawPassword);
    assertThat(encodedPassword).startsWith("$2a$"); // BCrypt format
  }

  @Test
  void shouldMatchPassword_whenCorrectPasswordProvided() {
    String rawPassword = "password123";
    String encodedPassword = passwordEncoder.encode(rawPassword);

    boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
    assertThat(matches).isTrue();
  }

  @Test
  void shouldNotMatchPassword_whenIncorrectPasswordProvided() {
    String rawPassword = "password123";
    String wrongPassword = "wrongpassword";
    String encodedPassword = passwordEncoder.encode(rawPassword);

    boolean matches = passwordEncoder.matches(wrongPassword, encodedPassword);
    assertThat(matches).isFalse();
  }

  @Test
  void shouldGenerateDifferentHashes_forSamePassword() {
    String rawPassword = "password123";
    String encoded1 = passwordEncoder.encode(rawPassword);
    String encoded2 = passwordEncoder.encode(rawPassword);

    assertThat(encoded1).isNotEqualTo(encoded2);

    // Both should match the original password
    assertThat(passwordEncoder.matches(rawPassword, encoded1)).isTrue();
    assertThat(passwordEncoder.matches(rawPassword, encoded2)).isTrue();
  }

  @Test
  void shouldHandleEmptyPassword() {
    String emptyPassword = "";
    String encoded = passwordEncoder.encode(emptyPassword);

    assertThat(encoded).isNotNull();
    assertThat(passwordEncoder.matches(emptyPassword, encoded)).isTrue();
    assertThat(passwordEncoder.matches("", encoded)).isTrue();
  }
}
