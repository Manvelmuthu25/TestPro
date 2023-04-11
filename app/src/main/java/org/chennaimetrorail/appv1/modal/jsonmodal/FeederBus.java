package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

public class FeederBus {

    @SerializedName("RouteNo")
    private String RouteNo;

    @SerializedName("From")
    private String From;

    @SerializedName("To")
    private String To;

    @SerializedName("Via")
    private String Via;

    @SerializedName("NumberofServices")
    private String NumberofServices;

    public String getRouteNo() {
        return RouteNo;
    }

    public void setRouteNo(String routeNo) {
        RouteNo = routeNo;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getVia() {
        return Via;
    }

    public void setVia(String via) {
        Via = via;
    }

    public String getNumberofServices() {
        return NumberofServices;
    }

    public void setNumberofServices(String numberofServices) {
        NumberofServices = numberofServices;
    }
}
