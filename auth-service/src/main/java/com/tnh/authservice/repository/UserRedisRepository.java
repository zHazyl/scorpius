package com.tnh.authservice.repository;

import com.tnh.authservice.domain.User;
import com.tnh.authservice.domain.UserRedis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRedisRepository {
    private final RedisTemplate template;

    public UserRedisRepository(RedisTemplate template) {
        this.template = template;
    }

    public static final String HASH_KEY = "UserRedis";

    public User save(User user) {
        template.opsForHash().put(HASH_KEY, user.getEmail(),
                new UserRedis(user.getUsername(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName()));
        return user;
    }

    public User findUserByEmail(String email) {
        UserRedis userRedis = (UserRedis) template.opsForHash().get(HASH_KEY, email);
        User user = new User();
        user.setUsername(userRedis.getUsername());
        user.setEmail(email);
        user.setFirstName(userRedis.getFirstName());
        user.setLastName(userRedis.getLastName());
        return user;
    }

    public void deleteUser(String email) {
        template.opsForHash().delete(HASH_KEY, email);
    }

}
