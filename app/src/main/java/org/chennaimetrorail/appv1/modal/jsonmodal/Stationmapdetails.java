package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

public class Stationmapdetails {

    @SerializedName("mapname")
    private String mapname;
    @SerializedName("mapimage")
    private String mapimage;

    public String getMapimage() {
        return mapimage;
    }

    public void setMapimage(String mapimage) {
        this.mapimage = mapimage;
    }

    public String getMapname() {
        return mapname;
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }
}
