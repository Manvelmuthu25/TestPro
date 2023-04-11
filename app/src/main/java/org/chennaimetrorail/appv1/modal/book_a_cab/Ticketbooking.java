package org.chennaimetrorail.appv1.modal.book_a_cab;

import com.google.gson.annotations.SerializedName;

public class Ticketbooking {

        @SerializedName("status")
        private String status;
        @SerializedName("securitycode")
        private String securitycode;
        @SerializedName("login")
        private LoginbookcabDetails LoginbookcabDetails;

        @SerializedName("jwttoken")
        private String jwttoken;

        @SerializedName("url")
        private String url;

        @SerializedName("QRjwttoken")
        private String QRjwttoken;

        @SerializedName("QRurl")
        private String QRurl;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSecuritycode() {
            return securitycode;
        }

        public void setSecuritycode(String securitycode) {
            this.securitycode = securitycode;
        }

        public LoginbookcabDetails getLoginbookcabDetails() {
            return LoginbookcabDetails;
        }

        public void setLoginbookcabDetails(LoginbookcabDetails loginbookcabDetails) {
            LoginbookcabDetails = loginbookcabDetails;
        }

        public String getQRjwttoken() {
            return QRjwttoken;
        }

    public void setQRjwttoken(String QRjwttoken) {
        this.QRjwttoken = QRjwttoken;
    }

    public void setQRurl(String QRurl) {
            this.QRurl = QRurl;

    }

    public String getQRurl() {
        return QRurl;
    }
}
