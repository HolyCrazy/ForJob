package com.example.leeduo.forjob.JsonBean;

/**
 * Created by LeeDuo on 2019/2/10.
 */

public class JsonPositionBean {
    private String code;
    private String message;
    private JsonPositionsContent content;

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

    public JsonPositionsContent getContent() {
        return content;
    }

    public void setContent(JsonPositionsContent content) {
        this.content = content;
    }
}
