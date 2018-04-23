package mn.btgt.safetyinst.database.model;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * Ангилал модел
 * URL: https://www.github.com/tortuvshin
 */

public class Category {

    public static final String TABLE_CATEGORYS       = "categorys";
    public static final String CATEGORY_ID          = "id";
    public static final String CATEGORY_NAME        = "name";
    public static final String CATEGORY_ICON        = "icon";
    public static final String CATEGORY_ORDER       = "sorder";

    public static final int CATEGORY_ID_INDEX         = 0;
    public static final int CATEGORY_NAME_INDEX       = 1;
    public static final int CATEGORY_ICON_INDEX       = 2;
    public static final int CATEGORY_ORDER_INDEX      = 3;

    private String id;
    private String name;
    private String icon;
    private String order;

    public Category() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
