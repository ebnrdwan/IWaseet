package ash.wasset.models;

/**
 * Created by ahmed on 12/19/16.
 */

public class MainMenuModel {

    int menuIcon;
    String menuName;

    public MainMenuModel(String menuName, int menuIcon) {
        this.menuName = menuName;
        this.menuIcon = menuIcon;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
