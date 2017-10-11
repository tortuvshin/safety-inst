package btgt.mn.safetyinst.entity;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class SignData {
    String id;
    String userId;
    String sNoteId;
    String viewDate;
    byte[] userSign;
    byte[] photo;
    String sendStatus;

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
}
