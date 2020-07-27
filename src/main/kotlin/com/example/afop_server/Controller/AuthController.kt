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
                user.setTokenCode(createTokenCode())
                userRepository.save(user)
                return responseService.getSingleResult(2000, mapOf("token" to jwtTokenProvider.createToken(user)))
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
        val rPassword = body["vPassword"] // 재 확인 패스워드
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
                    val code = createCode()
                    userRepository.save(User(email, passwordEncoder.encode(password), name, nickName, code, code.toString(), Collections.singletonList("ROLE_USER")))
                    //이 곳에 이메일로 인증코드를 보내는 코드를 작성
                    return responseService.getSuccessResult(2011, "가입 신청 성공!", "이메일로 보내진 인증 코드를 입력하여 회원가입을 마쳐주세요.")
                }
                throw SignUpUserException() //회원가입 중인 계정임!
            }
            throw AlreadyUserEmailException() //중복됨!
        }
        val code = createCode()
        userRepository.save(User(email, passwordEncoder.encode(password), name, nickName, code, code.toString(), Collections.singletonList("ROLE_USER")))
        //이 곳에 이메일로 인증코드를 보내는 코드를 작성
        return responseService.getSuccessResult(2011, "가입 신청 성공!", "이메일로 보내진 인증 코드를 입력하여 회원가입을 마쳐주세요.")
    }

    //테스트 코드 삭제 필
    @RequestMapping(path = ["/code/{email}"], method = [RequestMethod.GET])
    fun testCode(@PathVariable email: String): String {
        userRepository.findByEmail(email)?.let {
            return it.getCode()
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    @RequestMapping(path = ["/signup"], method = [RequestMethod.PATCH])
    fun signUpCodeNull(): CommonResult {
        throw EmptyDataException()
    }

    //회원가입 전 인증코드 입력
    @RequestMapping(path = ["/signup/{email}"], method = [RequestMethod.PATCH])
    fun signUpCode(@PathVariable email: String, @RequestParam code: String): CommonResult {

        //값이 유효한지 검사
        if (code.isEmpty()) {
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
                return responseService.getSuccessResult(2012, "회원가입 완료!", "회원가입에 성공하였습니다.")
            }
            throw WrongCodeException() //인증코드가 틀림!
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    @RequestMapping(path = ["/signup"], method = [RequestMethod.GET])
    fun checkEmailNull(): CommonResult {
        throw EmptyDataException()
    }

    //회원가입 전 이메일 중복 확인
    @RequestMapping(path = ["/signup/{email}"], method = [RequestMethod.GET])
    fun verifyEmail(@PathVariable email: String): CommonResult {
        if (!email.contains("@")) {
            throw Exception() //이메일 양식에 맞지 않음
        }

        userRepository.findByEmail(email)?.let { user ->
            if (!user.isEnabled) { //회원가입중인 계정인가?
                if ((user.getCode().toLong() + VAILDTIME) < createCode()) { //회원가입 중 코드 입력 시간이 지나 만료된 계정인가?
                    userRepository.deleteById(user.getPk())
                    return responseService.getSuccessResult(2010, "사용 가능!", "사용 가능한 이메일 주소입니다.\n사용하시겠습니까?")
                }
                throw SignUpUserException() //회원가입 중인 계정임!
            }
            throw AlreadyUserEmailException() //중복됨!
        }
        return responseService.getSuccessResult(2010, "사용 가능!", "${email}은(는)\n사용 가능한 이메일 주소입니다.\n사용하시겠습니까?")
    }

    //이메일 찾기
    @RequestMapping(path = ["/email"], method = [RequestMethod.GET])
    fun findEmailNull() {
        throw EmptyDataException()
    }

    @RequestMapping(path = ["/email/{nickName}"], method = [RequestMethod.GET])
    fun findEmail(@PathVariable nickName: String, @RequestParam name: String): SingleResult<String> {

        //값이 유효한지 검사
        if (name.isEmpty()) {
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
                return responseService.getSingleResult(2020, "이메일 찾음!", "${name}님의 이메일 주소는 \n${user.username}입니다.\n패스워드도 찾을까요?", user.username)
            }
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    @RequestMapping(path = ["/password"], method = [RequestMethod.GET])
    fun findPasswordNull() {
        throw EmptyDataException()
    }

    //패스워드 찾기
    @RequestMapping(path = ["/password/{email}"], method = [RequestMethod.GET])
    fun findPassword(@PathVariable email: String, @RequestParam name: String): CommonResult {

        //값이 유효한지 검사
        if (name.isEmpty()) {
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
                return responseService.getSuccessResult(2021, "인증코드 전송됨!", "${name}님의 이메일 주소 \n${user.username}으로 인증코드가 전송되었습니다.\n")
            }
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    //패스워드 찾기 전 인증코드 입력
    @RequestMapping(path = ["/password/{email}"], method = [RequestMethod.PATCH])
    fun findPasswordCode(@PathVariable email: String, @RequestParam code: String): SingleResult<Map<String, String>> {

        //값이 유효한지 검사
        if (code.isEmpty()) {
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
                user.setRole(Collections.singletonList("ROLE_PW"))
                user.credentialsDeActivation()
                user.setTokenCode(createTokenCode())
                userRepository.save(user)
                return responseService.getSingleResult(2022, "인증 성공!", "인증에 성공하였습니다.", mapOf("token" to jwtTokenProvider.createChangePasswordToken(user)))
            }
            throw WrongCodeException() //인증코드가 틀림!
        }
        throw UserNotFoundException() //해당되는 계정이 존재하지 않음
    }

    @RequestMapping(path = ["/password"], method = [RequestMethod.PATCH])
    fun changePassword(@RequestBody body: Map<String, String>): CommonResult {
        val password = body["password"]
        val vPassword = body["vPassword"]
        val authentication: Authentication = SecurityContextHolder.getContext().authentication

        //값이 유효한지 검사
        if (password.isNullOrEmpty() || vPassword.isNullOrEmpty()) {
            throw EmptyDataException()
        }
        Log.debug(tag, password)
        Log.debug(tag, vPassword)

        userRepository.findByEmail(authentication.name)?.let { user ->
            if(password != vPassword) {
                Log.debug(tag, "패스워드가 맞지 않음..")
                throw WrongPasswordException() // 패스워드가 맞지 않음!
            }

            user.password = passwordEncoder.encode(password)
            user.setRole(Collections.singletonList("ROLE_USER"))
            user.credentialsActivation()
            user.setTokenCode("")
            userRepository.save(user)
            return responseService.getSuccessResult(2023, "패스워드 변경 성공!", "패스워드 변경에 성공하였습니다.")
        }
        throw UserNotFoundException()
    }

    //코드를 생성함
    fun createCode(): Long {
        return Date().time
    }

    fun createTokenCode(): String {
        return Date().time.toString().reversed().substring(0, 8)
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