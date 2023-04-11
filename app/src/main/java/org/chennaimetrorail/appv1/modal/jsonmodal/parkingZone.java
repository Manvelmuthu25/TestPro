package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

public class parkingZone {

    @SerializedName("ParkingZoneName")
    private String ParkingZoneName;

    @SerializedName("AvailableCount")
    private String AvailableCount;

    @SerializedName("TotalCount")
    private String TotalCount;

    @SerializedName("vehicleID")
    private String vehicleID;


    public String getParkingZoneName() {
        return ParkingZoneName;
    }

    public void setParkingZoneName(String parkingZoneName) {
        ParkingZoneName = parkingZoneName;
    }

    public String getAvailableCount() {
        return AvailableCount;
    }

    public void setAvailableCount(String availableCount) {
        AvailableCount = availableCount;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }
}