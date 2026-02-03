package com.senla.app.task.view.factory;

import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.header.Header;
import com.senla.app.task.view.header.RequestMenuHeader;
import com.senla.app.task.view.menu.Menu;
import com.senla.app.task.view.menu.RequestMenu;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class RequestMenuFactory implements MenuFactory {

    private final ObjectProvider<RequestMenu> objectProvider;

    public RequestMenuFactory(ObjectProvider<RequestMenu> objectProvider) {
        this.objectProvider = objectProvider;
    }

    @Override
    public Menu createMenu(Navigator navigator) {
        return objectProvider.getObject();
    }

    @Override
    public Header createHeader() {
        return new RequestMenuHeader();
    }
}
