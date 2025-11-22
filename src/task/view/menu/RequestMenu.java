package task.view.menu;

import task.controller.RequestController;
import task.controller.StorageController;
import task.view.ControllerRegistry;
import task.view.Navigator;
import task.view.enums.ControllerKey;
import task.view.enums.NavigateTo;

import java.util.List;

public class RequestMenu implements Menu {
    private final List<MenuAction> menu;

    public RequestMenu(Navigator navigator, ControllerRegistry controllerRegistry) {
        RequestController controller = (RequestController) controllerRegistry.getController(ControllerKey.REQUEST);

        this.menu = List.of(
                new MenuAction("1. Добавить запрос", _ -> {
                    controller.createRequest();
                    navigator.navigateTo(NavigateTo.REQUEST);
                }),
                new MenuAction("2. Получить отсортированные запросы",   _ -> {
                    controller.getSorted();
                    navigator.navigateTo(NavigateTo.REQUEST);
                }),
                new MenuAction("3. Импорт запроса",   _ -> {
                    controller.importRequest();
                    navigator.navigateTo(NavigateTo.REQUEST);
                }),
                new MenuAction("4. Экспорт запроса",   _ -> {
                    controller.exportRequest();
                    navigator.navigateTo(NavigateTo.REQUEST);
                }),
                new MenuAction("5. Назад",  _ -> navigator.navigateTo(NavigateTo.MAIN))
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
