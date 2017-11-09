package fi.com.sukapura.Model;

/**
 * Created by _fi on 11/1/2017.
 */

public class Outlet {
    String nama;
    String id;
    String alamat;
    String tdc;

    public Outlet(String nama, String id, String alamat, String tdc){
        this.nama = nama;
        this.id = id;
        this.alamat = alamat;
        this.tdc = tdc;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
