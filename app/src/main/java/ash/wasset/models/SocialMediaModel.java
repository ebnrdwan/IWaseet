package ash.wasset.models;

/**
 * Created by ahmed on 1/29/17.
 */

public class SocialMediaModel {

    private String Id;
    private String Name;
    private String Icon;
    private String Url;

    public String getId() {
        return Integer.toString(Double.valueOf(Id).intValue());
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String iconPath) {
        Icon = iconPath;
    }

    public String toString() {
        return getName();
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
