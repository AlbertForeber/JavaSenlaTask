package com.senla.app.view.factory;

import com.senla.app.view.Navigator;
import com.senla.app.view.header.Header;
import com.senla.app.view.header.StorageMenuHeader;
import com.senla.app.view.menu.Menu;
import com.senla.app.view.menu.StorageMenu;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class StorageMenuFactory implements MenuFactory {

    private final ObjectProvider<StorageMenu> objectProvider;

    public StorageMenuFactory(ObjectProvider<StorageMenu> objectProvider) {
        this.objectProvider = objectProvider;
    }

    @Override
    public Menu createMenu(Navigator navigator) {
        return objectProvider.getObject();
    }

    @Override
    public Header createHeader() {
        return new StorageMenuHeader();
    }
}
