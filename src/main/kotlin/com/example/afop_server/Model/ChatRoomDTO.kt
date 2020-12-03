package com.example.afop_server.Model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "chat_room")
data class ChatRoomDTO(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(columnDefinition = "CHAR(36)")
        val id: String = "",

        @ElementCollection(fetch = FetchType.EAGER)
        val member: MutableSet<String> = mutableSetOf()
)