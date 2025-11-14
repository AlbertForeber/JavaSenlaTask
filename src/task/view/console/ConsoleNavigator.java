package task.view.console;

import task.view.IOHandler;
import task.view.MenuBuilder;
import task.view.MenuRenderer;
import task.view.Navigator;
import task.view.enums.NavigateTo;
import task.view.factory.*;

public class ConsoleNavigator implements Navigator {
    private final MenuBuilder menuBuilder;
    private final MenuRenderer menuRenderer;
    private final IOHandler ioHandler;
    private NavigateTo currentNavPoint;

    public ConsoleNavigator(
            MenuBuilder menuBuilder,
            MenuRenderer menuRenderer,
            IOHandler ioHandler
    ) {
        this.menuBuilder = menuBuilder;
        this.menuRenderer = menuRenderer;
        this.ioHandler = ioHandler;
    }

    @Override
    public void navigateTo(NavigateTo navigateTo) {
        if (navigateTo != currentNavPoint) {
            currentNavPoint = navigateTo;

            switch (navigateTo) {
                case MAIN -> menuBuilder.buildMenu(new MainMenuFactory());
                case STORAGE -> menuBuilder.buildMenu(new StorageMenuFactory());
                case ORDER -> menuBuilder.buildMenu(new OrderMenuFactory());
                case REQUEST -> menuBuilder.buildMenu(new RequestMenuFactory());
            }
        }

        menuRenderer.renderMenu(menuBuilder.getCurrentMenu(), menuBuilder.getCurrentMenuHeader());
        ioHandler.handleOptionInput(menuBuilder.getCurrentMenu());
    }
}
