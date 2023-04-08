const REGEXP = {
  NICKNAME: /^[가-힣a-zA-Z0-9][가-힣a-zA-Z0-9]{2,9}$/,
  USERNAME: /^[a-z0-9][a-z0-9]{7,15}$/,
  PASSWORD: /^[A-Za-z0-9`\-=\\[\];',./~!@#$%\\^&*()_+|{}:"<>?]{8,16}$/,
};

export { REGEXP };
