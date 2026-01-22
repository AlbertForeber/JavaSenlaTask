package com.senla.app.task.view.console;

import com.senla.app.task.utils.Colors;
import com.senla.app.task.view.IOHandler;
import com.senla.app.task.view.menu.Menu;

import java.util.Scanner;

public class ConsoleIOHandler implements IOHandler {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void handleOptionInput(Menu currentMenu) {
        System.out.print(">> ");

        try {
            currentMenu.executeAction(Integer.parseInt(scanner.nextLine()));
        } catch (NumberFormatException e) {
            showMessage(Colors.YELLOW + "ПУНКТ МЕНЮ ДОЛЖЕН БЫТЬ ЧИСЛОМ" + Colors.RESET);
            currentMenu.executeAction(-1);
        }
    }

    @Override
    public String handleInput() {
        System.out.print(">> ");
        return scanner.nextLine();
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}
