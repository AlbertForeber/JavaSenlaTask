package task.view;

import task.view.factory.MenuFactory;
import task.view.header.Header;
import task.view.menu.Menu;

public interface MenuBuilder {
    void setNavigator(Navigator navigator);
    void setControllerRegistry(ControllerRegistry controllerRegistry);
    void buildMenu(MenuFactory factory);
    Menu getCurrentMenu();
    Header getCurrentMenuHeader();
}
