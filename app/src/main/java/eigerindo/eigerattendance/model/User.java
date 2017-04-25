package eigerindo.eigerattendance.model;

/**
 * Created by ahmad on 4/25/2017.
 */

public class User {
    private String nik;
    private String password;
    private String site_id;

    public String getNik(){
        return nik;
    }
    public void setNik(String nik){
        this.nik = nik;
    }

    public String getPassword(){
        return nik;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getSite_id(){
        return site_id;
    }
    public void setSite_id(String site_id){
        this.site_id = site_id;
    }
}
