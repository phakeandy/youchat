package top.phakeandy.youchat.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.phakeandy.youchat.mapper.UsersMapper;
import top.phakeandy.youchat.model.Users;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

  @Mock private UsersMapper usersMapper;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private CustomUserDetailsService userService;

  private Users testUsers;
  private CustomUserDetails testUser;

  @BeforeEach
  void setUp() {
    testUsers = new Users();
    testUsers.setId(1L);
    testUsers.setUsername("testuser");
    testUsers.setPassword("encodedPassword");
    testUsers.setNickname("Test User");
    testUsers.setAvatarUrl("https://example.com/avatar.jpg");
    testUsers.setCreatedAt(new Date());
    testUsers.setUpdatedAt(new Date());

    testUser = new CustomUserDetails(testUsers);
  }

  @Test
  void shouldLoadUserByUsernameSuccessfully() {
    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.of(testUsers));

    CustomUserDetails result = (CustomUserDetails) userService.loadUserByUsername("testuser");

    assertThat(result).isNotNull();
    assertThat(result.getUsername()).isEqualTo("testuser");
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getAuthorities()).hasSize(1);
    assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");

    verify(usersMapper).selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class));
  }

  @Test
  void shouldThrowExceptionWhenUserNotFound() {
    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.loadUserByUsername("nonexistent"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("User not found with username: nonexistent");

    verify(usersMapper).selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class));
  }

  @Test
  void shouldFindUserByUsername() {
    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.of(testUsers));

    Optional<CustomUserDetails> result = userService.findByUsername("testuser");

    assertThat(result).isPresent();
    assertThat(result.get().getUsername()).isEqualTo("testuser");
    assertThat(result.get().getId()).isEqualTo(1L);

    verify(usersMapper).selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class));
  }

  @Test
  void shouldReturnEmptyWhenUserNotFoundByUsername() {
    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.empty());

    Optional<CustomUserDetails> result = userService.findByUsername("nonexistent");

    assertThat(result).isEmpty();

    verify(usersMapper).selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class));
  }

  @Test
  void shouldFindUserById() {
    when(usersMapper.selectByPrimaryKey(1L)).thenReturn(Optional.of(testUsers));

    Optional<CustomUserDetails> result = userService.findById(1L);

    assertThat(result).isPresent();
    assertThat(result.get().getId()).isEqualTo(1L);
    assertThat(result.get().getUsername()).isEqualTo("testuser");

    verify(usersMapper).selectByPrimaryKey(1L);
  }

  @Test
  void shouldReturnEmptyWhenUserNotFoundById() {
    when(usersMapper.selectByPrimaryKey(1L)).thenReturn(Optional.empty());

    Optional<CustomUserDetails> result = userService.findById(1L);

    assertThat(result).isEmpty();

    verify(usersMapper).selectByPrimaryKey(1L);
  }

  @Test
  void shouldCreateUserSuccessfully() {
    Users newUser = new Users();
    newUser.setUsername("newuser");
    newUser.setPassword("ValidPassword123!");
    newUser.setNickname("New User");
    newUser.setAvatarUrl("https://example.com/new-avatar.jpg");

    Users savedUser = new Users();
    savedUser.setId(2L);
    savedUser.setUsername("newuser");
    savedUser.setPassword("encodedValidPassword123!");
    savedUser.setNickname("New User");
    savedUser.setAvatarUrl("https://example.com/new-avatar.jpg");

    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(savedUser));
    when(usersMapper.insertSelective(any())).thenReturn(1);
    when(passwordEncoder.encode("ValidPassword123!")).thenReturn("encodedValidPassword123!");

    CustomUserDetails result = userService.createUser(newUser);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(2L);
    assertThat(result.getUsername()).isEqualTo("newuser");
    assertThat(result.getPassword()).isEqualTo("encodedValidPassword123!");

    ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
    verify(usersMapper).insertSelective(userCaptor.capture());
    assertThat(userCaptor.getValue().getPassword()).isEqualTo("encodedValidPassword123!");
  }

  @Test
  void shouldThrowExceptionWhenCreatingUserWithExistingUsername() {
    Users newUser = new Users();
    newUser.setUsername("existinguser");
    newUser.setPassword("ValidPassword123!");

    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.of(testUsers));

    assertThatThrownBy(() -> userService.createUser(newUser))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Username already exists: existinguser");

    verify(usersMapper, never()).insertSelective(any());
  }

  @Test
  void shouldCreateUserWithParameters() {
    Users savedUser = new Users();
    savedUser.setId(3L);
    savedUser.setUsername("paramuser");
    savedUser.setPassword("encodedParamPassword123!");
    savedUser.setNickname("Param User");
    savedUser.setAvatarUrl("https://example.com/param-avatar.jpg");

    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(savedUser));
    when(usersMapper.insertSelective(any())).thenReturn(1);
    when(passwordEncoder.encode("ParamPassword123!")).thenReturn("encodedParamPassword123!");

    CustomUserDetails result =
        userService.createUser(
            "paramuser", "ParamPassword123!", "Param User", "https://example.com/param-avatar.jpg");

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(3L);
    assertThat(result.getUsername()).isEqualTo("paramuser");
    assertThat(result.getNickname()).isEqualTo("Param User");

    ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
    verify(usersMapper).insertSelective(userCaptor.capture());
    Users capturedUser = userCaptor.getValue();
    assertThat(capturedUser.getUsername()).isEqualTo("paramuser");
    assertThat(capturedUser.getPassword()).isEqualTo("encodedParamPassword123!");
    assertThat(capturedUser.getNickname()).isEqualTo("Param User");
    assertThat(capturedUser.getAvatarUrl()).isEqualTo("https://example.com/param-avatar.jpg");
  }

  @Test
  void shouldCheckPasswordCorrectly() {
    when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

    boolean result = userService.checkPassword("rawPassword", "encodedPassword");

    assertThat(result).isTrue();
    verify(passwordEncoder).matches("rawPassword", "encodedPassword");
  }

  @Test
  void shouldReturnFalseForIncorrectPassword() {
    when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

    boolean result = userService.checkPassword("wrongPassword", "encodedPassword");

    assertThat(result).isFalse();
    verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
  }

  @Test
  void shouldChangePasswordSuccessfully() {
    when(usersMapper.selectByPrimaryKey(1L)).thenReturn(Optional.of(testUsers));
    when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
    when(passwordEncoder.encode("NewPassword123!")).thenReturn("encodedNewPassword123!");
    when(usersMapper.updateByPrimaryKeySelective(any())).thenReturn(1);

    userService.changePassword(1L, "oldPassword", "NewPassword123!");

    ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
    verify(usersMapper).updateByPrimaryKeySelective(userCaptor.capture());
    Users capturedUser = userCaptor.getValue();
    assertThat(capturedUser.getId()).isEqualTo(1L);
    assertThat(capturedUser.getPassword()).isEqualTo("encodedNewPassword123!");
  }

  @Test
  void shouldThrowExceptionWhenChangingPasswordWithWrongOldPassword() {
    when(usersMapper.selectByPrimaryKey(1L)).thenReturn(Optional.of(testUsers));
    when(passwordEncoder.matches("wrongOldPassword", "encodedPassword")).thenReturn(false);

    assertThatThrownBy(() -> userService.changePassword(1L, "wrongOldPassword", "NewPassword123!"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid old password");

    verify(usersMapper, never()).updateByPrimaryKeySelective(any());
  }

  @Test
  void shouldThrowExceptionWhenChangingPasswordForNonexistentUser() {
    when(usersMapper.selectByPrimaryKey(999L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.changePassword(999L, "oldPassword", "NewPassword123!"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("User not found with id: 999");

    verify(usersMapper, never()).updateByPrimaryKeySelective(any());
  }

  @Test
  void shouldUpdateUserSuccessfully() {
    when(usersMapper.updateByPrimaryKeySelective(any())).thenReturn(1);
    when(usersMapper.selectByPrimaryKey(1L)).thenReturn(Optional.of(testUsers));

    CustomUserDetails result = userService.updateUser(testUser);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(usersMapper).updateByPrimaryKeySelective(testUser);
    verify(usersMapper).selectByPrimaryKey(1L);
  }

  @Test
  void shouldThrowExceptionWhenUpdatingUserFails() {
    when(usersMapper.updateByPrimaryKeySelective(any())).thenReturn(0);

    assertThatThrownBy(() -> userService.updateUser(testUser))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Failed to update user");

    verify(usersMapper).updateByPrimaryKeySelective(testUser);
    verify(usersMapper, never()).selectByPrimaryKey(any());
  }

  @Test
  void shouldDeleteUserSuccessfully() {
    when(usersMapper.deleteByPrimaryKey(1L)).thenReturn(1);

    userService.deleteUser(1L);

    verify(usersMapper).deleteByPrimaryKey(1L);
  }

  @Test
  void shouldThrowExceptionWhenDeletingUserFails() {
    when(usersMapper.deleteByPrimaryKey(1L)).thenReturn(0);

    assertThatThrownBy(() -> userService.deleteUser(1L))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Failed to delete user");

    verify(usersMapper).deleteByPrimaryKey(1L);
  }

  @Test
  void shouldCheckUsernameExists() {
    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.of(testUsers));

    boolean result = userService.existsByUsername("testuser");

    assertThat(result).isTrue();
    verify(usersMapper).selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class));
  }

  @Test
  void shouldReturnFalseWhenUsernameDoesNotExist() {
    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.empty());

    boolean result = userService.existsByUsername("nonexistent");

    assertThat(result).isFalse();
    verify(usersMapper).selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class));
  }

  @Test
  void shouldValidatePasswordSuccessfully() {
    String validPassword = "ValidPassword123!";
    Users savedUser = new Users();
    savedUser.setId(1L);
    savedUser.setUsername("testuser");
    savedUser.setPassword("encodedPassword");

    when(usersMapper.selectOne(any(org.mybatis.dynamic.sql.select.SelectDSLCompleter.class)))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(savedUser));
    when(usersMapper.insertSelective(any())).thenReturn(1);
    when(passwordEncoder.encode(validPassword)).thenReturn("encodedPassword");

    userService.createUser(
        "testuser", validPassword, "Test User", "https://example.com/avatar.jpg");

    verify(passwordEncoder).encode(validPassword);
  }

  @Test
  void shouldThrowExceptionForNullPassword() {
    assertThatThrownBy(
            () ->
                userService.createUser(
                    "testuser", null, "Test User", "https://example.com/avatar.jpg"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Password cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionForEmptyPassword() {
    assertThatThrownBy(
            () ->
                userService.createUser(
                    "testuser", "", "Test User", "https://example.com/avatar.jpg"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Password cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionForShortPassword() {
    assertThatThrownBy(
            () ->
                userService.createUser(
                    "testuser", "Short1!", "Test User", "https://example.com/avatar.jpg"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Password must be at least 8 characters long");
  }

  @Test
  void shouldThrowExceptionForLongPassword() {
    String longPassword = "a".repeat(129);

    assertThatThrownBy(
            () ->
                userService.createUser(
                    "testuser", longPassword, "Test User", "https://example.com/avatar.jpg"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Password cannot be longer than 128 characters");
  }

  @Test
  void shouldThrowExceptionForPasswordWithoutUppercase() {
    assertThatThrownBy(
            () ->
                userService.createUser(
                    "testuser", "lowercase123!", "Test User", "https://example.com/avatar.jpg"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Password must contain at least one uppercase letter");
  }

  @Test
  void shouldThrowExceptionForPasswordWithoutLowercase() {
    assertThatThrownBy(
            () ->
                userService.createUser(
                    "testuser", "UPPERCASE123!", "Test User", "https://example.com/avatar.jpg"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Password must contain at least one lowercase letter");
  }

  @Test
  void shouldThrowExceptionForPasswordWithoutDigit() {
    assertThatThrownBy(
            () ->
                userService.createUser(
                    "testuser", "NoDigitsHere!", "Test User", "https://example.com/avatar.jpg"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Password must contain at least one digit");
  }

  @Test
  void shouldThrowExceptionForPasswordWithoutSpecialCharacter() {
    assertThatThrownBy(
            () ->
                userService.createUser(
                    "testuser", "NoSpecialChars123", "Test User", "https://example.com/avatar.jpg"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Password must contain at least one special character");
  }
}
