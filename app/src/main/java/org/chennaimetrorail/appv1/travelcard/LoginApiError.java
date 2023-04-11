package org.chennaimetrorail.appv1.travelcard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginApiError {

    @SerializedName("status")
    private String status="";

    @SerializedName("login")
    private String login="";

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
