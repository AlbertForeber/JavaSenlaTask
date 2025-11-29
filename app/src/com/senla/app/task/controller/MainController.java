package task.controller;

import task.service.StateLoadSaveFacade;
import task.utils.Colors;
import task.utils.PropertyLoader;
import task.view.IOHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MainController extends BaseController {
    private final String CONFIG_PATH;
    private final String STATE_PATH;
    private StateLoadSaveFacade stateLoadSaveFacade;
    private Properties properties = new Properties();

    public MainController(
            IOHandler ioHandler,
            String pathToConfig,
            String pathToState
    ) {
        super(ioHandler);
        this.CONFIG_PATH = pathToConfig;
        this.STATE_PATH = pathToState;
    }

    private void loadConfig() {
        // Config
        try {
            properties = PropertyLoader.loadProperties(CONFIG_PATH);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ФАЙЛ КОНФИГА НЕ НАЙДЕН" + Colors.RESET);
            ioHandler.showMessage(Colors.YELLOW + "УКАЗАННЫЙ ПУТЬ: " + CONFIG_PATH + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ КОНФИГА" + Colors.RESET);
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

    public Properties getProperties() {
        return properties;
    }
}
