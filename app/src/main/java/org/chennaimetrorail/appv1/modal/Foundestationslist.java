package org.chennaimetrorail.appv1.modal;

public class Foundestationslist {
    int id;
    String Station__Id;

    public Foundestationslist(){

    }
    // Empty constructor
    public Foundestationslist(String Station__Id){
        this.Station__Id=Station__Id;

    }
    public String getStation_Id() {
        return Station__Id;
    }

    public void setStation_Id(String station_Id) {
        Station__Id = station_Id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
