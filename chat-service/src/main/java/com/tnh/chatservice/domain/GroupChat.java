package com.tnh.chatservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(schema = "chat_service_database", name = "group_chat")
@Entity
public class GroupChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", length = 100, nullable = false)
    private String groupName;

    @Column(nullable = false, name = "created_date")
    private OffsetDateTime createdDate;

}
