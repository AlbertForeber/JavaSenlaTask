package task.view.menu;

import task.controller.MainController;
import task.controller.OrderController;
import task.view.ControllerRegistry;
import task.view.Navigator;
import task.view.enums.ControllerKey;
import task.view.enums.NavigateTo;

import java.util.List;

public class MainMenu implements Menu {

    private final List<MenuAction> menu;
    private final Navigator navigator;

    public MainMenu(Navigator navigator, ControllerRegistry controllerRegistry) {
        MainController controller = (MainController) controllerRegistry.getController(ControllerKey.MAIN);


        this.navigator = navigator;
        this.menu = List.of(
                new MenuAction("1. Меню управления хранилищем", _ -> navigator.navigateTo(NavigateTo.STORAGE)),
                new MenuAction("2. Меню управления заказами",   _ -> navigator.navigateTo(NavigateTo.ORDER)),
                new MenuAction("3. Меню управления запросами",  _ -> navigator.navigateTo(NavigateTo.REQUEST)),
                new MenuAction("4. Выход",  _ -> controller.saveState())
        );
    }

    @Override
    public List<MenuAction> getElements() {
        return List.copyOf(menu);
    }

    @Override
    public void executeAction(int actionId) {
        actionId -= 1;
        if (!(actionId < 0 || actionId >= menu.size())) {
            menu.get(actionId).performAction();
        } else navigator.navigateTo(NavigateTo.MAIN);
    }
}
