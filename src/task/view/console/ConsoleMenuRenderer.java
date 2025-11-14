package task.view.console;

import task.view.MenuRenderer;
import task.view.header.Header;
import task.view.menu.Menu;
import task.view.menu.MenuAction;

public class ConsoleMenuRenderer implements MenuRenderer {
    @Override
    public void renderMenu(Menu menu, Header header) {
        header.showHeader();
        menu.getElements().forEach(MenuAction::showDescription);
    }
}
