package com.senla.app;

import com.senla.app.task.controller.MainController;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.console.ConsoleNavigator;
import com.senla.app.task.view.enums.NavigateTo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
