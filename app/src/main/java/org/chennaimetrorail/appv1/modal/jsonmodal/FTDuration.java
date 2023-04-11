package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FTDuration {

    @SerializedName("train_time")
    private String train_time;
    @SerializedName("platform")
    private String platform;
    @SerializedName("colorcode")
    private String colorcode;
    @SerializedName("routeinfo")
    private String routeinfo;



    @SerializedName("distance_child")
    private String distance_child;
    @SerializedName("duration_child")
    private String duration_child;
    @SerializedName("inbetweenstation_child")
    private String inbetweenstation_child;

    @SerializedName("route")
    private List<FTRoute> route;





    public String getTrain_time() {
        return train_time;
    }

    public void setTrain_time(String train_time) {
        this.train_time = train_time;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public String getRouteinfo() {
        return routeinfo;
    }

    public void setRouteinfo(String routeinfo) {
        this.routeinfo = routeinfo;
    }






    public String getDistance_child() {
        return distance_child;
    }

    public void setDistance_child(String distance_child) {
        this.distance_child = distance_child;
    }
    public String getDuration_child() {
        return duration_child;
    }

    public void setDuration_child(String duration_child) {
        this.duration_child = duration_child;
    }
    public String getInbetweenstation_child() {
        return inbetweenstation_child;
    }

    public void setInbetweenstation_child(String inbetweenstation_child) {
        this.inbetweenstation_child = inbetweenstation_child;
    }














    public void setRoute(List<FTRoute> route) {
        this.route = route;
    }

    public List<FTRoute> getRoute() {
        return route;
    }

}
