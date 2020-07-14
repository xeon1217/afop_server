package com.example.afop_server.Controller

import com.example.afop_server.Advice.Exception.Common.*
import com.example.afop_server.Advice.Exception.Auth.*
import com.example.afop_server.Common.Log
import com.example.afop_server.Config.Security.JwtTokenProvider
import com.example.afop_server.Model.User
import com.example.afop_server.Repository.UserRepository
import com.example.afop_server.Response.CommonResult
import com.example.afop_server.Response.SingleResult
import com.example.afop_server.Service.ResponseService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*


/**
 * 회원 인증 관련
 * 회원의 회원가입 및 로그인 과정을 담당하는 Controller
 */

@RestController
@RequestMapping("/auth")
class AuthController(private val passwordEncoder: PasswordEncoder, private val jwtTokenProvider: JwtTokenProvider, private val userRepository: UserRepository, private val responseService: ResponseService) {
    private val tag = AuthController::class.simpleName
    private val VAILDTIME = 1000L * 300; // 인증코드 유효시간 1000ms * 300s = 5m

    //로그인
    @PostMapping("/signin")
    fun signIn(@RequestBody body: Map<String, String>): SingleResult<Map<String, String>> {
        val email = body["email"]
        val password = body["password"]

        //값이 유효한지 검사
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            throw EmptyDataException()
        }

        //해당되는 계정이 존재하는가?
        userRepository.findByEmail(email)?.let { user ->
            if (!user.isEnabled) { //회원가입중인 계정인가?
                if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //회원가입 중 코드 입력 시간이 지나 만료된 계정인가?
                    userRepository.deleteById(user.getPk())
                    throw CodeTimeoutException() //회원가입 중 코드 입력 시간이 지나서 만료된 계정임!
                }
                throw SignUpUserException() //회원가입 중인 계정임!
            }

            if(!user.isCredentialsNonExpired) { //패스워드가 만료되었는가?
                Log.debug(tag, "패스워드가 만료됨!!!")
                throw ExpiredPasswordException()
            }

            if (passwordEncoder.matches(password, user.password)) { //패스워드가 유효한가?
                return responseService.getSingleResult(mapOf("token" to jwtTokenProvider.createToken(user.getPk().toString(), user.getRole())))
            }
        }
        throw SignInFailedException() //해당되는 계정이 존재하지 않음!
    }

    //회원가입
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun signUp(@RequestBody body: Map<String, String>): CommonResult {
        val email = body["email"] // 이메일
        val password = body["password"] // 기준 패스워드
        val rPassword = body["rPassword"] // 재 확인 패스워드
        val name = body["name"] // 이름
        val nickName = body["nickName"] // 닉네임

        //값이 유효한지 검사
        if (email.isNullOrEmpty() || password.isNullOrEmpty() || rPassword.isNullOrEmpty() || name.isNullOrEmpty() || nickName.isNullOrEmpty()) {
            throw EmptyDataException()
        }

        if(password != rPassword) {
            throw WrongPasswordException() // 패스워드가 서로 다름!
        }

        if (userRepository.findByNickName(nickName) != null) {
            throw AlreadyUserNickNameException()
        }

        userRepository.findByEmail(email)?.let { user ->
            if (!user.isEnabled) { //회원가입중인 계정인가?
                if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //회원가입 중 코드 입력 시간이 지나 만료된 계정인가?
                    userRepository.deleteById(user.getPk())
                    userRepository.save(User(email, passwordEncoder.encode(password), name, nickName, createCode(), createCode().toString(), Collections.singletonList("ROLE_USER")))
                    //이 곳에 이메일로 인증코드를 보내는 코드를 작성
                    return responseService.getSuccessResult()
                }
                throw SignUpUserException() //회원가입 중인 계정임!
            }
            throw AlreadyUserEmailException() //중복됨!
        }

        userRepository.save(User(email, passwordEncoder.encode(password), name, nickName, createCode(), createCode().toString(), Collections.singletonList("ROLE_USER")))
        //이 곳에 이메일로 인증코드를 보내는 코드를 작성
        return responseService.getSuccessResult()
    }


    //회원가입 전 인증코드 입력
    @RequestMapping(path = ["/signup/{email}"], method = [RequestMethod.PATCH])
    fun signUpCode(@PathVariable email: String, @RequestParam code: String): CommonResult {

        //값이 유효한지 검사
        if (email.isEmpty() || code.isEmpty()) {
            throw EmptyDataException()
        }

        //회원가입 중인 계정이 존재하는가?
        userRepository.findByEmail(email)?.let { user ->
            if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //회원가입 중 코드 입력 시간이 지나 만료된 계정인가?
                userRepository.deleteById(user.getPk())
                throw CodeTimeoutException() //회원가입 중 코드 입력 시간이 지나 만료된 계정임!
            }

            //인증코드가 같은가?
            if (compareToCode(user.getCode(), code)) {
                initCode(user)
                user.activation()
                userRepository.save(user)
                return responseService.getSuccessResult()
            }
            throw WrongCodeException() //인증코드가 틀림!
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    //회원가입 전 이메일 중복 확인
    @RequestMapping(path = ["/signup/{email}"], method = [RequestMethod.GET])
    fun doubleCheckEmail(@PathVariable email: String): CommonResult {

        //값이 유효한지 검사
        if (email.isEmpty()) {
            throw EmptyDataException()
        }

        userRepository.findByEmail(email)?.let { user ->
            if (!user.isEnabled) { //회원가입중인 계정인가?
                if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //회원가입 중 코드 입력 시간이 지나 만료된 계정인가?
                    userRepository.deleteById(user.getPk())
                    return responseService.getSuccessResult()
                }
                throw SignUpUserException() //회원가입 중인 계정임!
            }
            throw AlreadyUserEmailException() //중복됨!
        }
        return responseService.getSuccessResult()
    }

    //이메일 찾기
    @RequestMapping(path = ["/email/{nickName}"], method = [RequestMethod.GET])
    fun findEmail(@PathVariable nickName: String, @RequestParam name: String): SingleResult<Map<String, String>> {

        //값이 유효한지 검사
        if (nickName.isEmpty() || name.isEmpty()) {
            throw EmptyDataException()
        }

        //해당되는 계정이 존재하는가?
        userRepository.findByNickName(nickName)?.let { user ->
            if (!user.isEnabled) { //회원가입 중인 계정인가?
                if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //회원가입 중 코드 입력 시간이 지나 만료된 계정인가?
                    userRepository.deleteById(user.getPk())
                    throw CodeTimeoutException() //회원가입 중 코드 입력 시간이 지나서 만료된 계정임!
                }
                throw SignUpUserException() //회원가입 중인 계정임!
            }

            //해당되는 계정을 찾음!
            if (user.getName() == name) {
                return responseService.getSingleResult(mapOf("email" to user.username))
            }
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    //패스워드 찾기
    @RequestMapping(path = ["/password/{email}"], method = [RequestMethod.GET])
    fun findPassword(@PathVariable("email") email: String, @RequestParam name: String): CommonResult {

        //값이 유효한지 검사
        if (email.isEmpty() || name.isEmpty()) {
            throw EmptyDataException()
        }

        //해당 되는 계정이 존재하는가?
        userRepository.findByEmail(email)?.let { user ->
            if (!user.isEnabled) { //회원가입 중인 계정인가?
                if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //회원가입 중 코드 입력 시간이 지나 만료된 계정인가?
                    userRepository.deleteById(user.getPk())
                    throw CodeTimeoutException() //회원가입 중 코드 입력 시간이 지나서 만료된 계정임!
                }
                throw SignUpUserException() //회원가입 중인 계정임!
            }

            //해당되는 계정을 찾음!
            if (user.getName() == name) {
                user.setCode(createCode().toString())
                userRepository.save(user)
                return responseService.getSuccessResult()
            }
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    //패스워드 찾기 전 인증코드 입력
    @RequestMapping(path = ["/password/{email}"], method = [RequestMethod.PATCH])
    fun findPasswordCode(@PathVariable email: String, @RequestParam code: String): SingleResult<Map<String, String>> {

        //값이 유효한지 검사
        if (email.isEmpty() || code.isEmpty()) {
            throw EmptyDataException()
        }

        //해당 되는 계정이 존재하는가?
        userRepository.findByEmail(email)?.let { user ->
            if (!user.isEnabled) { //회원가입 중인 계정인가?
                if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //회원가입 중 코드 입력 시간이 지나 만료된 계정인가?
                    userRepository.deleteById(user.getPk())
                    throw CodeTimeoutException() //회원가입 중 코드 입력 시간이 지나서 만료된 계정임!
                }
                throw SignUpUserException() //회원가입 중인 계정임!
            }

            if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //패스워드 찾기 중 코드 입력 시간이 지나 만료된 계정인가?
                initCode(user)
                userRepository.save(user)
                throw CodeTimeoutException() //패스워드 찾기 중 코드 입력 시간이 지나서 만료된 계정임!
            }

            /*
            if(!user.isCredentialsNonExpired) {
                Log.debug(tag, "만료되지 않음?")
                throw Exception() // 만료된 상태에서만 가능!
             }
             */

            //인증코드가 같은가?
            if (compareToCode(user.getCode(), code)) {
                initCode(user)
                //user.activation()
                user.setRole(Collections.singletonList("ROLE_PW"))
                user.credentialsDeActivation()
                userRepository.save(user)
                return responseService.getSingleResult(mapOf("token" to jwtTokenProvider.createChangePasswordToken(user.getPk().toString(), Collections.singletonList("ROLE_PW"))))
            }
            throw WrongCodeException() //인증코드가 틀림!
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    @RequestMapping(path = ["/password"], method = [RequestMethod.PATCH])
    fun changePassword(@RequestBody body: Map<String, String>): CommonResult {
        val password = body["password"]
        val rPassword = body["rPassword"]
        val authentication: Authentication = SecurityContextHolder.getContext().authentication

        //값이 유효한지 검사
        if (password.isNullOrEmpty() || rPassword.isNullOrEmpty()) {
            throw EmptyDataException()
        }
        Log.debug(tag, password)
        Log.debug(tag, rPassword)

        userRepository.findByEmail(authentication.name)?.let { user ->
            if(password != rPassword) {
                Log.debug(tag, "패스워드가 맞지 않음..")
                throw WrongPasswordException() // 패스워드가 맞지 않음!
            }

            user.password = passwordEncoder.encode(password)
            user.setRole(Collections.singletonList("ROLE_USER"))
            user.credentialsActivation()
            userRepository.save(user)
            return responseService.getSuccessResult()
        }
        throw UserNotFoundException()
    }

    //코드를 생성함
    fun createCode(): Long {
        return Date().time
    }

    //저장된 코드와 사용자가 입력한 코드를 비교함
    fun compareToCode(userCode: String, compareCode: String): Boolean {
        Log.debug(tag, userCode)
        Log.debug(tag, compareCode)
        return (userCode.reversed().substring(0, 6) == compareCode.replace("\"", ""))
    }

    //해당 계정이 만료(제한시간 내 코드 미입력)되었는지 확인함
    fun expiredCheck(user: User): Boolean {
        if ((user.getCode().toLong() + VAILDTIME) < createCode()) { // 만료됨
            userRepository.deleteById(user.getPk())
        }
        return true
    }

    //코드를 초기화 함
    fun initCode(user: User) {
        user.setCode("")
        userRepository.save(user)
    }
}