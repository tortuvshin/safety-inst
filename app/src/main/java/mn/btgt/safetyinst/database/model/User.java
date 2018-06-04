package mn.btgt.safetyinst.database.model;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */


public class User {

    public static final String TABLE_USERS      = "users";
    public static final String USER_ID          = "id";
    public static final String USER_NAME        = "name";
    public static final String USER_POSITION    = "position";
    public static final String USER_PHONE       = "phone";
    public static final String USER_IMEI        = "imei";
    public static final String USER_EMAIL       = "email";
    public static final String USER_PASS        = "pass";
    public static final String USER_AVATAR      = "profile";
    public static final String USER_LAST_SIGNED = "lastSigned";

    public static final int USER_ID_INDEX       = 0;
    public static final int USER_NAME_INDEX     = 1;
    public static final int USER_POSITION_INDEX = 2;
    public static final int USER_PHONE_INDEX    = 3;
    public static final int USER_IMEI_INDEX     = 4;
    public static final int USER_EMAIL_INDEX    = 5;
    public static final int USER_PASS_INDEX     = 6;
    public static final int USER_AVATAR_INDEX   = 7;
    public static final int USER_LASTS_INDEX    = 8;

    private String id;
    private String name;
    private String position;
    private int phone;
    private String imei;
    private String email;
    private String password;
    private String avatar;
    private String lastSigned;

    public User() {

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
