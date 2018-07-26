package mn.btgt.safetyinst.database.model;

import java.util.Arrays;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */
@Deprecated
public class SignData {

    public static final String TABLE_SIGNDATAS     = "sign_data";
    public static final String SIGNDATA_ID         = "id";
    public static final String SIGNDATA_USER_ID    = "user_id";
    public static final String SIGNDATA_SNOTE_ID   = "snote_id";
    public static final String SIGNDATA_USERNAME = "user_name";
    public static final String SIGNDATA_SNOTE_NAME = "snote_name";
    public static final String SIGNDATA_VIEWDATE   = "view_date";
    public static final String SIGNDATA_SIGN_NAME  = "signature_name";
    public static final String SIGNDATA_SIGN_DATA  = "signature_data";
    public static final String SIGNDATA_PHOTO_NAME = "photo_name";
    public static final String SIGNDATA_PHOTO      = "photo";
    public static final String SIGNDATA_SENDSTATUS = "send_status";

    public static final int SIGNDATA_ID_INDEX         = 0;
    public static final int SIGNDATA_USER_ID_INDEX    = 1;
    public static final int SIGNDATA_SNOTE_ID_INDEX   = 2;
    public static final int SIGNDATA_USERNAME_INDEX   = 3;
    public static final int SIGNDATA_SNOTE_NAME_INDEX = 4;
    public static final int SIGNDATA_VIEWDATE_INDEX   = 5;
    public static final int SIGNDATA_SIGN_NAME_INDEX  = 6;
    public static final int SIGNDATA_SIGN_DATA_INDEX  = 7;
    public static final int SIGNDATA_PHOTO_NAME_INDEX = 8;
    public static final int SIGNDATA_PHOTO_INDEX      = 9;
    public static final int SIGNDATA_SENDSTATUS_INDEX = 10;

    private String id;
    private String userId;
    private String sNoteId;
    private String userName;
    private String sNoteName;
    private String viewDate;
    private String signName;
    private byte[] signData;
    private String photoName;
    private byte[] photo;
    private String sendStatus;

    public SignData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getsNoteId() {
        return sNoteId;
    }

    public void setsNoteId(String sNoteId) {
        this.sNoteId = sNoteId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getsNoteName() {
        return sNoteName;
    }

    public void setsNoteName(String sNoteName) {
        this.sNoteName = sNoteName;
    }

    public String getViewDate() {
        return viewDate;
    }

    public void setViewDate(String viewDate) {
        this.viewDate = viewDate;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public byte[] getSignData() {
        return signData;
    }

    public void setSignData(byte[] signData) {
        this.signData = signData;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return "SignData{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", sNoteId='" + sNoteId + '\'' +
                ", userName='" + userName + '\'' +
                ", sNoteName='" + sNoteName + '\'' +
                ", viewDate='" + viewDate + '\'' +
                ", signName='" + signName + '\'' +
                ", signData=" + Arrays.toString(signData) +
                ", photo=" + Arrays.toString(photo) +
                ", sendStatus='" + sendStatus + '\'' +
                '}';
    }
}
