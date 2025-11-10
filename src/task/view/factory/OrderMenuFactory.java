package task.view.factory;

import task.view.ControllerRegistry;
import task.view.Navigator;
import task.view.header.Header;
import task.view.header.OrderMenuHeader;
import task.view.menu.Menu;
import task.view.menu.OrderMenu;

public class OrderMenuFactory implements MenuFactory {
    @Override
    public Menu createMenu(Navigator navigator, ControllerRegistry controllerRegistry) {
        return new OrderMenu(navigator, controllerRegistry);
    }

    @Override
    public Header createHeader() {
        return new OrderMenuHeader();
    }
}
