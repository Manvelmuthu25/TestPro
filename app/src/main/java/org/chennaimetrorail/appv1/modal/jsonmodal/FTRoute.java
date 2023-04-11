package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FTRoute {

    @SerializedName("linecolor")
    private String linecolor;
    @SerializedName("stationname")
    private String stationname;


    public void setLinecolor(String linecolor) {
        this.linecolor = linecolor;
    }

    public String getLinecolor() {
        return linecolor;
    }
    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getStationname() {
        return stationname;
    }


}
