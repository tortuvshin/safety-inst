package mn.btgt.safetyinst.entity;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class Settings {
    private String companyName;
    private String departmentName;
    private String image;
    private String imei;
    private String androidId;
    private String sNoteData;

    public Settings() {

    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getsNoteData() {
        return sNoteData;
    }

    public void setsNoteData(String sNoteData) {
        this.sNoteData = sNoteData;
    }
}
