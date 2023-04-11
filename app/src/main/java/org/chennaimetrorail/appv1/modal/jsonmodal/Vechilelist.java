package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vechilelist {

    @SerializedName("vehiclename")
    private String vehiclename;
    @SerializedName("AvailableSlot")
    private String AvailableSlot;
 /*   @SerializedName("AvailableSlotStatus")
    private String AvailableSlotStatus;
*/
    @SerializedName("TotalSlot")
    private String TotalSlot;
    @SerializedName("ColourCode")
    private String ColourCode;
    @SerializedName("parkingtariff")
    private List<Parkingtariff> parkingtariff;


    @SerializedName("parkingZone")
    private  List<parkingZone>parkingZone;


    public List<parkingZone> getParkingZone() {
        return parkingZone;
    }

    public void setParkingZone(List<parkingZone> parkingZone) {
        this.parkingZone = parkingZone;
    }

    public List<Parkingtariff> getParkingtariff() {
        return parkingtariff;
    }

    public void setAvailableSlot(String availableSlot) {
        AvailableSlot = availableSlot;
    }

    public String getAvailableSlot() {
        return AvailableSlot;
    }

    public void setParkingtariff(List<Parkingtariff> parkingtariff) {
        this.parkingtariff = parkingtariff;
    }

    public String getColourCode() {
        return ColourCode;
    }

    public void setColourCode(String colourCode) {
        ColourCode = colourCode;
    }

  /*  public String getAvailableSlotStatus() {
        return AvailableSlotStatus;
    }

    public void setAvailableSlotStatus(String availableSlotStatus) {
        AvailableSlotStatus = availableSlotStatus;
    }
*/
    public String getVehiclename() {
        return vehiclename;
    }

    public void setVehiclename(String vehiclename) {
        this.vehiclename = vehiclename;
    }

    public String getTotalSlot() {
        return TotalSlot;
    }

    public void setTotalSlot(String totalSlot) {
        TotalSlot = totalSlot;
    }
}
