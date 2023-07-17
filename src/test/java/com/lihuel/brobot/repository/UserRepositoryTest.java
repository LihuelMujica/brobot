package com.lihuel.brobot.repository;

import com.lihuel.brobot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void crud() {
        User user = new User();
        user.setDiscordId("123");
        user.setSteamUserId("123");
        userRepository.save(user);

        User user2 = userRepository.findById(user.getSteamUserId()).orElse(null);

        assertNotNull(user2);
        assertEquals(user.getDiscordId(), user2.getDiscordId());
        assertEquals(user.getSteamUserId(), user2.getSteamUserId());

        userRepository.delete(user);
    }

}