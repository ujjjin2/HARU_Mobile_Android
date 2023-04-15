package com.object.haru.Chat;

import com.kakao.sdk.user.model.User;

public class UserAccountDTO {

      private String emailId;     // 이메일 아이디
      private String name;        // 이름
      private String idToken;     // Firebase Uid (고유 토큰 정보)


    public UserAccountDTO(){

    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
