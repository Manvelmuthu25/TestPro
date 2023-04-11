package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 1/30/2018.
 */

public class Login {

    @SerializedName("status")
    private String status;
    @SerializedName("securitycode")
    private String securitycode;
    @SerializedName("login")
    private LoginDetails loginDetails;

    @SerializedName("QRjwttoken")
    private  String QRjwttoken;
    @SerializedName("QRurl")
    private  String QRurl;
    @SerializedName("AvailDiscountedRide")
    private String AvailDiscountedRide;
    @SerializedName("AvailFreeRide")
    private  String  AvailFreeRide;




    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoginDetails getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }

    public String getSecuritycode() {
        return securitycode;
    }

    public void setSecuritycode(String securitycode) {
        this.securitycode = securitycode;
    }

    public String getQRurl() {
        return QRurl;
    }

    public void setQRjwttoken(String QRjwttoken) {
        this.QRjwttoken = QRjwttoken;
    }

    public String getQRjwttoken() {
        return QRjwttoken;
    }

    public void setQRurl(String QRurl) {
        this.QRurl = QRurl;
    }

    public String getAvailDiscountedRide() {
        return AvailDiscountedRide;
    }

    public void setAvailDiscountedRide(String availDiscountedRide) {
        AvailDiscountedRide = availDiscountedRide;
    }

    public String getAvailFreeRide() {
        return AvailFreeRide;
    }

    public void setAvailFreeRide(String availFreeRide) {
        AvailFreeRide = availFreeRide;
    }
}



