package com.senla.app.task.view.factory;

import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.header.OrderMenuHeader;
import com.senla.app.task.view.menu.Menu;
import com.senla.app.task.view.menu.OrderMenu;

public class OrderMenuFactory implements MenuFactory {

    @Override
    public Menu createMenu(Navigator navigator) {
        return new OrderMenu(navigator);
    }

    @Override
    public Header createHeader() {
        return new OrderMenuHeader();
    }
}
