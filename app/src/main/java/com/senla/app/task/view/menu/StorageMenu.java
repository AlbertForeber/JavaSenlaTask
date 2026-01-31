package com.senla.app.task.view.menu;

import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.task.controller.StorageController;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.enums.NavigateTo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class StorageMenu implements Menu {

    private final List<MenuAction> menu;
    private final Navigator navigator;

    public StorageMenu(
            @Console Navigator navigator,
            StorageController controller
    ) {
        this.navigator = navigator;
        this.menu = List.of(
                new MenuAction("1. Добавить книгу в хранилище", o -> {
                    controller.addBookToStorage();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("2. Получить описание книги",   o -> {
                    controller.getBookDescription();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("3. Получить малопродаваемые книги",  o -> {
                    controller.getHardToSell();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("4. Получить отсортированные книги",  o -> {
                    controller.getSorted();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("5. Списать книгу",  o -> {
                    controller.writeOffBook();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("6. Назад",  o -> navigator.navigateTo(NavigateTo.MAIN))
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
        } else navigator.navigateTo(NavigateTo.STORAGE);
    }
}
