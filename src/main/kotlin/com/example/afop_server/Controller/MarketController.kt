package com.example.afop_server.Controller

import com.example.afop_server.Response.Result
import com.example.afop_server.Response.Response
import com.example.afop_server.Advice.Exception.Common.EmptyDataException
import com.example.afop_server.Model.MarketDTO
import com.example.afop_server.Repository.MarketRepository
import com.example.afop_server.Repository.UserRepository
import com.example.afop_server.Service.FileStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/market")
class MarketController(private val userRepository: UserRepository, private val marketRepository: MarketRepository) {

    @RequestMapping(path = ["/items"], method = [RequestMethod.GET])
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun marketGetList(@Param("title") title: String?, @Param("last_id_cursor") last_id_cursor: Long?): Result<List<MarketDTO?>> {

        if (title.isNullOrEmpty() && last_id_cursor == null) {
            marketRepository.getList().let {
                return Result(data = it, response = Response(success = true))
            }
        }

        if (title.isNullOrEmpty() && last_id_cursor != null) {
            marketRepository.getList(last_id_cursor = last_id_cursor).let {
                return Result(data = it, response = Response(success = true))
            }
        }

        if (!title.isNullOrEmpty() && last_id_cursor != null) {
            marketRepository.getList(title = title, last_id_cursor = last_id_cursor).let {
                return Result(data = it, response = Response(success = true))
            }
        }
        throw Exception()
    }

    @RequestMapping(path = ["/item"], method = [RequestMethod.GET])
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun marketGetItem(@Param("market_id") market_id: String?): Result<MarketDTO?> {
        if (market_id != null) {
            marketRepository.getItem(market_id = market_id)?.let {
                return Result(data = it, response = Response(success = true))
            }
        }
        throw EmptyDataException()
    }

    @PostMapping("/item")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun marketPutItem(@RequestBody item: MarketDTO): Result<MarketDTO> {
        item.apply {
            userRepository.findByEmail(SecurityContextHolder.getContext().authentication.name)?.apply {
                sellerUID = getID()
                timeStamp = Date().time
            }
            marketRepository.save(this)
            return Result(data = null, response = Response(success = true))
        }
        //throw EmptyDataException()
    }

    @PutMapping("/item")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun marketModifyItem(@RequestBody item: MarketDTO?): Result<MarketDTO> {
        item?.apply {
            if(sellerUID.isNullOrEmpty()) {
                throw EmptyDataException()
            }
            marketRepository.save(this)
            return Result(data = this, response = Response(success = true))
        }
        throw EmptyDataException()
    }
}