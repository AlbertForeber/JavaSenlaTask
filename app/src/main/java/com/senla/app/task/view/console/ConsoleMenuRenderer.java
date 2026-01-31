package com.senla.app.task.view.console;

import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.task.view.MenuRenderer;
import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.menu.Menu;
import com.senla.app.task.view.menu.MenuAction;
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
