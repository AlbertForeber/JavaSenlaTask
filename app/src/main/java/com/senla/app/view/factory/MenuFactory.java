package com.senla.app.view.factory;

import com.senla.app.view.Navigator;
import com.senla.app.view.header.Header;
import com.senla.app.view.menu.Menu;

public interface MenuFactory {

    Menu createMenu(Navigator navigator);
    Header createHeader();
}
