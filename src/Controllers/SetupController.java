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
        connection = TCPConnection.getInstance();
        if(host.equals("") || port.equals("")) { return false; }
        // Initialize server connection
        this.host = host;
        this.port = port;
        try {
            connection.initializeConnection(this.host, Integer.parseInt(this.port));
        } catch (Exception e) {
            System.out.println("Could not connect");
            return false;
        }
        connection.start();


        this.name = name;
        connection.sentCommand("login " + name);
        DataController.getInstance().setPlayerName(name);

        return true;
    }

    public String getUserName() {
        return DataController.getInstance().getPlayerName();
    }


    /**
     * Asks the datacontroller for either the gamelist or player list depening on the value of type
     * @param type 0 = gamelist, 1 = playerlist
     * @return a list containing the required data
     */
    public List<String> getDataList(int type) {
        connection = TCPConnection.getInstance();
        List<String> dataList;
        switch(type) {
            case 0: {
                //System.out.println("Connection: " + connection);
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
            if(c.equals(' ') || c.equals('\"')) {
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
        DataController.getInstance().setDatasetType(GameType.Reversi);
        synchronized (primaryStage) {
            primaryStage.setScene(sceneToSet);
            DataController dataController = DataController.getInstance();
            dataController.setPrimayStage(primaryStage);
            dataController.setScene(sceneToSet);
        }
    }

    public Scene InitializeSetupView()
    {
        return new SetupView(this).getSetupScene();
    }
}
