package com.senla.app.task.controller;

import com.senla.annotation.InjectTo;
import com.senla.annotation_processor.ConfigProcessor;
import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.task.service.StateLoadSaveFacade;
import com.senla.app.task.utils.Colors;
import com.senla.app.task.utils.Routes;
import com.senla.app.task.view.IOHandler;
import com.senla.app.task.view.console.ConsoleIOHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController extends BaseController {
    private final String STATE_PATH = Routes.PATH_TO_STATE;

    @InjectTo(useImplementation = ConsoleIOHandler.class)
    private IOHandler ioHandler;

    @InjectTo
    private StateLoadSaveFacade stateLoadSaveFacade;

    private final List<Object> needDI = new ArrayList<>();


    public MainController() {
        super();
        this.needDI.add(this);
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

    public void loadState() {
        try {
            stateLoadSaveFacade.loadState(STATE_PATH);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ДИРЕКТОРИЯ '" + STATE_PATH + "' НЕ НАЙДЕНА ИЛИ ПУСТА" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ ФАЙЛА" + Colors.RESET);
        } catch (ClassNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "КЛАСС ДЛЯ ПАРСИНГА НЕ НАЙДЕН" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }

    public void saveState() {
        try {
            stateLoadSaveFacade.saveState(STATE_PATH);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ФАЙЛ '" + STATE_PATH + "' НЕ НАЙДЕН" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ ФАЙЛА" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }

        ioHandler.showMessage(Colors.YELLOW + "Состояние сохранено в " + STATE_PATH + Colors.RESET);
    }

    public void setStateLoadSaveFacade(StateLoadSaveFacade stateLoadSaveFacade) {
        this.stateLoadSaveFacade = stateLoadSaveFacade;
    }
}
