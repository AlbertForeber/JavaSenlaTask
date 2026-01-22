package com.senla.app;

import com.senla.app.task.controller.MainController;
import com.senla.app.task.utils.HibernateUtil;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.console.ConsoleNavigator;
import com.senla.app.task.view.enums.NavigateTo;


public class Application {

    public static void main(String[] args) {
        try {
            // MainController
            MainController mainController = new MainController();
            mainController.addToNeedDi(mainController);

            // Navigator
            Navigator navigator = new ConsoleNavigator();
            mainController.addToNeedDi(navigator);

            mainController.injectDependencies();
//        mainController.loadState();

            navigator.navigateTo(NavigateTo.MAIN);
//        BookDao bookDao = new BookDao();
//
//        try {
//            bookDao.findAll(null).forEach(o -> System.out.println(o.toBusinessObject()));
//            HibernateUtil.shutdown();
//        } finally {
//            HibernateUtil.shutdown();
//        }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
