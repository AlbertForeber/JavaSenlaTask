package task.view.factory;

import task.view.ControllerRegistry;
import task.view.Navigator;
import task.view.header.Header;
import task.view.menu.Menu;

public interface MenuFactory {
    Menu createMenu(Navigator navigator, ControllerRegistry controllerRegistry);
    Header createHeader();
}
