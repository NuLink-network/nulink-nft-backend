package com.project.nulinknft.dto;

import javax.validation.constraints.Size;

public class UserDTO extends BaseDTO{

    private String avatar;

    @Size(max = 20)
    private String userName;

    @Size(max = 200)
    private String website;

    @Size(max = 200)
    private String twitter;

    @Size(max = 200)
    private String instagram;

    @Size(max = 200)
    private String facebook;

    @Size(max = 1000)
    private String description;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
