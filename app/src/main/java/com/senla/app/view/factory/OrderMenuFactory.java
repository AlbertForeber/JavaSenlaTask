package com.senla.app.view.factory;

import com.senla.app.view.Navigator;
import com.senla.app.view.header.Header;
import com.senla.app.view.header.OrderMenuHeader;
import com.senla.app.view.menu.Menu;
import com.senla.app.view.menu.OrderMenu;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class OrderMenuFactory implements MenuFactory {

    // Современный способ динамического создания объектов с DI в Spring
    private final ObjectProvider<OrderMenu> objectProvider;

    public OrderMenuFactory(ObjectProvider<OrderMenu> objectProvider) {
        this.objectProvider = objectProvider;
    }

    @Override
    public Menu createMenu(Navigator navigator) {
        return objectProvider.getObject();
    }

    @Override
    public Header createHeader() {
        return new OrderMenuHeader();
    }
}
