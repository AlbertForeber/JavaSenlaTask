package com.senla.app.task.view.menu;

import com.senla.annotation.InjectTo;
import com.senla.app.task.controller.OrderController;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.enums.NavigateTo;

import java.util.List;

public class OrderMenu implements Menu {

    private final List<MenuAction> menu;
    private final Navigator navigator;

    @InjectTo
    OrderController controller;

    public OrderMenu(Navigator navigator) {
        this.navigator = navigator;
        this.menu = List.of(
                new MenuAction("1. Создать заказ", o -> {
                    controller.createOrder();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("2. Отменить заказ",   o -> {
                    controller.cancelOrder();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("3. Изменить статус заказа",  o -> {
                    controller.changeOrderStatus();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("4. Получить отсортированные заказы",  o -> {
                    controller.getSorted();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("5. Получить подробности заказа",  o -> {
                    controller.getOrderDetails();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("6. Получить выполненные заказы в интервал времени ",  o -> {
                    controller.getCompletedOrdersInInterval();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("7. Получить количество выполненных заказов в интервал времени ",  o -> {
                    controller.getOrderAmountInInterval();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("8. Получить прибыль в интервал времени ",  o -> {
                    controller.getIncomeInInterval();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("9. Импорт заказа ",  o -> {
                    controller.importOrder();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("10. Экспорт заказа ",  o -> {
                    controller.exportOrder();
                    navigator.navigateTo(NavigateTo.ORDER);
                }),
                new MenuAction("11. Назад",  o -> navigator.navigateTo(NavigateTo.MAIN))

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
        } else navigator.navigateTo(NavigateTo.ORDER);
    }
}
