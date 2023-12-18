package com.project.nulinknft.dto;


public class DecryptingDTO extends BaseDTO{

    private int tokenId;

    private String preUser;

    private String metamaskUser;

    private String content;

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public String getPreUser() {
        return preUser;
    }

    public void setPreUser(String preUser) {
        this.preUser = preUser;
    }

    public String getMetamaskUser() {
        return metamaskUser;
    }

    public void setMetamaskUser(String metamaskUser) {
        this.metamaskUser = metamaskUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
