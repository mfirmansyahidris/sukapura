package fi.com.sukapura.Model.GsonInterface;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by _fi on 11/1/2017.
 */

public class ListOutletInterface {
    @SerializedName("response")
    private String response;

    @SerializedName("outlet_list")
    private List<OutletDetail> outletDetails;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<OutletDetail> getOutletDetails() {
        return outletDetails;
    }

    public void setOutletDetails(List<OutletDetail> outletDetails) {
        this.outletDetails = outletDetails;
    }

    public class OutletDetail{
        @SerializedName("id")
        private String id;

        @SerializedName("nama")
        private String nama;

        @SerializedName("alamat")
        private String alamat;

        @SerializedName("tdc")
        private String tdc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }

        public String getTdc() {
            return tdc;
        }

        public void setTdc(String tdc) {
            this.tdc = tdc;
        }
    }
}
