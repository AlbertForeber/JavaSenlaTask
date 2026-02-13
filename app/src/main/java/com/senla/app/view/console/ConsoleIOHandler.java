package com.senla.app.view.console;

import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.utils.Colors;
import com.senla.app.view.IOHandler;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@Console
public class ConsoleIOHandler implements IOHandler {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void handleOptionInput() {
        System.out.print(">> ");

        try {
        } catch (NumberFormatException e) {
            showMessage(Colors.YELLOW + "ПУНКТ МЕНЮ ДОЛЖЕН БЫТЬ ЧИСЛОМ" + Colors.RESET);
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
