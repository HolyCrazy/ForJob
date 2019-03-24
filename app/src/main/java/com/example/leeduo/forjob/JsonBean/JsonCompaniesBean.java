package com.example.leeduo.forjob.JsonBean;

/**
 * Created by LeeDuo on 2019/2/4.
 */

public class JsonCompaniesBean {
    private int total;
    private String code;
    private int pageSize;
    private String message;
    private int pn;
    private JsonCompaniesContent[] content;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
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

    public JsonCompaniesContent[] getContent() {
        return content;
    }

    public void setContent(JsonCompaniesContent[] content) {
        this.content = content;
    }
}
