package task.controller;

import task.service.StateLoadSaveFacade;
import task.utils.Colors;
import task.utils.PropertyLoader;
import task.view.IOHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MainController extends BaseController {
    private StateLoadSaveFacade stateLoadSaveFacade;
    private Properties properties = new Properties();

    public MainController(
            IOHandler ioHandler,
            String pathToConfig
    ) {
        super(ioHandler);

        // Config
        try {
            properties = PropertyLoader.loadProperties(pathToConfig);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ФАЙЛ КОНФИГА НЕ НАЙДЕН" + Colors.RESET);
            ioHandler.showMessage(Colors.YELLOW + "УКАЗАННЫЙ ПУТЬ: " + pathToConfig + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ КОНФИГА" + Colors.RESET);
        }
    }

    public void loadState() {
        ioHandler.showMessage(Colors.BLUE + "Введите путь для загрузки файлов состояния" + Colors.RESET);
        String path = ioHandler.handleInput();

        try {
            stateLoadSaveFacade.loadState(path);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ФАЙЛ '" + path + "' НЕ НАЙДЕН" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ ФАЙЛА" + Colors.RESET);
        } catch (ClassNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "КЛАСС ДЛЯ ПАРСИНГА НЕ НАЙДЕН" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }

    public void saveState() {
        ioHandler.showMessage(Colors.BLUE + "Введите путь для сохранения файлов состояния" + Colors.RESET);
        String path = ioHandler.handleInput();

        try {
            stateLoadSaveFacade.saveState(path);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ФАЙЛ '" + path + "' НЕ НАЙДЕН" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ ФАЙЛА" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }

    public void setStateLoadSaveFacade(StateLoadSaveFacade stateLoadSaveFacade) {
        this.stateLoadSaveFacade = stateLoadSaveFacade;
    }

    public Properties getProperties() {
        return properties;
    }
}
