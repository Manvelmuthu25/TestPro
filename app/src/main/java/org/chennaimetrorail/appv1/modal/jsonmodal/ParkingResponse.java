package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParkingResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("vehicletype")
    private List<VehicleType> vehicletype;
    @SerializedName("vechilelist")
    private List<Vechilelist> vechilelist;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Vechilelist> getVechilelist() {
        return vechilelist;
    }

    public void setVechilelist(List<Vechilelist> vechilelist) {
        this.vechilelist = vechilelist;
    }

    public List<VehicleType> getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(List<VehicleType> vehicletype) {
        this.vehicletype = vehicletype;
    }
}
