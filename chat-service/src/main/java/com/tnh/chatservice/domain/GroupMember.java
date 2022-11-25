package com.tnh.chatservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(schema = "chat_service_database", name = "group_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "member_id"}))
@Entity
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupChat group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private ChatProfile member;

    @Column(nullable = false, name = "is_admin")
    private boolean isAdmin = false;

}
