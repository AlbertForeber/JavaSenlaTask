package com.senla.app.view.menu;

import com.senla.app.view.Navigator;
import com.senla.app.view.enums.NavigateTo;

import java.util.List;

public class MainMenu implements Menu {

    private final List<MenuAction> menu;
    private final Navigator navigator;

    public MainMenu(Navigator navigator) {
        this.navigator = navigator;
        this.menu = List.of(
                new MenuAction("1. Меню управления хранилищем", o -> navigator.navigateTo(NavigateTo.STORAGE)),
                new MenuAction("2. Меню управления заказами",   o -> navigator.navigateTo(NavigateTo.ORDER)),
                new MenuAction("3. Меню управления запросами",  o -> navigator.navigateTo(NavigateTo.REQUEST)),
                new MenuAction("4. Выход",  o -> { })
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
