package fi.com.sukapura.Model.GsonInterface;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by _fi on 11/7/2017.
 */

public class ListMsisdnKonsinyasi {
    @SerializedName("response")
    String response;

    @SerializedName("outlet_id")
    String outlet_id;

    @SerializedName("list_msisdn")
    List<ListMsisdn> list_msisdn;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public List<ListMsisdn> getList_msisdn() {
        return list_msisdn;
    }

    public void setList_msisdn(List<ListMsisdn> list_msisdn) {
        this.list_msisdn = list_msisdn;
    }

    public class ListMsisdn{
        @SerializedName("msisdn")
        String msisdn;

        boolean checked;

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
