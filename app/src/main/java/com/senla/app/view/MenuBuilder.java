package com.senla.app.view;

import com.senla.app.view.factory.MenuFactory;
import com.senla.app.view.header.Header;
import com.senla.app.view.menu.Menu;

public interface MenuBuilder {

    void buildMenu(MenuFactory factory, Navigator navigator) throws IllegalArgumentException;
    Menu getCurrentMenu();
    Header getCurrentMenuHeader();
}
