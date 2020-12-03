package com.example.afop_server.Repository

import com.example.afop_server.Model.MarketDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MarketRepository : JpaRepository<MarketDTO?, String> {
    //fun findMarketDTOByOrderByTimeStampDesc(): List<MarketDTO?>
    //fun findMarketDTOByTitleLikeOrderByTimeStampDesc(title: String): List<MarketDTO?>

    @Query(value = "SELECT * FROM market WHERE id = :market_id", nativeQuery = true)
    fun getItem(@Param("market_id") market_id: String): MarketDTO?

    @Query(value = "SELECT * FROM market ORDER BY time_stamp DESC LIMIT 25", nativeQuery = true)
    fun getList(): List<MarketDTO?>

    @Query(value = "SELECT * FROM market WHERE seq < :last_id_cursor ORDER BY time_stamp DESC LIMIT 25", nativeQuery = true)
    fun getList(@Param("last_id_cursor") last_id_cursor: Long): List<MarketDTO?>

    @Query(value = "SELECT * FROM market WHERE seq < :last_id_cursor ORDER BY time_stamp DESC LIMIT 25 LIKE %:title%", nativeQuery = true)
    fun getList(@Param("title") title: String, @Param("last_id_cursor") last_id_cursor: Long): List<MarketDTO?>

}

/*
SELECT id, file_name
FROM file
WHERE file_name > ${lastCursorFileName}
    OR (file_name = ${lastCursorFileName} AND id > ${lastCursorId})
ORDER BY file_name ASC, id ASC
LIMIT 2
 */