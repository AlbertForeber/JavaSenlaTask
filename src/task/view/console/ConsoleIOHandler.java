package task.view.console;

import task.view.IOHandler;
import task.view.menu.Menu;

import java.util.Scanner;

public class ConsoleIOHandler implements IOHandler {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void handleOptionInput(Menu currentMenu) {
        System.out.print(">> ");
        currentMenu.executeAction(Integer.parseInt(scanner.nextLine()));
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
