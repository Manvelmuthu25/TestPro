package org.chennaimetrorail.appv1.modal;

import com.google.gson.annotations.SerializedName;

public class Googleplacesphotos {

    @SerializedName("status")
    private String status;
    @SerializedName("ImageUrl")
    private String ImageUrl;
    @SerializedName("PhotoImage")
    private String PhotoImage;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoImage() {
        return PhotoImage;
    }

    public void setPhotoImage(String photoImage) {
        PhotoImage = photoImage;
    }
}
