package org.chennaimetrorail.appv1.modal;

public class ParkingData {

    String text;
    Integer imageId;
    public ParkingData(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }
}
