package com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean;

import com.example.leeduo.forjob.JsonBean.JsonUserInfoBean;

/**
 * Created by LeeDuo on 2019/3/23.
 */

public class JsonUserInformationBean {
    private int code;
    private String message;
    private JsonUserInfoBean content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonUserInfoBean getContent() {
        return content;
    }

    public void setContent(JsonUserInfoBean content) {
        this.content = content;
    }
}
