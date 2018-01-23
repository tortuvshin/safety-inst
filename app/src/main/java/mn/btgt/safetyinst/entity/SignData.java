package mn.btgt.safetyinst.entity;

import java.util.Arrays;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */


public class SignData {
    private String id;
    private String userId;
    private String sNoteId;
    private String viewDate;
    private byte[] userSign;
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

    public String getViewDate() {
        return viewDate;
    }

    public void setViewDate(String viewDate) {
        this.viewDate = viewDate;
    }

    public byte[] getUserSign() {
        return userSign;
    }

    public void setUserSign(byte[] userSign) {
        this.userSign = userSign;
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
                ", viewDate=" + viewDate +
                ", userSign=" + Arrays.toString(userSign) +
                ", photo=" + Arrays.toString(photo) +
                ", sendStatus='" + sendStatus + '\'' +
                '}';
    }
}
