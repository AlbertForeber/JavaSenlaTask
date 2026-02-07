package com.senla.app.view.console;

import com.senla.annotation.InjectTo;
import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.utils.Colors;
import com.senla.app.view.IOHandler;
import com.senla.app.view.MenuBuilder;
import com.senla.app.view.MenuRenderer;
import com.senla.app.view.Navigator;
import com.senla.app.view.enums.NavigateTo;
import com.senla.app.view.factory.FactoryProvider;
import org.springframework.stereotype.Component;

@Component
@Console
public class ConsoleNavigator implements Navigator {

    @InjectTo(useImplementation = ConsoleMenuBuilder.class)
    private final MenuBuilder menuBuilder;

    @InjectTo(useImplementation = ConsoleMenuRenderer.class)
    private final MenuRenderer menuRenderer;

    @InjectTo(useImplementation = ConsoleIOHandler.class)
    private final IOHandler ioHandler;

    FactoryProvider factoryProvider;

    private NavigateTo currentNavPoint;

    public ConsoleNavigator(
            @Console MenuBuilder menuBuilder,
            @Console MenuRenderer menuRenderer,
            @Console IOHandler ioHandler,
            FactoryProvider factoryProvider
    ) {
        this.menuBuilder = menuBuilder;
        this.menuRenderer = menuRenderer;
        this.ioHandler = ioHandler;
        this.factoryProvider = factoryProvider;
    }

    @Override
    public void navigateTo(NavigateTo navigateTo) {
        if (navigateTo != currentNavPoint) {
            try {
                currentNavPoint = navigateTo;

                switch (navigateTo) {
                    case MAIN       -> menuBuilder.buildMenu(factoryProvider.getFactory("mainMenuFactory"), this);
                    case STORAGE    -> menuBuilder.buildMenu(factoryProvider.getFactory("storageMenuFactory"), this);
                    case ORDER      -> menuBuilder.buildMenu(factoryProvider.getFactory("orderMenuFactory"), this);
                    case REQUEST    -> menuBuilder.buildMenu(factoryProvider.getFactory("requestMenuFactory"), this);
                }
            } catch (IllegalArgumentException e) {
                ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
            }
        }

        menuRenderer.renderMenu(menuBuilder.getCurrentMenu(), menuBuilder.getCurrentMenuHeader());
        ioHandler.handleOptionInput(menuBuilder.getCurrentMenu());
    }
    private void startNavigation() {
        navigateTo(NavigateTo.MAIN);
    }
}
