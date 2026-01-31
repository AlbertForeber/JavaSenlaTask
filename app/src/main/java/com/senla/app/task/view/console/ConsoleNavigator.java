package com.senla.app.task.view.console;

import com.senla.annotation.InjectTo;
import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.task.utils.Colors;
import com.senla.app.task.view.IOHandler;
import com.senla.app.task.view.MenuBuilder;
import com.senla.app.task.view.MenuRenderer;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.enums.NavigateTo;
import com.senla.app.task.view.factory.*;
import org.springframework.context.ApplicationContext;
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

    ApplicationContext context;

    private NavigateTo currentNavPoint;

    public ConsoleNavigator(
            @Console MenuBuilder menuBuilder,
            @Console MenuRenderer menuRenderer,
            @Console IOHandler ioHandler,
            ApplicationContext context
    ) {
        this.menuBuilder = menuBuilder;
        this.menuRenderer = menuRenderer;
        this.ioHandler = ioHandler;
        this.context = context;
    }

    @Override
    public void navigateTo(NavigateTo navigateTo) {
        if (navigateTo != currentNavPoint) {
            try {
                currentNavPoint = navigateTo;

                switch (navigateTo) {
                    case MAIN -> menuBuilder.buildMenu(context.getBean("mainMenuFactory", MainMenuFactory.class), this);
                    case STORAGE -> menuBuilder.buildMenu(context.getBean("storageMenuFactory", StorageMenuFactory.class), this);
                    case ORDER -> menuBuilder.buildMenu(context.getBean("orderMenuFactory", OrderMenuFactory.class), this);
                    case REQUEST -> menuBuilder.buildMenu(context.getBean("requestMenuFactory", RequestMenuFactory.class), this);
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
