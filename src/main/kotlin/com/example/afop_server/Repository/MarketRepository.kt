package com.example.afop_server.Repository

import com.example.afop_server.Model.MarketDTO
import com.example.afop_server.Model.User
import org.springframework.data.jpa.repository.JpaRepository

interface MarketRepository : JpaRepository<MarketDTO?, Long> {
}