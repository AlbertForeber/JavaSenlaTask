package com.senla.app.task.view.factory;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.header.MainMenuHeader;
import com.senla.app.task.view.menu.MainMenu;
import com.senla.app.task.view.menu.Menu;

public class MainMenuFactory implements MenuFactory {
    @Override
    public Menu createMenu(Navigator navigator) {
        return new MainMenu(navigator);
    }

    @Override
    public Header createHeader() {
        return new MainMenuHeader();
    }
}
