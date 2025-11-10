package task.view.factory;
import task.view.ControllerRegistry;
import task.view.Navigator;
import task.view.header.Header;
import task.view.header.MainMenuHeader;
import task.view.menu.MainMenu;
import task.view.menu.Menu;

public class MainMenuFactory implements MenuFactory {
    @Override
    public Menu createMenu(Navigator navigator, ControllerRegistry controllerRegistry) {
        return new MainMenu(navigator);
    }

    @Override
    public Header createHeader() {
        return new MainMenuHeader();
    }
}
