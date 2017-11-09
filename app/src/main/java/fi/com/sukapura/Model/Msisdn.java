package fi.com.sukapura.Model;

/**
 * Created by _fi on 10/18/2017.
 */

public class Msisdn {
    String msisdn;
    boolean valid;
    boolean checked;

    public Msisdn(String msisdn, boolean valid, boolean checked){
        this.msisdn = msisdn;
        this.valid = valid;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid= valid;
    }
}
