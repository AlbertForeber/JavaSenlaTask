package task.view;

import task.controller.BaseController;
import task.view.enums.ControllerKey;

public interface ControllerRegistry {
    void registerController(ControllerKey key, BaseController controller);
    BaseController getController(ControllerKey key);
}
