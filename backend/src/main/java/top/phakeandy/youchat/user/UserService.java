package top.phakeandy.youchat.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.phakeandy.youchat.mapper.UsersDynamicSqlSupport;
import top.phakeandy.youchat.mapper.UsersMapper;
import top.phakeandy.youchat.model.User;
import top.phakeandy.youchat.model.Users;

import java.util.Optional;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        Optional<Users> usersOptional = usersMapper.selectOne(c ->
                c.where(UsersDynamicSqlSupport.username, isEqualTo(username)));

        if (usersOptional.isEmpty()) {
            log.warn("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Users users = usersOptional.get();
        log.debug("User found: {}", users.getUsername());

        return new User(users);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);

        return usersMapper.selectOne(c ->
                c.where(UsersDynamicSqlSupport.username, isEqualTo(username)))
                .map(User::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        log.debug("Finding user by id: {}", id);

        return usersMapper.selectByPrimaryKey(id)
                .map(User::new);
    }

    @Transactional
    public User createUser(Users users) {
        log.debug("Creating new user: {}", users.getUsername());

        // Validate username uniqueness
        if (findByUsername(users.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + users.getUsername());
        }

        // Validate password
        validatePassword(users.getPassword());

        // Encode password before storing
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        // Insert user (ID will be auto-generated)
        int result = usersMapper.insertSelective(users);
        if (result != 1) {
            throw new RuntimeException("Failed to create user");
        }

        // Re-fetch the user to get the generated ID
        return findByUsername(users.getUsername())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created user"));
    }

    @Transactional
    public User createUser(String username, String rawPassword, String nickname, String avatarUrl) {
        log.debug("Creating new user with username: {}", username);

        Users users = new Users();
        users.setUsername(username);
        users.setPassword(rawPassword);
        users.setNickname(nickname);
        users.setAvatarUrl(avatarUrl);

        return createUser(users);
    }

    @Transactional(readOnly = true)
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        log.debug("Checking password for user");
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        log.debug("Changing password for user id: {}", userId);

        User user = findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // Verify old password
        if (!checkPassword(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        // Validate new password
        validatePassword(newPassword);

        // Update password
        Users usersToUpdate = new Users();
        usersToUpdate.setId(userId);
        usersToUpdate.setPassword(passwordEncoder.encode(newPassword));

        int result = usersMapper.updateByPrimaryKeySelective(usersToUpdate);
        if (result != 1) {
            throw new RuntimeException("Failed to update password");
        }
    }

    @Transactional
    public User updateUser(User user) {
        log.debug("Updating user: {}", user.getUsername());

        int result = usersMapper.updateByPrimaryKeySelective(user);
        if (result != 1) {
            throw new RuntimeException("Failed to update user");
        }

        return findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve updated user"));
    }

    @Transactional
    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);

        int result = usersMapper.deleteByPrimaryKey(id);
        if (result != 1) {
            throw new RuntimeException("Failed to delete user");
        }
    }

    @Transactional
    public boolean existsByUsername(String username) {
        log.debug("Checking if username exists: {}", username);

        return usersMapper.selectOne(c ->
                c.where(UsersDynamicSqlSupport.username, isEqualTo(username)))
                .isPresent();
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (password.length() > 128) {
            throw new IllegalArgumentException("Password cannot be longer than 128 characters");
        }

        // Add more validation rules as needed
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
    }
}