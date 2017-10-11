package btgt.mn.safetyinst.entity;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class SNote {
    String id;
    String categoryId;
    String name;
    String order;
    int frameType;
    String frameData;
    String voiceData;
    int timeout;

    public SNote() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getFrameType() {
        return frameType;
    }

    public void setFrameType(int frameType) {
        this.frameType = frameType;
    }

    public String getFrameData() {
        return frameData;
    }

    public void setFrameData(String frameData) {
        this.frameData = frameData;
    }

    public String getVoiceData() {
        return voiceData;
    }

    public void setVoiceData(String voiceData) {
        this.voiceData = voiceData;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
