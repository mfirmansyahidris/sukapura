package fi.com.sukapura.Model.GsonInterface;

import com.google.gson.annotations.SerializedName;

/**
 * Created by _fi on 10/31/2017.
 */

public class ResponseHandler {
    @SerializedName("response")
    private String response;

    @SerializedName("success")
    private String success;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
