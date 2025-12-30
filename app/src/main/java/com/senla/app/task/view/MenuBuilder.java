package com.senla.app.task.view;

import com.senla.app.task.view.factory.MenuFactory;
import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.menu.Menu;

public interface MenuBuilder {
    void buildMenu(MenuFactory factory, Navigator navigator) throws IllegalArgumentException;
    Menu getCurrentMenu();
    Header getCurrentMenuHeader();
}
