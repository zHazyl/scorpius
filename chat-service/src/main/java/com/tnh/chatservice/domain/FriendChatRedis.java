package com.tnh.chatservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("FriendChatRedis")
public class FriendChatRedis implements Serializable {
    @Id
    private Long id;
    private Long chatWith;
    private String sender;
    private String recipient;
}
