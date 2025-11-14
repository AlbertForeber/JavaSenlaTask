package task.view.factory;

import task.view.ControllerRegistry;
import task.view.Navigator;
import task.view.header.Header;
import task.view.header.StorageMenuHeader;
import task.view.menu.Menu;
import task.view.menu.StorageMenu;

public class StorageMenuFactory implements MenuFactory {
    @Override
    public Menu createMenu(Navigator navigator, ControllerRegistry controllerRegistry) {
        return new StorageMenu(navigator, controllerRegistry);
    }

    @Override
    public Header createHeader() {
        return new StorageMenuHeader();
    }
}
