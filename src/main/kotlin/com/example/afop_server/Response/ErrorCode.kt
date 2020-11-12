package com.example.afop_server.Response

/**
 * 에러코드를 정의해둔 클래스
 * 400 Bad Request - 클라이언트의 요청이 유효하지 않아 더 이상 작업을 진행하지 않는 경우
 * 401 Unauthorized - 클라이언트가 권한이 없기 때문에 작업을 진행할 수 없는 경우
 * 403 Forbidden - 클라이언트가 권한이 없기 때문에 작업을 진행할 수 없는 경우
 * 404 Not Found - 클라이언트가 요청한 자원이 존재하지 않다.
 * 405 Method Not Allowed - 클라이언트의 요청이 허용되지 않는 메소드인 경우
 * 409 Conflict - 클라이언트의 요청이 서버의 상태와 충돌이 발생한 경우
 * 429 Too Many Requests - 클라이언트가 일정 시간 동안 너무 많은 요청을 보낸 경우
 */

enum class ErrorCode(val status: Int, val code: String, val message: String) {
    //Common
    NOT_DEFINE_ERROR(400, "C001", ""),
    EMPTY_DATA(400, "C002", ""),
    ACCESS_DENIED(401, "C003", "잘못된 접근입니다."),

    //Auth
    ALREADY_USER_EMAIL(400, "", "이미 존재하는 이메일입니다."),
    ALREADY_USER_NICKNAME(400, "", "이미 존재하는 닉네임입니다."),
    EXPIRED_USER_PASSWORD(403, "", "패스워드가 만료되었습니다."),
    NOT_VERIFY_EMAIL(403, "", "이메일 인증 절차가 끝나지 않았습니다."),
    REGISTERING_USER(409, "", "이미 회원가입 중인 계정입니다."),
    EXPIRED_VERIFY_EMAIL(400, "", "만료된 링크입니다!"),
    FAILED_LOGIN(400, "", "로그인에 실패했습니다."),
    WRONG_PASSWORD(400, "", "기준 패스워드와 패스워드 확인값이 일치하지 않습니다.")
}