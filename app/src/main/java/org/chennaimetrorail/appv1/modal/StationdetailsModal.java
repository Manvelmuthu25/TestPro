package org.chennaimetrorail.appv1.modal;

/**
 * Created by 102525 on 3/19/2018.
 */

public class StationdetailsModal {

    private String facility_name;
    private String facility_imag;


    public StationdetailsModal(String facility_name,String facility_imag){

        this.facility_name = facility_name;
        this.facility_imag = facility_imag;
    }

    public String getFacility_imag() {
        return facility_imag;
    }

    public void setFacility_imag(String facility_imag) {
        this.facility_imag = facility_imag;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }
}
