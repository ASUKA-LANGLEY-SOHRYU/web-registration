package com.prosvirnin.webregistration.model.account;

import lombok.Getter;

@Getter
public class ActivationResponse {
    private String status;

    private ActivationResponse(){}

    public static ActivationResponse ok(){
        ActivationResponse activationResponse = new ActivationResponse();
        activationResponse.status = "OK!";
        return activationResponse;
    }

    public static ActivationResponse wrongCode(){
        ActivationResponse activationResponse = new ActivationResponse();
        activationResponse.status = "WRONG CODE!";
        return activationResponse;
    }
}
