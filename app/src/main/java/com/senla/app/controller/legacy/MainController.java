package com.senla.app.controller.legacy;

import com.senla.annotation.ui_qualifiers.Console;
import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.utils.Colors;
import com.senla.app.utils.Routes;
import com.senla.app.view.IOHandler;

import java.util.ArrayList;
import java.util.List;


public class MainController {

    private final String STATE_PATH = Routes.PATH_TO_STATE;

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
