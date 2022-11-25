package com.tnh.chatmessagesservice.document;

import com.tnh.chatmessagesservice.constant.MessageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "chat_message")
public class ChatMessage {

    @Id
    private String id;

    @Field(name = "friend_chat")
    private Long friendChat;
    private String sender;
    private String recipient;
    private String content;
    private Date time;
    private MessageStatus status;
    private String type;

}
