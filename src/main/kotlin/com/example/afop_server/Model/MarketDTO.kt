package com.example.afop_server.Model

import javax.persistence.*

@Entity
@Table(name = "markets")
data class MarketDTO(
        @Id
        var marketID: Long? = null, //글 아이디
        var sellerUID: String? = null, //판매자
        var buyerUID: String? = null, //구매자
        var title: String? = null, //제목
        var content: String? = null, //글 본문
        var price: String? = null, //가격
        var sold: Boolean? = false, //판매 상태
        var reservation: Boolean? = false, //예약 상태
        var negotiation: Boolean? = false, //흥정 가능
        var timeStamp: Long? = null,
        var lookUpCount: Long? = null, //조회횟수
        var category: String? = null,

        @ElementCollection(fetch = FetchType.EAGER)
        var photos: List<String>? = null//사진, 10장까지
)