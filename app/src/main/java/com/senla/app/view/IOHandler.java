package com.senla.app.view;

import com.senla.app.view.menu.Menu;

public interface IOHandler {

    void handleOptionInput(Menu currentMenu);
    String handleInput();
    void showMessage(String message);
}
