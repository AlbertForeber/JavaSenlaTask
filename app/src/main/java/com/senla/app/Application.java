package com.senla.app;

import com.senla.app.task.controller.MainController;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.console.ConsoleNavigator;

import java.sql.SQLException;


public class Application {

    public static void main(String[] args) {
        // MainController
        MainController mainController = new MainController();
        mainController.addToNeedDi(mainController);

        // Navigator
        Navigator navigator = new ConsoleNavigator();
        mainController.addToNeedDi(navigator);

        mainController.injectDependencies();
//        mainController.loadState();

//        navigator.navigateTo(NavigateTo.MAIN);
//        BookDao bookDao = new BookDao();
//
        try {
            System.out.println(bookDao.findAll(null));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
