package com.project.nulinknft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "referrer")
public class Referrer extends BaseEntity{

    @Column(name = "tx_hash", unique = true)
    private String txHash;

    @Column(name = "referrer_address")
    private String referrerAddress;

    @Column(name = "referred_address")
    private String referredAddress;

    @Column(name = "activated")
    private Boolean activated;

}
