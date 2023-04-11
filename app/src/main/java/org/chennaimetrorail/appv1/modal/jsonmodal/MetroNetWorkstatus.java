package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetroNetWorkstatus {

    @SerializedName("status")
    private String status;
    @SerializedName("MetroNetWorkList")
    private List<MetroNetWorkList> MetroNetWorkList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MetroNetWorkList> getMetroNetWorkList() {
        return MetroNetWorkList;
    }

    public void setMetroNetWorkList(List<MetroNetWorkList> metroNetWorkList) {
        MetroNetWorkList = metroNetWorkList;
    }
}
