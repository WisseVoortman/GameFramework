package Views;

import Controllers.SetupController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.textfield.TextFields;

/**
 * Class LoginView
 * This class manages the login scene
 * Its controller is the SetupController
 */
public class LoginView {
    private TextField name, serverUrl, port ;
    private Button login;
    private SetupController controller;

    public LoginView(SetupController controller) {
        this.controller = controller;
    }

    /**
     * Makes a gridpane and calls the setters methods in this class to set the labels/textfields etc...
     * @return the scene of the login screen
     */
    public Scene getLoginScene() {
        BorderPane bp = new BorderPane();
        GridPane gridPane = new GridPane();
        bp.setTop(gridPane);
        setStyling(gridPane,bp);
        setbutton(gridPane);
        setTextboxes(gridPane);
        setEventHandlers();

        bp.setPadding(new Insets(40,0,0,40));
        bp.setStyle("-fx-background-image: url(\"/Images/water2.jpg\");-fx-background-size: 600, 300;-fx-background-repeat: no-repeat;");
        return new Scene(bp,585,300);
    }

    /**
     * Ties the login button to the login function in the SetupController
     * Gives appropiate error msgs
     */
    private void setEventHandlers() {
        this.login.setOnAction((ActionEvent e) -> {
            if(controller.checkName(name.getText())) {
                if (controller.login(name.getText(), serverUrl.getText(), port.getText())) {
                        controller.setScene(controller.InitializeSetupView());
                } else {
                    //error with loging in
                    port.setText("7789");
                    serverUrl.setText("Localhost");

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Could not Login");
                    alert.setContentText("The server could not log you in");
                    alert.showAndWait();
                }
            } else {
                //error with name syntax
                name.setText("");

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Name error");
                alert.setContentText("Your player name can't contain spaces and must at least be 1 character long and cant be longer than 7 characters");

                alert.showAndWait();
            }
        });
    }



    /**
     * Sets the textboxes name, server address and port
     * @param grid the grid to which the textfields need to be added
     */
    private void setTextboxes(GridPane grid)
    {
        name = new TextField();
        name.setPrefColumnCount(20);
        name.setPromptText("Username");

        String[] namesuggestions = {"Jelmer","Wisse","Arjen","Wouter","oldAI","newAI"};
        TextFields.bindAutoCompletion(name,namesuggestions);


        serverUrl = new TextField();
        serverUrl.setPrefColumnCount(20);
        serverUrl.setPromptText("Server address");
        //serverUrl.setText("Localhost");

        String[] suggestions = {"77.162.40.81","Localhost","145.33.225.170"};
        TextFields.bindAutoCompletion(serverUrl,suggestions);

        port = new TextField();
        port.setPrefColumnCount(20);
        port.setPromptText("Port");
        port.setText("7789");
        grid.setVgap(8.0);


        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);

        GridPane.setConstraints(serverUrl, 0, 1);
        grid.getChildren().add(serverUrl);

        GridPane.setConstraints(port, 0, 2);
        grid.getChildren().add(port);
    }

    /**
     * Sets the button the logs the user in on the grid
     * @param grid the grid to which the button need to be added
     */
    private void setbutton(GridPane grid)
    {
        grid.setVgap(5.0);
        login = new Button();
        login.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Image loginPic = new Image("/Images/login2.jpg");
        login.setGraphic(new ImageView(loginPic));
        login.setStyle("-fx-background-color: transparent;");

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(login);
        grid.add(hbBtn, 0, 3);
    }

    private void setStyling(GridPane grid,BorderPane bp){
       /* Image image = new Image("Images/loginLine.png");
        bp.setBottom(new ImageView(image));*/
        bp.setStyle("-fx-background-color: #FAFAFA");
        grid.setVgap(5.0);
        grid.setHgap(5.0);
    }

}
