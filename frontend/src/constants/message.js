const CLIENT_MESSAGE = {
  REGEXP_NICKNAME: "3 ~ 20자 사이의 한글, 영문 대/소문자, 숫자를 사용해 주세요.",
  REGEXP_USERNAME: "8 ~ 16자 사이의 영문 소문자, 숫자를 사용해 주세요.",
  REGEXP_PASSWORD: "8 ~ 16자 사이의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.",
  NOT_MATCH_PASSWORD: "비밀번호가 일치하지 않습니다.",
  GOOD_NICKNAME: "사용 가능한 닉네임입니다.",
  GOOD_USERNAME: "사용 가능한 아이디입니다.",
  GOOD_PASSWORD: "사용 가능한 비밀번호입니다.",
  GOOD_CONFIRM: "비밀번호가 일치합니다.",
};

const SERVER_MESSAGE = {
  ERRC001: "지원하지 않는 에러 유형입니다.",
  ERRC002: "지원하지 않는 HTTP 메소드를 호출했습니다.",
  ERRC003: "잘못된 입력 값입니다.",
  ERRA001: "아이디 또는 비밀번호를 잘못 입력했습니다.",
  ERRM001: "사용 중인 닉네임입니다.",
  ERRM002: "사용 중인 이메일입니다.",
  ERRM003: "회원을 찾을 수 없습니다.",
};

export { CLIENT_MESSAGE, SERVER_MESSAGE };
