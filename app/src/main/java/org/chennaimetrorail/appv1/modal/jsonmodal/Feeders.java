package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Feeders {

    @SerializedName("status")
    private String status;
    @SerializedName("Station")
    private String Station;
    @SerializedName("Bus")
    private List<FeederBus> Bus;
    @SerializedName("Auto")
    private List<FeederAuto> Auto;
    @SerializedName("Car")
    private List<FeederCar> Car;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FeederAuto> getAuto() {
        return Auto;
    }

    public List<FeederBus> getBus() {
        return Bus;
    }

    public List<FeederCar> getCar() {
        return Car;
    }

    public String getStation() {
        return Station;
    }

    public void setAuto(List<FeederAuto> auto) {
        Auto = auto;
    }

    public void setBus(List<FeederBus> bus) {
        Bus = bus;
    }

    public void setCar(List<FeederCar> car) {
        Car = car;
    }

    public void setStation(String station) {
        Station = station;
    }
}
