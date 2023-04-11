package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

public class Parkingtariff {

    @SerializedName("parkingtime")
    private String parkingtime;
    @SerializedName("parkingamount")
    private String parkingamount;

    public String getParkingamount() {
        return parkingamount;
    }

    public void setParkingamount(String parkingamount) {
        this.parkingamount = parkingamount;
    }

    public String getParkingtime() {
        return parkingtime;
    }

    public void setParkingtime(String parkingtime) {
        this.parkingtime = parkingtime;
    }
}

