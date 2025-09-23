package top.phakeandy.youchat.test;

import net.datafaker.Faker;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.phakeandy.youchat.model.Users;

// 设计为 final 类，并提供私有构造函数，防止被实例化
public final class TestDataFaker {

    // 创建一个静态的 Faker 实例，支持中文
    private static final Faker FAKER = new Faker(Locale.CHINA);

    private TestDataFaker() {
        // 私有构造函数，这是一个工具类
    }

    /**
     * 创建一个带有随机数据的 Users 对象。
     * 为了方便登录测试，我们使用一个固定的、已知的原始密码。
     *
     * @param passwordEncoder 用于加密密码的编码器
     * @return 一个填充了随机数据的 Users 实例
     */
    public static Users createRandomUser(PasswordEncoder passwordEncoder) {
        String rawPassword = "password123"; // 使用一个固定的密码方便测试

        Users user = new Users();
        // 生成符合验证规则的用户名：字母开头，包含字母数字，长度4-20
        String username = "user" + FAKER.number().digits(6);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setNickname(FAKER.name().fullName()); // 将生成如 "张伟", "李娜" 等中文名
        user.setAvatarUrl("default-avatar.png"); // 头像通常可以是固定的

        return user;
    }

    /**
     * 如果需要，也可以创建一个返回原始密码的版本，用于登录请求体的构建
     */
    public static class UserWithRawPassword {
        public final Users user;
        public final String rawPassword;

        public UserWithRawPassword(Users user, String rawPassword) {
            this.user = user;
            this.rawPassword = rawPassword;
        }
    }

    public static UserWithRawPassword createRandomUserWithRawPassword(PasswordEncoder passwordEncoder) {
        String rawPassword = "ValidPassword123!";
        Users user = createRandomUser(passwordEncoder, rawPassword); // 重载一个方法
        return new UserWithRawPassword(user, rawPassword);
    }

    private static Users createRandomUser(PasswordEncoder passwordEncoder, String rawPassword) {
        // ... 和上面类似的实现 ...
        Users user = new Users();
        // 生成符合验证规则的用户名：字母开头，包含字母数字，长度4-20
        String username = "user" + FAKER.number().digits(6);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setNickname(FAKER.name().fullName());
        user.setAvatarUrl("default-avatar.png");
        return user;
    }
}