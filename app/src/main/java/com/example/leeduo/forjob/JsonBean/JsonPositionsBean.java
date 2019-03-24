package com.example.leeduo.forjob.JsonBean;

/**
 * Created by LeeDuo on 2019/2/9.
 */

public class JsonPositionsBean {
    private int total;
    private int code;
    private int pageSize;
    private String message;
    private JsonPositionsContent[] content;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonPositionsContent[] getContent() {
        return content;
    }

    public void setContent(JsonPositionsContent[] content) {
        this.content = content;
    }
}
