package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 8/29/2017.
 */

public class NeaerstDetails {

    @SerializedName("status")
    private String status;
  /*  @SerializedName("results")
    private List<StationList> results;*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
