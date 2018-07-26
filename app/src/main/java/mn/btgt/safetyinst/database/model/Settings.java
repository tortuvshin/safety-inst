package mn.btgt.safetyinst.database.model;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

@Deprecated
public class Settings {

    public static final String TABLE_SETTINGS = "settings";
    public static final String SETTINGS_KEY   = "settings_key";
    public static final String SETTINGS_VALUE = "settings_value";

    public static final int SETTINGS_KEY_INDEX = 0;
    public static final int SETTINGS_VALUE_INDEX = 1;

    private String key;
    private String value;

    public Settings() {
    }

    public Settings(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}