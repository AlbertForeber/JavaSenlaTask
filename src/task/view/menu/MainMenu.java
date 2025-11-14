package task.view.menu;

import task.view.Navigator;
import task.view.enums.NavigateTo;

import java.util.List;

public class MainMenu implements Menu {

    private final List<MenuAction> menu;

    public MainMenu(Navigator navigator) {
        menu = List.of(
                new MenuAction("1. Меню управления хранилищем", _ -> navigator.navigateTo(NavigateTo.STORAGE)),
                new MenuAction("2. Меню управления заказами",   _ -> navigator.navigateTo(NavigateTo.ORDER)),
                new MenuAction("3. Меню управления запросами",  _ -> navigator.navigateTo(NavigateTo.REQUEST))
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
        }
    }
}
