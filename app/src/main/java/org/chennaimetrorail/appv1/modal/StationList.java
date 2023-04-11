package org.chennaimetrorail.appv1.modal;

/**
 * Created by 102525 on 7/11/2017.
 */

public class StationList {
    private String Station_Id, Station_Code,
            Station_ShortName, Station_LongName,
            Station_Latitude, Station_Longitude,
            Station_Priority, Station_LineColour,
            Gates_Directions, Station_Contacts,
            Platform_Info, Station_Type;
    public StationList(){

    }

    public String getStation_Id() {
        return Station_Id;
    }

    public String getStation_Code() {
        return Station_Code;
    }

    public String getStation_ShortName() {
        return Station_ShortName;
    }

    public String getStation_LongName() {
        return Station_LongName;
    }

    public String getStation_Latitude() {
        return Station_Latitude;
    }

    public String getStation_Longitude() {
        return Station_Longitude;
    }

    public String getStation_Priority() {
        return Station_Priority;
    }

    public String getStation_LineColour() {
        return Station_LineColour;
    }




    public String getGates_Directions() {
        return Gates_Directions;
    }

    public String getStation_Contacts() {
        return Station_Contacts;
    }

    public String getPlatform_Info() {
        return Platform_Info;
    }

    public String getStation_Type() {
        return Station_Type;
    }


    public void setStation_Id(String station_Id) {
        Station_Id = station_Id;
    }

    public void setGates_Directions(String gates_Directions) {
        Gates_Directions = gates_Directions;
    }



    public void setPlatform_Info(String platform_Info) {
        Platform_Info = platform_Info;
    }

    public void setStation_Code(String station_Code) {
        Station_Code = station_Code;
    }

    public void setStation_Contacts(String station_Contacts) {
        Station_Contacts = station_Contacts;
    }


    public void setStation_Latitude(String station_Latitude) {
        Station_Latitude = station_Latitude;
    }

    public void setStation_LineColour(String station_LineColour) {
        Station_LineColour = station_LineColour;
    }

    public void setStation_Longitude(String station_Longitude) {
        Station_Longitude = station_Longitude;
    }

    public void setStation_LongName(String station_LongName) {
        Station_LongName = station_LongName;
    }

    public void setStation_Priority(String station_Priority) {
        Station_Priority = station_Priority;
    }

    public void setStation_ShortName(String station_ShortName) {
        Station_ShortName = station_ShortName;
    }


    public void setStation_Type(String station_Type) {
        Station_Type = station_Type;
    }


}