package com.senla.app;

import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.task.controller.MainController;
import com.senla.app.task.service.StateLoadSaveFacade;
import com.senla.app.task.view.*;
import com.senla.app.task.view.console.*;
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
        mainController.loadState();
        navigator.navigateTo(NavigateTo.MAIN);
    }
}
