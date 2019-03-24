package com.example.leeduo.forjob.JsonBean;

/**
 * Created by LeeDuo on 2019/3/23.
 */

public class JsonUserInfoBean {
    private String uid;
    private String tel;
    private int userType;
    private int isActive;
    private int isBlocked;
    private int telCaptcha;
    private String nickName;
    private String imgUrl;
    private String email;
    private String companyId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public int getTelCaptcha() {
        return telCaptcha;
    }

    public void setTelCaptcha(int telCaptcha) {
        this.telCaptcha = telCaptcha;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
