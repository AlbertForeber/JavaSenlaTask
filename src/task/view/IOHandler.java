package task.view;

import task.view.menu.Menu;

public interface IOHandler {
    void handleOptionInput(Menu currentMenu);
    String handleInput();
    void showMessage(String message);
}
