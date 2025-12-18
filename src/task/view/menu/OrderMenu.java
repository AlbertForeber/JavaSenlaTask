package task.view.menu;

import task.controller.OrderController;
import task.controller.StorageController;
import task.view.ControllerRegistry;
import task.view.Navigator;
import task.view.enums.ControllerKey;
import task.view.enums.NavigateTo;

import java.util.List;

public class OrderMenu implements Menu {
    private final List<MenuAction> menu;
    private final Navigator navigator;

    public OrderMenu(Navigator navigator, ControllerRegistry controllerRegistry) {
        OrderController controller = (OrderController) controllerRegistry.getController(ControllerKey.ORDER);

        this.navigator = navigator;
        this.menu = List.of(
                new MenuAction("1. Создать заказ", _ -> {
                    controller.createOrder();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("2. Отменить заказ",   _ -> {
                    controller.cancelOrder();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("3. Изменить статус заказа",  _ -> {
                    controller.changeOrderStatus();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("4. Получить отсортированные заказы",  _ -> {
                    controller.getSorted();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("5. Получить подробности заказа",  _ -> {
                    controller.getOrderDetails();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("6. Получить выполненные заказы в интервал времени ",  _ -> {
                    controller.getCompletedOrdersInInterval();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("7. Получить количество выполненных заказов в интервал времени ",  _ -> {
                    controller.getOrderAmountInInterval();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("8. Получить прибыль в интервал времени ",  _ -> {
                    controller.getIncomeInInterval();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("9. Импорт заказа ",  _ -> {
                    controller.importOrder();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("10. Экспорт заказа ",  _ -> {
                    controller.exportOrder();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("11. Назад",  _ -> navigator.navigateTo(NavigateTo.MAIN))

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
        else navigator.navigateTo(NavigateTo.ORDER);
    }
}
