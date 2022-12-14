package com.tnh.authservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@RedisHash("UserRedis")
@Getter
@Setter
@NoArgsConstructor
public class UserRedis implements Serializable {

    @Id
    private String email;
    private String firstName;
    private String lastName;

}
