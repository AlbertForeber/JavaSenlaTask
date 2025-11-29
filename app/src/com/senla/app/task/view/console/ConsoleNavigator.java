package com.senla.app.task.view.console;

import com.senla.annotation.InjectTo;
import com.senla.app.task.utils.Colors;
import com.senla.app.task.view.IOHandler;
import com.senla.app.task.view.MenuBuilder;
import com.senla.app.task.view.MenuRenderer;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.enums.NavigateTo;
import com.senla.app.task.view.factory.*;

public class ConsoleNavigator implements Navigator {

    @InjectTo
    private MenuBuilder menuBuilder;

    @InjectTo
    private MenuRenderer menuRenderer;

    @InjectTo
    private IOHandler ioHandler;

    private NavigateTo currentNavPoint;


    @Override
    public void navigateTo(NavigateTo navigateTo) {
        if (navigateTo != currentNavPoint) {
            try {
                currentNavPoint = navigateTo;

                switch (navigateTo) {
                    case MAIN -> menuBuilder.buildMenu(new MainMenuFactory());
                    case STORAGE -> menuBuilder.buildMenu(new StorageMenuFactory());
                    case ORDER -> menuBuilder.buildMenu(new OrderMenuFactory());
                    case REQUEST -> menuBuilder.buildMenu(new RequestMenuFactory());
                }
            } catch (IllegalArgumentException e) {
                ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ОТОБРАЖЕНИЯ: " + e.getMessage() + Colors.RESET);
            }
        }

        menuRenderer.renderMenu(menuBuilder.getCurrentMenu(), menuBuilder.getCurrentMenuHeader());
        ioHandler.handleOptionInput(menuBuilder.getCurrentMenu());
    }
}
