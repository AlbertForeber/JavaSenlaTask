package task.view.menu;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuAction {
    private final String description;
    private final Consumer<Object> action;

    public MenuAction(String description, Consumer<Object> action) {
        this.description = description;
        this.action = action;
    }

    public void performAction() {
        action.accept(0);
    }

    public void showDescription() {
        System.out.println(description);
    }
}
