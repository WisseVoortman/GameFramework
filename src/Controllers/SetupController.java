package Controllers;

import DAL.TCPConnection;
import Models.GameType;
import Views.LoginView;
import Views.SetupView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


//import Views.SetupView;

public class SetupController {
    private Stage primaryStage;
    private String host;
    private String port;
    private String name;
    private TCPConnection connection;

    public SetupController(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    public Scene InitializeLogin()
    {
        return new LoginView(this).getLoginScene();
    }

    public boolean login(String name, String host, String port)
    {
        if(host.equals("") || port.equals("")) { return false; }
        // Initialize server connection
        connection = TCPConnection.getInstance();
        this.host = host;
        this.port = port;
        try {
            connection.initializeConnection(this.host, Integer.parseInt(this.port));
            connection.start();
        } catch (Exception e) {
            return false;
        }

        this.name = name;
        connection.sentCommand("login " + name);

        return true;
    }

    public String getUserName() {
        return name;
    }

    /**
     * Asks the datacontroller to change its AI boolean
     * @param value true if AI selected, false if not
     */
    public void setAI(boolean value) {
        DataController dataController = DataController.getInstance();
        dataController.setAI(value);

    }

    /**
     * Tells the data contoller to change the gametype
     * @param game the game name
     */
    public void setGameType(String game) {
        DataController dataController = DataController.getInstance();
        switch (game) {
            case "Reversi": {
                dataController.setDatasetType(GameType.Reversi);
                break;
            }
            case "Tic-tac-toe": {
                dataController.setDatasetType(GameType.Tictactoe);
                break;
            }
        }
    }

    /**
     * Asks the datacontroller for either the gamelist or player list depening on the value of type
     * @param type 0 = gamelist, 1 = playerlist
     * @return a list containing the required data
     */
    public List<String> getDataList(int type) {
        List<String> dataList;
        switch(type) {
            case 0: {
                connection.sentCommand("get gamelist");
                break;
            }
            case 1: {
                connection.sentCommand("get playerlist");
                break;
            }
            default: {
                break;
            }
        }
        DataController dataController = DataController.getInstance();
        //give the server time to respond
        try {
            Thread.sleep(20);
        } catch(InterruptedException ex) {
            System.out.println(ex.getMessage());
        }

        switch(type) {
            case 0: {
                dataList = dataController.getGamelist();
                break;
            }
            case 1: {
                dataList = dataController.getPlayerList();
                break;
            }
            default: {
                dataList = new ArrayList<>();
                break;
            }
        }

        return dataList;
    }

    /**
     * Checks the entered player name for spaces
     * @param name the entered player name
     * @return false if the name contains a space, true if not
     */
    public boolean checkName(String name) {
        if(name.equals("")) { return false; }
        for (Character c: name.toCharArray()) {
            if(c.equals(' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the scene on the primary stage
     * @param sceneToSet The scene that needs to be set
     */
    public void setScene(Scene sceneToSet) {
        synchronized (primaryStage) {
            primaryStage.setScene(sceneToSet);
        }
    }

    public Scene InitializeSetupView()
    {
        return new SetupView(this).getSetupScene();
    }
}
