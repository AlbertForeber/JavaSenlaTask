package com.senla.app.task.view.factory;

import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.header.RequestMenuHeader;
import com.senla.app.task.view.menu.Menu;
import com.senla.app.task.view.menu.RequestMenu;

public class RequestMenuFactory implements MenuFactory {
    @Override
    public Menu createMenu() {
        return new RequestMenu();
    }

    @Override
    public Header createHeader() {
        return new RequestMenuHeader();
    }
}
