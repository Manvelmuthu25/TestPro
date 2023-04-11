package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 8/28/2017.
 */

public class NeaerstStationList {

    @SerializedName("status")
    private String status;
    @SerializedName("station_list")
    private List<StationList> station_list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StationList> getStation_list() {
        return station_list;
    }

    public void setStation_list(List<StationList> station_list) {
        this.station_list = station_list;
    }
}
