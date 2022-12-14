package com.tnh.authservice.repository;

import com.tnh.authservice.domain.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRedisRepository {
    private final RedisTemplate template;

    public UserRedisRepository(RedisTemplate template) {
        this.template = template;
    }

    public static final String HASH_KEY = "User";

    public User save(User user) {
        template.opsForHash().put(HASH_KEY, user.getEmail(), user);
        return user;
    }

    public User findUserByEmail(String email) {
        return (User) template.opsForHash().get(HASH_KEY, email);
    }

    public void deleteUser(String email) {
        template.opsForHash().delete(HASH_KEY, email);
    }

}
