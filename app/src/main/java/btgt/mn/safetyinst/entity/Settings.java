package btgt.mn.safetyinst.entity;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class Settings {
    String companyName;
    String departmentName;
    String imei;
    String androidId;
    String sNoteData;

    public Settings(String companyName, String departmentName, String imei, String androidId, String sNoteData) {
        this.companyName = companyName;
        this.departmentName = departmentName;
        this.imei = imei;
        this.androidId = androidId;
        this.sNoteData = sNoteData;
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
