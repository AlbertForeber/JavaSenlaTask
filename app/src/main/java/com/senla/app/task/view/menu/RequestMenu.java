package com.senla.app.task.view.menu;

import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.task.controller.RequestController;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.enums.NavigateTo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class RequestMenu implements Menu {

    private final List<MenuAction> menu;
    private final Navigator navigator;

    public RequestMenu(
            @Console Navigator navigator,
            RequestController controller
    ) {
        this.navigator = navigator;
        this.menu = List.of(
                new MenuAction("1. Добавить запрос", o -> {
                    controller.createRequest();
                    navigator.navigateTo(NavigateTo.REQUEST);
                }),
                new MenuAction("2. Получить отсортированные запросы",   o -> {
                    controller.getSorted();
                    navigator.navigateTo(NavigateTo.REQUEST);
                }),
                new MenuAction("3. Назад",  o -> navigator.navigateTo(NavigateTo.MAIN))
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
        } else navigator.navigateTo(NavigateTo.REQUEST);
    }
}
