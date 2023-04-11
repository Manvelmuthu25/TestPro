package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 8/28/2017.
 */

public class StationList {

    @SerializedName("station_id")
    private String station_id;
    @SerializedName("station_code")
    private String station_code;
    @SerializedName("station_longName")
    private String station_longName;
    @SerializedName("station_latitude")
    private String station_latitude;
    @SerializedName("station_longitude")
    private String station_longitude;
    @SerializedName("station_lineColour")
    private String station_lineColour;
    @SerializedName("distance")
    private String distance;
    @SerializedName("station_type")
    private String station_type;
    @SerializedName("station_thumbimage")
    private String station_thumbimage;

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getStation_code() {
        return station_code;
    }

    public void setStation_code(String station_code) {
        this.station_code = station_code;
    }

    public String getStation_longName() {
        return station_longName;
    }

    public void setStation_longName(String station_longName) {
        this.station_longName = station_longName;
    }

    public String getStation_latitude() {
        return station_latitude;
    }

    public void setStation_latitude(String station_latitude) {
        this.station_latitude = station_latitude;
    }

    public String getStation_longitude() {
        return station_longitude;
    }

    public void setStation_longitude(String station_longitude) {
        this.station_longitude = station_longitude;
    }

    public String getStation_lineColour() {
        return station_lineColour;
    }

    public void setStation_lineColour(String station_lineColour) {
        this.station_lineColour = station_lineColour;
    }

    public String getStation_type() {
        return station_type;
    }

    public void setStation_type(String station_type) {
        this.station_type = station_type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStation_thumbimage() {
        return station_thumbimage;
    }

    public void setStation_thumbimage(String station_thumbimage) {
        this.station_thumbimage = station_thumbimage;
    }
}
