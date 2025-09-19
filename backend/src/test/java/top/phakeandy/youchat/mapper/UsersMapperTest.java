package top.phakeandy.youchat.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import top.phakeandy.youchat.model.Users;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static top.phakeandy.youchat.mapper.UsersDynamicSqlSupport.*;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;

@Testcontainers
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
class UsersMapperTest {

    @Autowired
    private UsersMapper usersMapper;

    @Test
    void shouldInsertUser() {
        Users user = createTestUser();

        int result = usersMapper.insertSelective(user);

        assertThat(result).isEqualTo(1);

        // Re-fetch the user to get the generated ID
        Optional<Users> insertedUser = usersMapper.selectOne(c ->
                c.where(username, isEqualTo(user.getUsername())));
        assertThat(insertedUser).isPresent();
        assertThat(insertedUser.get().getId()).isNotNull();
    }

    @Test
    void shouldFindUserByUsername() {
        Users user = createTestUser();
        usersMapper.insertSelective(user);

        Optional<Users> foundUser = usersMapper.selectOne(c -> c
                .where(username, isEqualTo(user.getUsername())));

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(foundUser.get().getNickname()).isEqualTo(user.getNickname());
    }

    @Test
    void shouldFindUserById() {
        Users user = createTestUser();
        usersMapper.insertSelective(user);

        // Re-fetch the user to get the generated ID
        Optional<Users> insertedUser = usersMapper.selectOne(c ->
                c.where(username, isEqualTo(user.getUsername())));
        assertThat(insertedUser).isPresent();

        Optional<Users> foundUser = usersMapper.selectByPrimaryKey(insertedUser.get().getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(insertedUser.get().getId());
    }

    @Test
    void shouldUpdateUser() {
        Users user = createTestUser();
        usersMapper.insertSelective(user);

        // Re-fetch the user to get the generated ID
        Optional<Users> insertedUser = usersMapper.selectOne(c ->
                c.where(username, isEqualTo(user.getUsername())));
        assertThat(insertedUser).isPresent();

        Users userToUpdate = new Users();
        userToUpdate.setId(insertedUser.get().getId());
        userToUpdate.setNickname("Updated Nickname");
        userToUpdate.setAvatarUrl("https://example.com/new-avatar.jpg");

        int result = usersMapper.updateByPrimaryKeySelective(userToUpdate);

        assertThat(result).isEqualTo(1);

        Optional<Users> updatedUser = usersMapper.selectByPrimaryKey(userToUpdate.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getNickname()).isEqualTo("Updated Nickname");
        assertThat(updatedUser.get().getAvatarUrl()).isEqualTo("https://example.com/new-avatar.jpg");
    }

    @Test
    void shouldUpdateUserSelective() {
        Users user = createTestUser();
        usersMapper.insertSelective(user);

        // Re-fetch the user to get the generated ID
        Optional<Users> insertedUser = usersMapper.selectOne(c ->
                c.where(username, isEqualTo(user.getUsername())));
        assertThat(insertedUser).isPresent();

        Users updateData = new Users();
        updateData.setId(insertedUser.get().getId());
        updateData.setNickname("Updated Nickname Only");

        int result = usersMapper.updateByPrimaryKeySelective(updateData);

        assertThat(result).isEqualTo(1);

        Optional<Users> updatedUser = usersMapper.selectByPrimaryKey(insertedUser.get().getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getNickname()).isEqualTo("Updated Nickname Only");
        assertThat(updatedUser.get().getAvatarUrl()).isEqualTo(user.getAvatarUrl());
    }

    @Test
    void shouldDeleteUser() {
        Users user = createTestUser();
        usersMapper.insertSelective(user);

        // Re-fetch the user to get the generated ID
        Optional<Users> insertedUser = usersMapper.selectOne(c ->
                c.where(username, isEqualTo(user.getUsername())));
        assertThat(insertedUser).isPresent();

        int result = usersMapper.deleteByPrimaryKey(insertedUser.get().getId());

        assertThat(result).isEqualTo(1);

        Optional<Users> deletedUser = usersMapper.selectByPrimaryKey(insertedUser.get().getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void shouldCountUsers() {
        long initialCount = usersMapper.count(CountDSLCompleter.allRows());

        Users user = createTestUser();
        usersMapper.insertSelective(user);

        long finalCount = usersMapper.count(CountDSLCompleter.allRows());

        assertThat(finalCount).isEqualTo(initialCount + 1);
    }

    @Test
    void shouldFindMultipleUsers() {
        Users user1 = createTestUser();
        user1.setUsername("user1");
        usersMapper.insertSelective(user1);

        Users user2 = createTestUser();
        user2.setUsername("user2");
        usersMapper.insertSelective(user2);

        var users = usersMapper.select(c -> c
                .where(username, isIn("user1", "user2"))
                .orderBy(username));

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("user1");
        assertThat(users.get(1).getUsername()).isEqualTo("user2");
    }

    private Users createTestUser() {
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setNickname("Test User");
        user.setAvatarUrl("https://example.com/avatar.jpg");
        // Let database auto-generate created_at and updated_at timestamps
        return user;
    }
}