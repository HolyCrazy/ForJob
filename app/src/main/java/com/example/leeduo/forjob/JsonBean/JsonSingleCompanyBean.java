package com.example.leeduo.forjob.JsonBean;

/**
 * Created by LeeDuo on 2019/2/9.
 */

public class JsonSingleCompanyBean {
    private String code;
    private String message;
    private JsonSingleCompanyContent content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonSingleCompanyContent getContent() {
        return content;
    }

    public void setContent(JsonSingleCompanyContent content) {
        this.content = content;
    }
}
