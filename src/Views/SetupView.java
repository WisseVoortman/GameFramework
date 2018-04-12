package Views;

import Controllers.DataController;
import Controllers.SetupController;
import DAL.TCPConnection;
import Models.GameType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.controlsfx.control.ToggleSwitch;

import java.util.List;

/**
 * This view is the view in which you can select the game you want to play, and if possible to player you want to challenge.
 */
public class SetupView {
    private SetupController controller;
    private Button start;
    private GridPane playBox;
    private HBox scnBox;
    private String game;
    private boolean hasGames = false;
    private boolean hasPlayers = false;
    private ListView<String> gameListView;
    private ListView<String> playerListView;
    private DataController dataController = DataController.getInstance();
    private TCPConnection connection = TCPConnection.getInstance();
    public SetupView(SetupController controller) {
        this.controller = controller;
    }

    /**
     * Sets up the setup scene
     * The scene is a borderPane
     * The center is the main part with the 3 list (games, player)
     * The right is options part
     * The bottom is the start game button
     * @return the setup scene
     */
    public Scene getSetupScene() {
        BorderPane pane = new BorderPane();

        scnBox = new HBox();
        playBox = new GridPane();

        GridPane optionsBox = new GridPane();
        HBox bottomBox = new HBox();

        gameListView = new ListView();
        playerListView = new ListView();

        scnBox.setMinWidth(400);
        optionsBox.setMinWidth(150);

        scnBox.setPadding(new Insets(20,20,20,20));
        scnBox.setSpacing(10);

        optionsBox.setPadding(new Insets(20,20,20,20));
        optionsBox.setHgap(10);

        bottomBox.setPadding(new Insets(10,10,10,10));
        bottomBox.setAlignment(Pos.CENTER);

        playBox.setVgap(5);
        optionsBox.setVgap(10);

        setupGameList();
        setupSpacer();
        setupPlayerList();
        setupSlider(optionsBox);

        setupAIOption(optionsBox);
        setupOptionPane(optionsBox);

        pane.setCenter(scnBox);
        pane.setRight(optionsBox);
        pane.setBottom(bottomBox);
        //pane.setStyle("-fx-background-color: #FAFAFA");
        pane.setStyle("-fx-background-image: url(\"/Images/water.png\");-fx-background-size: 720, 350;-fx-background-repeat: no-repeat;");

        return new Scene(pane,720,350);

    }

    /**
     * creates the Options menu part of setupView, has listeners for buttons
     * @param pane
     */
    private void setupOptionPane(GridPane pane) {
        Label playerName = new Label(dataController.getPlayerName());
        start = new Button("Quick game");
        Button logout = new Button("Logout");
        Button challenge = new Button("Challenge");
        Label empty = new Label("");
        empty.setPadding(new Insets(0,0,5,0));

        logout.setPrefWidth(99);
        challenge.setPrefWidth(99);
        start.setTooltip(new Tooltip("Play against random player"));
        playerName.setFont(Font.font("Verdana", FontWeight.BOLD, 15));


        start.setOnAction( (ActionEvent e) -> {
            switch(game) {
                case "Tic-tac-toe": {
                    connection.sentCommand("subscribe " + game);
                    break;
                }
                case "Reversi": {
                    connection.sentCommand("subscribe " + game);
                    break;
                }
                default: {
                    System.out.println("This game doesn't exist");
                    start.setDisable(true);
                }
            }
        });
        start.setDisable(true);


        challenge.setOnAction((ActionEvent e) -> {
            if (hasPlayers && hasGames) {
                switch (game) {
                    case "Tic-tac-toe": {
                        connection.sentCommand("challenge " + "\"" + playerListView.getSelectionModel().getSelectedItem()
                                + "\" \"" + gameListView.getSelectionModel().getSelectedItem() + "\"");
                        System.out.println(gameListView.getItems());
                        break;
                    }
                    case "Reversi": {
                        connection.sentCommand("challenge " + "\"" + playerListView.getSelectionModel().getSelectedItem()
                                + "\" \"" + gameListView.getSelectionModel().getSelectedItem() + "\"");
                        System.out.println(gameListView.getItems());
                        break;
                    }
                    default: {
                        System.out.println("challenge failed");
                        start.setDisable(true);
                    }
                }
            } });

        logout.setOnAction((ActionEvent e) -> {
            TCPConnection connection = TCPConnection.getInstance();
            connection.logout();
            controller.setScene(controller.InitializeLogin());
        });

        pane.add(empty,0,0);
        pane.add(playerName,0,1);

        pane.add(start,0,4);
        pane.add(challenge,0,5);
        pane.add(logout,0,6);

    }


    /**
     * Adds the ToggleSwitch for  AI  on/off to the gridpane and sets up the event to handle the selection
     * @param pane The gridpane containing all the options
     */

    private void setupAIOption(GridPane pane) {
        ToggleSwitch aiSwitch = new ToggleSwitch("AI");
        aiSwitch.setSelected(true);
        dataController.setAI(true);
        aiSwitch.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (aiSwitch.isSelected()){
                    dataController.setAI(true);
                }else {
                    dataController.setAI(false);
                }
            }
        });
        pane.add(aiSwitch,0,2);
    }


    /**
     * Simple slider control for difficulty level
     * @param OPane
     */
    private void setupSlider(GridPane OPane){
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(2);
        slider.setValue(1);
        slider.setMinorTickCount(0);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        dataController.setAiDifficulty(1);

        slider.setLabelFormatter(new StringConverter<Double>() {

            public String toString(Double n) {
                if (n < 0.5) return "easy";
                if (n < 1.5) return "medium";
                if (n < 2.5) return "hard";

                return "Expert";
            }

            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "easy":
                        return 0d;
                    case "medium":
                        return 1d;
                    case "hard":
                        return 2d;

                    default:
                        return 2d;
                }
            }
        });
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (slider.getValue() < 0.8){
                    System.out.println("easy");
                    dataController.setAiDifficulty(0);
                }
                if (slider.getValue() > 0.9 && slider.getValue() < 1.4){
                    System.out.println("medium");
                    dataController.setAiDifficulty(1);
                }
                if (slider.getValue() > 1.6){
                    System.out.println("hard");
                    dataController.setAiDifficulty(2);
                }
            }
        });


        OPane.add(slider,0,3);
    }

    /**
     * Adds a VBox containing all the games on the server to the center HBox, uses the setupController to retrieve all the games
     * It also sets up the mouse click event to change the selected game
     */
    private void setupGameList() {
        VBox gameBox = new VBox();
        gameBox.setPadding(new Insets(7,0,0,0));
        scnBox.getStylesheets().add("CSS/SetupStyle.css");
        gameListView.setId("gamesList");
        gameListView.setMinWidth(70.0);
        Label gamesLabel = new Label("    GAMES");
        gamesLabel.setPadding(new Insets(0,0,3,0));
        gamesLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        gameBox.setSpacing(8.0);
        gameListView.getItems().addAll(controller.getDataList(0));

        gameListView.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                 game = gameListView.getSelectionModel().getSelectedItem();

                switch (game) {
                    case "Tic-tac-toe": {
                        dataController.setDatasetType(GameType.Tictactoe);
                        start.setDisable(false);
                        hasGames = true;
                        break;
                    }
                    case "Reversi": {
                        dataController.setDatasetType(GameType.Reversi);
                        start.setDisable(false);
                        hasGames = true;
                        break;
                    }
                    default: {
                        System.out.println("This game is not supported by this application");
                    }
                }
            }
        });

        gameBox.setMinWidth(100);
        gameBox.setPrefWidth(100);
        gameBox.setMaxWidth(100);
        gameBox.getChildren().add(gamesLabel);
        gameBox.getChildren().add(gameListView);
        scnBox.getChildren().add(gameBox);
    }


    /**
     * Similair to setupGameList, retrieves all the players via SetupController, puts them in a BorderPane and adds this to VBox
     */
    private void setupPlayerList() {
        scnBox.getChildren().remove(playBox);
        playerListView.setId("playerList");
        Label player = new Label("PLAYERS");
        player.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        BorderPane stylePane = new BorderPane();
        playBox.getChildren().clear();

        Button refresh = new Button();
        Image refreshPic = new Image("/Images/refresh2.png");
        refresh.setGraphic(new ImageView(refreshPic));
        refresh.setStyle("-fx-background-color: transparent;");

        stylePane.setCenter(player);
        stylePane.setLeft(refresh);
        refresh.setId("refreshButton");

        refresh.setOnAction( (ActionEvent e) -> {
            playerListView.getItems().clear();
            setupPlayerList();
        });

        List<String> players = controller.getDataList(1);
        for (String s : players){
            if (s.equals(dataController.getPlayerName())){

            }else {
                playerListView.getItems().add(s);
            }
        }

        playerListView.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                String player = playerListView.getSelectionModel().getSelectedItem();
                hasPlayers = true;

            }
        });


        playBox.add(stylePane,0,0);
        playBox.add(refresh,0,0);
        playBox.add(playerListView,0,1);
        scnBox.getChildren().add(playBox);
    }

    /**
     * Adds the spacer to the center HBox
     * This spacer makes it so there is some room between the games- and player list
     */
    private void setupSpacer() {
        Region spacer = new Region();
        spacer.setPrefWidth(60);
        scnBox.getChildren().add(spacer);
    }
}

