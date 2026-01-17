package com.senla.app.task.view.menu;

import java.util.List;

public interface Menu {
    List<MenuAction> getElements();
    void executeAction(int actionId);

}
