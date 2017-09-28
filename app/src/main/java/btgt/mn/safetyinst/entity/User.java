package btgt.mn.safetyinst.entity;

/**
 * Created by turtuvshin on 9/28/17.
 */

public class User {
    String id;
    String name;
    String imei;
    String password;
    String role;

    public User(String id, String name, String imei, String password, String role) {
        this.id = id;
        this.name = name;
        this.imei = imei;
        this.password = password;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
