package org.chennaimetrorail.appv1.modal.book_a_cab;

import com.google.gson.annotations.SerializedName;

public class freerideavailabiltydetails {
    @SerializedName("ErrorCaption")
    private String ErrorCaption ;
    @SerializedName("ErrorMsg")
    private String ErrorMsg;
    @SerializedName("FreeRideURL")
    private  String FreeRideURL;
    @SerializedName("status")
    private String status;

    public String getErrorCaption() {
        return ErrorCaption;
    }

    public void setErrorCaption(String errorCaption) {
        ErrorCaption = errorCaption;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public String getFreeRideURL() {
        return FreeRideURL;
    }

    public void setFreeRideURL(String freeRideURL) {
        FreeRideURL = freeRideURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
