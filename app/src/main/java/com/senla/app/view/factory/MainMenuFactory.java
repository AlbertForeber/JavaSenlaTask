package com.senla.app.view.factory;
import com.senla.app.view.Navigator;
import com.senla.app.view.header.Header;
import com.senla.app.view.header.MainMenuHeader;
import com.senla.app.view.menu.MainMenu;
import com.senla.app.view.menu.Menu;
import org.springframework.stereotype.Component;

@Component
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
