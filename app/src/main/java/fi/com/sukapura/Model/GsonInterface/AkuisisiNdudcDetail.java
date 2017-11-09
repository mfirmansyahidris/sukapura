package fi.com.sukapura.Model.GsonInterface;

import com.google.gson.annotations.SerializedName;

/**
 * Created by _fi on 11/6/2017.
 */

public class AkuisisiNdudcDetail {
    @SerializedName("response")
    String response;

    @SerializedName("detail")
    Detail detail;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public class Detail{
        @SerializedName("id")
        String id;

        @SerializedName("name")
        String name;

        @SerializedName("cluster")
        String cluster;

        @SerializedName("valid_ndudc")
        String valid_ndudc;

        @SerializedName("valid")
        String valid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCluster() {
            return cluster;
        }

        public void setCluster(String cluster) {
            this.cluster = cluster;
        }

        public String getValid_ndudc() {
            return valid_ndudc;
        }

        public void setValid_ndudc(String valid_ndudc) {
            this.valid_ndudc = valid_ndudc;
        }

        public String getValid() {
            return valid;
        }

        public void setValid(String valid) {
            this.valid = valid;
        }
    }
}
