package btgt.mn.safetyinst.entity;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class SignData {
    String id;
    String userId;
    String sNoteId;
    String viewDate;
    String userSign;
    String photo;
    String sendStatus;

    public SignData(String id, String userId, String sNoteId, String viewDate, String userSign, String photo, String sendStatus) {
        this.id = id;
        this.userId = userId;
        this.sNoteId = sNoteId;
        this.viewDate = viewDate;
        this.userSign = userSign;
        this.photo = photo;
        this.sendStatus = sendStatus;
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

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }
}
