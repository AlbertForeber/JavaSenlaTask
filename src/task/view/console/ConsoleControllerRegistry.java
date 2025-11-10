package task.view.console;

import task.controller.BaseController;
import task.controller.RequestController;
import task.view.ControllerRegistry;
import task.view.enums.ControllerKey;

import java.util.HashMap;

public class ConsoleControllerRegistry implements ControllerRegistry {
    HashMap<ControllerKey, BaseController> registry = new HashMap<>();

    @Override
    public void registerController(ControllerKey key, BaseController controller) {
        registry.put(key, controller);
    }

    @Override
    public BaseController getController(ControllerKey key) {
        return registry.get(key);
    }
}
