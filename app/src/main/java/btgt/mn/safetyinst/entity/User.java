package btgt.mn.safetyinst.entity;

/**
 * Created by turtuvshin on 9/28/17.
 */

public class User {
    String id;
    String name;
    String position;
    int phone;
    String imei;
    String email;
    String password;
    String avatar;
    String lastSigned;

    public User(String id, String name, String position, int phone, String imei, String email, String password, String avatar, String lastSigned) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.imei = imei;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.lastSigned = lastSigned;
    }

    public User(String name, String position, int phone, String imei, String email, String password, String avatar, String lastSigned) {
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.imei = imei;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.lastSigned = lastSigned;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastSigned() {
        return lastSigned;
    }

    public void setLastSigned(String lastSigned) {
        this.lastSigned = lastSigned;
    }
}
