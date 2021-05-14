package utility.navigation;

import view.appHome.AppStageController;

public class MenuBarHandler {
    private static int menuNumber = 0;
    private static AppStageController appStageController = null;

    public static AppStageController getHomeController() {
        return appStageController;
    }

    public static void setHomeController(AppStageController desktopStageController) {
        MenuBarHandler.appStageController = desktopStageController;
    }

    public static int getMenuNumber() {
        return menuNumber;
    }

    public static void setMenuNumber(int menuNumber) {
        MenuBarHandler.menuNumber = menuNumber;
    }
}
