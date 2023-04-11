package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102526 on 4/3/2018.
 */

public class Stationfacilities {
    @SerializedName("facilityname")
    private String facilityname;
    @SerializedName("facilityimage")
    private String facilityimage;

    public String getFacilityname() {
        return facilityname;
    }

    public void setFacilityname(String facilityname) {
        this.facilityname = facilityname;
    }

    public String getFacilityimage() {
        return facilityimage;
    }

    public void setFacilityimage(String facilityimage) {
        this.facilityimage = facilityimage;
    }
}


