package com.senla.app.task.view;

import com.senla.app.task.view.menu.Menu;

public interface IOHandler {

    void handleOptionInput(Menu currentMenu);
    String handleInput();
    void showMessage(String message);
}
