package org.chennaimetrorail.appv1.modal.book_a_cab;

import com.google.gson.annotations.SerializedName;

import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.LoginDetails;

import java.util.List;

public class Bookcab_modals {
    @SerializedName("status")
    private String status;
    @SerializedName("securitycode")
    private String securitycode;
    @SerializedName("login")
    private LoginbookcabDetails LoginbookcabDetails;

    @SerializedName("jwttoken")
    private String jwttoken;

    @SerializedName("url")
    private String url;

    @SerializedName("QRjwttoken")
    private  String QRjwttoken;

    @SerializedName("QRurl")
    private  String QRurl;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecuritycode() {
        return securitycode;
    }

    public void setSecuritycode(String securitycode) {
        this.securitycode = securitycode;
    }

    public LoginbookcabDetails getLoginbookcabDetails() {
        return LoginbookcabDetails;
    }

    public void setLoginbookcabDetails(LoginbookcabDetails loginbookcabDetails) {
        LoginbookcabDetails = loginbookcabDetails;
    }

    public String getJwttoken() {
        return jwttoken;
    }

    public void setJwttoken(String jwttoken) {
        this.jwttoken = jwttoken;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getQRjwttoken() {
        return QRjwttoken;
    }

    public void setQRjwttoken(String QRjwttoken) {
        this.QRjwttoken = QRjwttoken;
    }

    public String getQRurl() {
        return QRurl;
    }

    public void setQRurl(String QRurl) {
        this.QRurl = QRurl;
    }
}
