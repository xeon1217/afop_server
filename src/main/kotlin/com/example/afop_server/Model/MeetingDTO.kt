package com.example.afop_server.Model

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.*

@Entity
@IdClass(MeetingDTOId::class)
@Table(name = "meeting")
data class MeetingDTO (
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(columnDefinition = "CHAR(36)")
        val id: String, //모임 id -> Key

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "seq", unique = true)
        val seq: Long,

        val title: String, //모임 이름
        val introduce: String, //모임 설명

        @ElementCollection(fetch = FetchType.EAGER)
        val member: List<String>, //멤버 리스트
        val category: String
) {
    enum class Type(val type: String) {

    }
}

class MeetingDTOId : Serializable {
    val id: String? = null
    val seq: Long? = null
}