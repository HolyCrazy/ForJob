package com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean;

/**
 * Created by LeeDuo on 2019/3/15.
 */

public class AnalyzerSalaryCompanyBean {
    private String key;
    private int doc_count;
    private AnalyzerPriceBean avg_price;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDoc_count() {
        return doc_count;
    }

    public void setDoc_count(int doc_count) {
        this.doc_count = doc_count;
    }

    public AnalyzerPriceBean getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(AnalyzerPriceBean avg_price) {
        this.avg_price = avg_price;
    }
}
