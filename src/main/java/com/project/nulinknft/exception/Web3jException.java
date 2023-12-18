package com.project.nulinknft.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class Web3jException extends RuntimeException {

    private Integer status = INTERNAL_SERVER_ERROR.value();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Web3jException(String msg){
        super(msg);
    }

    public Web3jException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
