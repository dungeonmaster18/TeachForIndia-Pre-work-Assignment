package com.example.umesh.teachforindia;

/**
 * Created by umesh on 15-Apr-17.
 */

public class Opportunity {

    private String opportunity_title,opportunity_description,url,email,end_date,opportunity_location,start_date;

    public Opportunity() {

    }

    public Opportunity(String opportunity_title, String opportunity_description, String url, String email, String end_date, String opportunity_location, String start_date) {
        this.opportunity_title = opportunity_title;
        this.opportunity_description = opportunity_description;
        this.url = url;
        this.email = email;
        this.end_date = end_date;
        this.opportunity_location = opportunity_location;
        this.start_date = start_date;
    }

    public String getOpportunity_title() {
        return opportunity_title;
    }

    public void setOpportunity_title(String opportunity_title) {
        this.opportunity_title = opportunity_title;
    }

    public String getOpportunity_description() {
        return opportunity_description;
    }

    public void setOpportunity_description(String opportunity_description) {
        this.opportunity_description = opportunity_description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getOpportunity_location() {
        return opportunity_location;
    }

    public void setOpportunity_location(String opportunity_location) {
        this.opportunity_location = opportunity_location;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
