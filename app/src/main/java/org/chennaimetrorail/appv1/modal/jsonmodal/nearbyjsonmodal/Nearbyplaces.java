package org.chennaimetrorail.appv1.modal.jsonmodal.nearbyjsonmodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 102525 on 7/17/2017.
 */

public class Nearbyplaces {

        @SerializedName("html_attributions")
        @Expose
        private List<Object> htmlAttributions = new ArrayList<Object>();
        @SerializedName("next_page_token")
        @Expose
        private String nextPageToken;
        @SerializedName("results")
        @Expose
        private List<Results> results = new ArrayList<Results>();
        @SerializedName("status")
        @Expose
        private String status;

        /**
         * @return The htmlAttributions
         */
        public List<Object> getHtmlAttributions() {
            return htmlAttributions;
        }

        /**
         * @param htmlAttributions The html_attributions
         */
        public void setHtmlAttributions(List<Object> htmlAttributions) {
            this.htmlAttributions = htmlAttributions;
        }

        /**
         * @return The nextPageToken
         */
        public String getNextPageToken() {
            return nextPageToken;
        }

        /**
         * @param nextPageToken The next_page_token
         */
        public void setNextPageToken(String nextPageToken) {
            this.nextPageToken = nextPageToken;
        }

        /**
         * @return The results
         */
        public List<Results> getResults() {
            return results;
        }

        /**
         * @param results The results
         */
        public void setResults(List<Results> results) {
            this.results = results;
        }

        /**
         * @return The status
         */
        public String getStatus() {
            return status;
        }

        /**
         * @param status The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

    }
