package task.view.console;

import task.view.ControllerRegistry;
import task.view.MenuBuilder;
import task.view.Navigator;
import task.view.factory.MenuFactory;
import task.view.header.Header;
import task.view.menu.Menu;

public class ConsoleMenuBuilder implements MenuBuilder {
    private Menu currentMenu;
    private Header header;
    private Navigator navigator;
    private ControllerRegistry controllerRegistry;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void setControllerRegistry(ControllerRegistry controllerRegistry) {
        this.controllerRegistry = controllerRegistry;
    }

    @Override
    public void buildMenu(MenuFactory factory) {
        currentMenu = factory.createMenu(navigator, controllerRegistry);
        header = factory.createHeader();
    }

    @Override
    public Menu getCurrentMenu() {
        return currentMenu;
    }

    @Override
    public Header getCurrentMenuHeader() {
        return header;
    }
}
