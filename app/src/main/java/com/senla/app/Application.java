package com.senla.app;

import com.senla.app.task.controller.MainController;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.console.ConsoleNavigator;
import com.senla.app.task.view.enums.NavigateTo;


public class Application {

    public static void main(String[] args) {
        // MainController
        MainController mainController = new MainController();
        mainController.addToNeedDi(mainController);

        // Navigator
        Navigator navigator = new ConsoleNavigator();
        mainController.addToNeedDi(navigator);

        mainController.injectDependencies();
        // mainController.loadState();
        navigator.navigateTo(NavigateTo.MAIN);
    }
}
