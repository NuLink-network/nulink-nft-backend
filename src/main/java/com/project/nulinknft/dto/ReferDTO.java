package com.project.nulinknft.dto;

public class ReferDTO extends BaseDTO{

    private String time;

    private String level;

    private String referredUser;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getReferredUser() {
        return referredUser;
    }

    public void setReferredUser(String referredUser) {
        this.referredUser = referredUser;
    }
}
