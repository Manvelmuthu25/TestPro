package org.chennaimetrorail.appv1.modal;

import com.google.gson.annotations.SerializedName;

public class CheckdirectionResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("reason")
    private String reason;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
