package com.senla.app.view.console;

import com.senla.annotation.ui_qualifiers.Console;
import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.view.MenuBuilder;
import com.senla.app.view.Navigator;
import com.senla.app.view.factory.MenuFactory;
import com.senla.app.view.header.Header;
import com.senla.app.view.menu.Menu;
import org.springframework.stereotype.Component;

@Component
@Console
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
