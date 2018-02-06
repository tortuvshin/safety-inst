package mn.btgt.safetyinst.data.model;

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
