package com.project.nulinknft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "nft")
public class NFT extends BaseEntity{

    @Column(name = "tx_hash", nullable = false)
    private String txHash;

    private String owner;

    @Column(name = "token_id", unique = true)
    private int tokenId;

    private int airdropLevel;

    @Column(name = "is_decrypted", columnDefinition = " bit DEFAULT 0 ")
    private boolean isDecrypted;

    @Column(name = "content", length = 1000)
    private String content;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getAirdropLevel() {
        return airdropLevel;
    }

    public void setAirdropLevel(int airdropLevel) {
        this.airdropLevel = airdropLevel;
    }

    public boolean isDecrypted() {
        return isDecrypted;
    }

    public void setDecrypted(boolean decrypted) {
        isDecrypted = decrypted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
