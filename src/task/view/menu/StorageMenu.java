package task.view.menu;

import task.controller.BaseController;
import task.controller.StorageController;
import task.view.ControllerRegistry;
import task.view.Navigator;
import task.view.enums.ControllerKey;
import task.view.enums.NavigateTo;

import java.util.List;

public class StorageMenu implements Menu {
    private final List<MenuAction> menu;

    public StorageMenu(Navigator navigator, ControllerRegistry controllerRegistry) {
        StorageController controller = (StorageController) controllerRegistry.getController(ControllerKey.STORAGE);

        this.menu = List.of(
                new MenuAction("1. Добавить книгу в хранилище", _ -> {
                    controller.addBookToStorage();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("2. Получить описание книги",   _ -> {
                    controller.getBookDescription();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("3. Получить малопродаваемые книги",  _ -> {
                    controller.getHardToSell();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("4. Получить отсортированные книги",  _ -> {
                    controller.getSorted();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("5. Списать книгу",  _ -> {
                    controller.writeOffBook();
                    navigator.navigateTo(NavigateTo.STORAGE);
                }),
                new MenuAction("6. Назад",  _ -> navigator.navigateTo(NavigateTo.MAIN))

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
