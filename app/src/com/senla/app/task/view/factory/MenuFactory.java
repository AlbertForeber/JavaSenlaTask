package com.senla.app.task.view.factory;

import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.menu.Menu;

public interface MenuFactory {
    Menu createMenu();
    Header createHeader();
}
