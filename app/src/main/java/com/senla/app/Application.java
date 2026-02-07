package com.senla.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties") // Иначе конфигурация не работает
@ComponentScan("com.senla.app")
public class Application {

//    public static void main(String[] args) {
////            // MainController
////            MainController mainController = new MainController();
////            mainController.addToNeedDi(mainController);
////
////            // Navigator
////            Navigator navigator = new ConsoleNavigator();
////            mainController.addToNeedDi(navigator);
////
////            mainController.injectDependencies();
//////        mainController.loadState();
////
////            navigator.navigateTo(NavigateTo.MAIN);
//
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
//        Navigator navigator = applicationContext.getBean("consoleNavigator", ConsoleNavigator.class);
//        navigator.navigateTo(NavigateTo.MAIN);
//    }
}
