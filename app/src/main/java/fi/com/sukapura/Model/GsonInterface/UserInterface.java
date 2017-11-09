package fi.com.sukapura.Model.GsonInterface;

import com.google.gson.annotations.SerializedName;

/**
 * Created by _fi on 10/16/2017.
 */

public class UserInterface {
    @SerializedName("success")
    private String success;

    @SerializedName("user")
    private UserDetail userDetail;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public class UserDetail{
        @SerializedName("id")
        private String id;
        @SerializedName("msisdn")
        private String msisdn;
        @SerializedName("access")
        private String access;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }
    }
}
