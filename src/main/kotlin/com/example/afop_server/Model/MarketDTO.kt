package com.example.afop_server.Model

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.*

@Entity
@IdClass(MarketDTOId::class)
@Table(name = "market")
data class MarketDTO(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(columnDefinition = "CHAR(36)")
        val id: String, //글 아이디

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "seq", unique = true)
        val seq: Long,

        var sellerUID: String? = null, //판매자
        val buyerUID: String? = null, //구매자
        var title: String? = null, //제목

        @Column(length = 1024)
        var content: String? = null, //글 본문
        var price: String? = null, //가격
        var state: Int = State.SOLD.ordinal, //상태, 판매중, 예약중, 판매완료
        var negotiation: Boolean? = false, //흥정 가능
        var timeStamp: Long? = null,
        var lookUpCount: Long? = null, //조회수
        var category: String? = null,

        @ElementCollection(fetch = FetchType.EAGER)
        var images: List<String>? = null//사진, 10장까지
) {
    enum class State(val state: String) {
        SOLD("판매중"),
        RESERVATION("예약중"),
        SOLD_OUT("판매완료")
    }

    enum class Category(val category: String) {

    }
}

class MarketDTOId : Serializable {
    val id: String? = null
    val seq: Long? = null
}
