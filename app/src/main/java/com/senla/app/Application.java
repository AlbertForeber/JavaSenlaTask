package com.senla.app;

import com.senla.app.task.utils.HibernateUtil;
import com.senla.app.task.view.Navigator;
import com.senla.app.task.view.console.ConsoleNavigator;
import com.senla.app.task.view.enums.NavigateTo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties") // Иначе конфигурация не работает
@ComponentScan("com.senla.app")
public class Application {

    public static void main(String[] args) {
        try {
//            // MainController
//            MainController mainController = new MainController();
//            mainController.addToNeedDi(mainController);
//
//            // Navigator
//            Navigator navigator = new ConsoleNavigator();
//            mainController.addToNeedDi(navigator);
//
//            mainController.injectDependencies();
////        mainController.loadState();
//
//            navigator.navigateTo(NavigateTo.MAIN);
////        BookDao bookDao = new BookDao();
////
////        try {
////            bookDao.findAll(null).forEach(o -> System.out.println(o.toBusinessObject()));
////            HibernateUtil.shutdown();
////        } finally {
////            HibernateUtil.shutdown();
////        }
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
            Navigator navigator = applicationContext.getBean("consoleNavigator", ConsoleNavigator.class);
            navigator.navigateTo(NavigateTo.MAIN);
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
