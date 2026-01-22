package com.senla.app.task.view.console;

import com.senla.app.task.view.MenuRenderer;
import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.menu.Menu;
import com.senla.app.task.view.menu.MenuAction;

public class ConsoleMenuRenderer implements MenuRenderer {

    @Override
    public void renderMenu(Menu menu, Header header) {
        header.showHeader();
        menu.getElements().forEach(MenuAction::showDescription);
    }
}
