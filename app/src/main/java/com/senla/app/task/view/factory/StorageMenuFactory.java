package com.senla.app.task.view.factory;

import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.header.StorageMenuHeader;
import com.senla.app.task.view.menu.Menu;
import com.senla.app.task.view.menu.StorageMenu;

public class StorageMenuFactory implements MenuFactory {
    @Override
    public Menu createMenu(Navigator navigator) {
        return new StorageMenu(navigator);
    }

    @Override
    public Header createHeader() {
        return new StorageMenuHeader();
    }
}
