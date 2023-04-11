package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 9/22/2017.
 */

public class DeviceDetails {

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    }
