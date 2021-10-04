package ch.zhaw.theluckyseven.frantic.view;

import ch.zhaw.theluckyseven.frantic.controller.MenuViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class to create the MenuView
 */
public class MenuView {
    static Stage primaryStage;

    /**
     * Creates and displays the MenuView
     *
     * @param primaryStage the stage for the View
     */
    public MenuView(Stage primaryStage) {
        MenuView.primaryStage = primaryStage;
        display();
    }

    private void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch.zhaw.theluckyseven.frantic.view/MainMenuWindow.fxml"));
            Pane rootPane = loader.load();

            MenuViewController controller = loader.getController();
            controller.startMenuViewController(primaryStage);

            Scene scene = new Scene(rootPane);
            primaryStage.setScene(scene);
            primaryStage.resizableProperty().setValue(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
