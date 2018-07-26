package mn.btgt.safetyinst.database.model;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */
@Deprecated
public class SNote {

    public static final String TABLE_SNOTE       = "snote";
    public static final String SNOTE_ID          = "id";
    public static final String SNOTE_CAT_ID      = "category_id";
    public static final String SNOTE_NAME        = "name";
    public static final String SNOTE_ORDER       = "sorder";
    public static final String SNOTE_FRAME_TYPE  = "frame_type";
    public static final String SNOTE_FRAME_DATA  = "frame_data";
    public static final String SNOTE_VOICE_DATA  = "voice_data";
    public static final String SNOTE_TIMEOUT     = "timeout";

    public static final int SNOTE_ID_INDEX         = 0;
    public static final int SNOTE_CAT_ID_INDEX     = 1;
    public static final int SNOTE_NAME_INDEX       = 2;
    public static final int SNOTE_ORDER_INDEX      = 3;
    public static final int SNOTE_FRAME_TYPE_INDEX = 4;
    public static final int SNOTE_FRAME_DATA_INDEX = 5;
    public static final int SNOTE_VOICE_DATA_INDEX = 6;
    public static final int SNOTE_TIMEOUT_INDEX    = 7;

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

    @Override
    public String toString() {
        return "SNote{" +
                "id='" + id + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", order='" + order + '\'' +
                ", frameType=" + frameType +
                ", frameData='" + frameData + '\'' +
                ", voiceData='" + voiceData + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
