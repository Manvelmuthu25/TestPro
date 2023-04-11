package org.chennaimetrorail.appv1.modal;

public class LocalIntermediatelist {

    int id;
    String Station_Id;
    String Station_ShortName;
    String Station_LineColour;

    // Empty constructor
    public LocalIntermediatelist(){

    }
    // constructor
    public LocalIntermediatelist(String Station_Id, String Station_ShortName,String Station_LineColour){
        this.Station_Id = Station_Id;
        this.Station_ShortName = Station_ShortName;
        this.Station_LineColour = Station_LineColour;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStation_Id() {
        return Station_Id;
    }

    public void setStation_Id(String station_Id) {
        Station_Id = station_Id;
    }

    public String getStation_LineColour() {
        return Station_LineColour;
    }

    public void setStation_LineColour(String station_LineColour) {
        Station_LineColour = station_LineColour;
    }

    public String getStation_ShortName() {
        return Station_ShortName;
    }

    public void setStation_ShortName(String station_ShortName) {
        Station_ShortName = station_ShortName;
    }
}
