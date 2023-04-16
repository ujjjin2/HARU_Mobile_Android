package com.object.haru.Chat;

import com.kakao.sdk.user.model.User;

public class UserAccountDTO {

      private String email;     // 이메일 아이디
      private String name;        // 이름
      private String idToken;     // Firebase Uid (고유 토큰 정보)
    private Long kakaoid;

    public Long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(Long kakaoid) {
        this.kakaoid = kakaoid;
    }

    // Firebase Uid (고유 토큰 정보)


    public UserAccountDTO(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailId) {
        this.email = emailId;
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
