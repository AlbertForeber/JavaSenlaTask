package com.senla.app.view.console;

import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.view.MenuRenderer;
import com.senla.app.view.header.Header;
import com.senla.app.view.menu.Menu;
import com.senla.app.view.menu.MenuAction;
import org.springframework.stereotype.Component;

@Component
@Console
public class ConsoleMenuRenderer implements MenuRenderer {

    @Override
    public void renderMenu(Menu menu, Header header) {
        header.showHeader();
        menu.getElements().forEach(MenuAction::showDescription);
    }
}
