package com.object.haru.Chat;

import com.kakao.sdk.user.model.User;

public class UserAccountDTO {

      private String email,name,idToken,token;
      private Long kakaoid;

    // Firebase Uid (고유 토큰 정보)


    public UserAccountDTO(){

    }

    public UserAccountDTO(String idToken, String email, String name, Long kakaoid) {
        this.idToken = idToken;
        this.email = email;
        this.name = name;
        this.kakaoid = kakaoid;
    }

    public Long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(Long kakaoid) {
        this.kakaoid = kakaoid;
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

    public String getToken() { return token;    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
