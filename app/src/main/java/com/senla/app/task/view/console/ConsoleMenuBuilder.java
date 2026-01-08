package com.senla.app.task.view.console;

import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.task.view.MenuBuilder;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.factory.MenuFactory;
import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.menu.Menu;

public class ConsoleMenuBuilder implements MenuBuilder {

    private Menu currentMenu;
    private Header header;


    @Override
    public void buildMenu(MenuFactory factory, Navigator navigator) throws IllegalArgumentException {
        Menu newMenu = factory.createMenu(navigator);

        InjectProcessor.injectDependencies(newMenu);

        currentMenu = newMenu;
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
