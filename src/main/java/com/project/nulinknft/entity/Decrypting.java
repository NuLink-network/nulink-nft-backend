package com.project.nulinknft.entity;

import javax.persistence.*;

@Entity
@Table(name = "decrypting")
public class Decrypting extends BaseEntity{

    @Column(name = "token_id")
    private int tokenId;

    @Column(name = "pre_user", nullable = false)
    private String preUser;

    @Column(name = "metamask_user", nullable = false)
    private String metamaskUser;

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

}
