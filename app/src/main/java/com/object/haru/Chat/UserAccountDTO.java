package com.object.haru.Chat;

public class UserAccountDTO {
    private String emailId;     // 이메일 아이디
    private String passWord;    // 비밀번호
    private String name;        // 이름
    private String idToken;     // Firebase Uid (고유 토큰 정보)
    private String year;
    private String month;
    private String date;
    private String sex;
    private String introduce;

    public UserAccountDTO() {
    }     // 파이어베이스에서는 빈 생성자를 만들어야함

    public UserAccountDTO(String name, String myself)  {
        this.name = name;
        this.introduce = myself;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
