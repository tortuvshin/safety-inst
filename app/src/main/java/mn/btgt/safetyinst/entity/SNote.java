package mn.btgt.safetyinst.entity;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class SNote {
    private String id;
    private String categoryId;
    private String name;
    private String order;
    private int frameType;
    private String frameData;
    private String voiceData;
    private int timeout;

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
