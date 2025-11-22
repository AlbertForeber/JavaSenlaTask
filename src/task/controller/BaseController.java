package task.controller;

import task.view.IOHandler;

public abstract class BaseController {
    protected final IOHandler ioHandler;

    public BaseController(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }
}
