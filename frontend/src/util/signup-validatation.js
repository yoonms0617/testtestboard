import { CLIENT_MESSAGE, SERVER_MESSAGE } from "@/constants/message";
import { REGEXP } from "@/constants/regexp";
import { SIGNUP } from "@/api/member";

const SIGNUP_VALIDATION = {
  nickname(input, message) {
    message.nickname = REGEXP.NICKNAME.test(input.nickname) ? "" : CLIENT_MESSAGE.REGEXP_NICKNAME;
  },
  username(input, message) {
    message.username = REGEXP.USERNAME.test(input.username) ? "" : CLIENT_MESSAGE.REGEXP_USERNAME;
  },
  password(input, message, validate) {
    if (REGEXP.PASSWORD.test(input.password)) {
      message.password = CLIENT_MESSAGE.GOOD_PASSWORD;
      validate.password = true;
    } else {
      message.password = CLIENT_MESSAGE.REGEXP_PASSWORD;
      validate.password = false;
    }
  },
  confirm(input, message, validate) {
    if (input.password === input.confirm) {
      message.confirm = CLIENT_MESSAGE.GOOD_CONFIRM;
      validate.confirm = true;
    } else {
      message.confirm = CLIENT_MESSAGE.NOT_MATCH_PASSWORD;
      validate.confirm = false;
    }
  },
  checkMatchPassword(input, message, validate) {
    if (input.confirm != "") {
      if (input.password === input.confirm) {
        message.confirm = CLIENT_MESSAGE.GOOD_CONFIRM;
        validate.confirm = true;
      } else {
        message.confirm = CLIENT_MESSAGE.NOT_MATCH_PASSWORD;
        validate.confirm = false;
      }
    }
  },
  checkDuplicateNickname(nickname, message, validate) {
    if (!REGEXP.NICKNAME.test(nickname)) {
      return;
    }
    if (!validate.nickname) {
      SIGNUP.existsNicknameRequest(nickname)
        .then((res) => {
          if (res.data === "Y") {
            validate.nickname = true;
            message.nickname = CLIENT_MESSAGE.GOOD_NICKNAME;
          }
        })
        .catch((err) => {
          validate.nickname = false;
          message.nickname = SERVER_MESSAGE[err.response.data.code];
        });
    }
  },
  checkDuplicateUsername(username, message, validate) {
    if (!REGEXP.USERNAME.test(username)) {
      return;
    }
    if (!validate.username) {
      SIGNUP.existsUsernameRequest(username)
        .then((res) => {
          if (res.data === "Y") {
            validate.username = true;
            message.username = CLIENT_MESSAGE.GOOD_USERNAME;
          }
        })
        .catch((err) => {
          validate.username = false;
          message.username = SERVER_MESSAGE[err.response.data.code];
        });
    }
  },
};

export { SIGNUP_VALIDATION };
