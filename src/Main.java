import Controllers.SetupController;
import DAL.TCPConnection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application {
    protected Stage primaryStage;
    protected GridPane grid;

    /**
     * Starts the javafx application
     * @param primaryStage the primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        primaryStage.setResizable(false);

        SetupController setupController = new SetupController(primaryStage);
        primaryStage.setScene(setupController.InitializeLogin());

        primaryStage.setTitle("Project 2.3");
        primaryStage.getIcons().add(new Image("/Images/GC.png"));
        primaryStage.show();
    }

    /**
     * Makes sure you're logged out of the server when you close the application
     */
    @Override
    public void stop() {
        //log out on exit

        if (TCPConnection.checkConnection()) {
            TCPConnection connection = TCPConnection.getInstance();
            connection.logout();
        }
    }

    public static void main(String args[])
    {
        launch(args);
    }
}
