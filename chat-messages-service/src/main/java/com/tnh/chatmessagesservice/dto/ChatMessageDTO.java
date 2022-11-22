package com.tnh.chatmessagesservice.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDTO implements Serializable {

    private String id;
    private Long friendChat;
    private String sender;
    private String recipient;
    private String content;
    private String time;
    private String status;
    private String type;

}
