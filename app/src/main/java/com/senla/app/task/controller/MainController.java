package com.senla.app.task.controller;

import com.senla.annotation.InjectTo;
import com.senla.annotation.ui_qualifiers.Console;
import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.task.utils.Colors;
import com.senla.app.task.utils.Routes;
import com.senla.app.task.view.IOHandler;
import com.senla.app.task.view.console.ConsoleIOHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainController {

    private final String STATE_PATH = Routes.PATH_TO_STATE;

    @InjectTo(useImplementation = ConsoleIOHandler.class)
    private final IOHandler ioHandler;

    private final List<Object> needDI = new ArrayList<>();

    public MainController(@Console IOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public <T> void addToNeedDi(T object) {
        needDI.add(object);
    }

    public void injectDependencies() {
        for (Object o : needDI) {
            try {
                InjectProcessor.injectDependencies(o);
            } catch (IllegalArgumentException e) {
                if (ioHandler != null) {
                    ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
                } else {
                    System.err.println("Для корректной обработки ошибок IOHandler должен быть первой зависимостью класса");
                    System.exit(1);
                }
            }
        }
    }


//    public void applyConfig(List<Object> applyConfigFor) {
//        for (Object o : applyConfigFor) {
//            try {
//                ConfigProcessor.applyConfig(o);
//            } catch (IllegalArgumentException e) {
//                ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
//            }
//        }
//    }
}
