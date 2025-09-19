package top.phakeandy.youchat.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base test class with common configurations
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class TestBase {

    @BeforeEach
    void setup() {
        // Common setup for all tests
        // Override in subclasses if needed
    }
}