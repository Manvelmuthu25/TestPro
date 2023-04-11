package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 2/13/2018.
 */

public class ChangepassowordModal {
    @SerializedName("status")
    private String status;
    @SerializedName("msg")
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
