package com.senla.app.task.controller;

import com.senla.annotation_processor.ConfigProcessor;
import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.task.service.StateLoadSaveFacade;
import com.senla.app.task.utils.Colors;
import com.senla.app.task.view.IOHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController extends BaseController {
    private final String STATE_PATH;
    private StateLoadSaveFacade stateLoadSaveFacade;
    private final IOHandler ioHandler;
    private final List<Object> applyConfigFor = new ArrayList<>();
    private final List<Object> needDI = new ArrayList<>();


    public MainController(
            IOHandler ioHandler,
            String pathToState
    ) {
        super();
        this.ioHandler = ioHandler;
        this.STATE_PATH = pathToState;
    }

    public <T> T addToNeedDi(T object) {
        needDI.add(object);
        return object;
    }

    public void injectDependencies() {
        for (Object o : needDI) {
            try {
                InjectProcessor.injectDependencies(o);
            } catch (IllegalArgumentException e) {
                ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
            }
        }
    }

    public void addToConfigurable(Object o) {
        applyConfigFor.add(o);
    }

    public void applyConfig() {
        for (Object o : applyConfigFor) {
            try {
                ConfigProcessor.applyConfig(o);
            } catch (IllegalArgumentException e) {
                ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
            }
        }
    }

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
