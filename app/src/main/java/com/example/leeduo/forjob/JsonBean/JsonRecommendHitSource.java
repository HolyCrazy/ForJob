package com.example.leeduo.forjob.JsonBean;

/**
 * Created by LeeDuo on 2019/3/5.
 */

public class JsonRecommendHitSource {
    private String degree;
    private String create_time;
    private String experience;
    private String title;
    private String city;
    private int max_salary;
    private String company_id;
    private String job_id;
    private int min_salary;
    private String advantage;
    private JsonSingleCompanyContent company;
    private JsonCompanyLegalPerson person;
    private String description;

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getMax_salary() {
        return max_salary;
    }

    public void setMax_salary(int max_salary) {
        this.max_salary = max_salary;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public int getMin_salary() {
        return min_salary;
    }

    public void setMin_salary(int min_salary) {
        this.min_salary = min_salary;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public JsonSingleCompanyContent getCompany() {
        return company;
    }

    public void setCompany(JsonSingleCompanyContent company) {
        this.company = company;
    }

    public JsonCompanyLegalPerson getPerson() {
        return person;
    }

    public void setPerson(JsonCompanyLegalPerson person) {
        this.person = person;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
