package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 2/19/2018.
 */

public class PaymentStatusModal
{

    @SerializedName("status")
    private String status;
    @SerializedName("strmsg")
    private String strmsg;
    @SerializedName("tokendetails")
    private String tokendetails;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStrmsg() {
        return strmsg;
    }

    public void setStrmsg(String strmsg) {
        this.strmsg = strmsg;
    }

    public String getTokendetails() {
        return tokendetails;
    }

    public void setTokendetails(String tokendetails) {
        this.tokendetails = tokendetails;
    }
}
