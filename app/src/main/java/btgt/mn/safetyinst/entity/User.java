package btgt.mn.safetyinst.entity;

/**
 * Created by turtuvshin on 9/28/17.
 */

public class User {
    String id;
    String name;
    String imei;
    String email;
    String password;

    public User(String id, String name, String imei, String email, String password) {
        this.id = id;
        this.name = name;
        this.imei = imei;
        this.email = email;
        this.password = password;
    }

    public User(String name, String imei, String email, String password) {
        this.name = name;
        this.imei = imei;
        this.email = email;
        this.password = password;
    }

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
