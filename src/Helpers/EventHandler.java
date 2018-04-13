package Helpers;

import Controllers.DataController;
import Controllers.ReversiController;
import Controllers.SetupController;
import Controllers.TicTacToeController;
import DAL.TCPConnection;
import Models.GameType;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class EventHandler {

    private DataController dataController;

    public EventHandler()
    {
        this.dataController = DataController.getInstance();
    }

    /**
     * Handles the server response
     * If the response is a SVR response it gives it to the SVR handler
     * @param response
     */
    public void HandleCommand(String response)
    {
        if(response == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            return;
        }
        String command = response.split(" ")[0];
        switch(command)
        {
            case "OK" :
                System.out.println("Server accepted command.");
                break;
            case "ERR" :
                System.out.println("Server denied command!");
                break;
            case "SVR" :
                this.SVRHandler(response.substring(4));
                break;
            default:
                break;
        }
    }

    /**
     * Handles all server responses that start with 'SVR'
     * Depending on the second keyword it gives the response to the appropriate handler.
     * @param response The server response
     */
    private void SVRHandler(String response)
    {
        //System.out.println(response);
        String command = response.split(" ")[0];
        if("HELP".equals(command))
        {
            //System.out.println("Server gave help information:");
            System.out.println(response);
        }
        if("PLAYERLIST".equals(command))
        {
            this.PlayerlistHandler(response.substring(11));
        }
        if("GAMELIST".equals(command))
        {
            this.GamelistHandler(response.substring(9));
        }
        if("GAME".equals(command))
        {
            response = response.substring(5);
            command = response.split(" ")[0];
            switch(command)
            {
                case "MATCH" :
                    this.MatchHandler(response.substring(6));
                    //System.out.println("Match started.");
                    break;
                case "YOURTURN" :
                    this.TurnHandler(response.substring(9));
                    break;
                case "MOVE" :
                    this.MoveHandler(response.substring(5));
                    break;
                case "CHALLENGE" :
                    this.ChallengeHandler(response.substring(10));
                    break;
                case "WIN" :
                    this.EndGameHandler(response.substring(4), "WIN");
                    break;
                case "LOSS" :
                    this.EndGameHandler(response.substring(5), "LOSS");
                    break;
                case "DRAW" :
                    this.EndGameHandler(response.substring(5), "DRAW");
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * Handles the playerlist response from the server
     * Adds all the players from the response to the DataController playerlist
     * @param response The server response
     */
    private void PlayerlistHandler(String response)
    {
        response = response.replaceAll(" ", "");
        response = response.substring(1, response.length() - 1);
        dataController.clearPlayerlist();
        for(String name : response.split(","))
        {
            if(name.substring(1,name.length() -1).length() > 0) {
                name = name.substring(1, name.length() - 1);
                dataController.addPlayerlistItem(name);
            }
        }

    }

    /**
     * Handles the game list response from the server
     * Puts all the games from the response in the dataController game list
     * Not much else
     * @param response The server response
     */
    private void GamelistHandler(String response) {
        //System.out.println("In gamelist handler");
        response = response.replaceAll(" ", "");
        response = response.substring(1, response.length() - 1);
        dataController.clearGamelist();
        for(String game : response.split(","))
        {
            game = game.substring(1,game.length() - 1);
            dataController.addGamelistItem(game);
        }
    }

    /**
     * Handles a turn
     * Depending on which game is beïng played it creates a controller for that game
     * and tells it to do a turn
     * @param response The server response
     */
    private void TurnHandler(String response)
    {
        HashMap<String, String> parameters = this.parameterConvert(response);
        dataController.setYourTurn(true);

        GameType type = dataController.getGameType();
        if(type == GameType.Tictactoe)
        {
            TicTacToeController contr = new TicTacToeController();
            contr.turn();
        }
        if(type == GameType.Reversi)
        {
            ReversiController contr = new ReversiController();
            contr.turn();
        }

    }

    /**
     * Handles the starting of a match
     * Sets whether you are player one and if its your turn to start
     * Depening on which game is beïng played the appropiate controller is created and told to initialize the game
     * @param response The server response
     */
    private void MatchHandler(String response)
    {
        HashMap<String, String> parameters = this.parameterConvert(response);
        String playertostart = parameters.get("PLAYERTOMOVE");
        String opponent = parameters.get("OPPONENT");
        boolean Yourturn = true;
        boolean playerOne = true;
        if(playertostart.equals(opponent))
        {
            Yourturn = false;
            playerOne = false;
        }
        System.out.println("Current dataset + " + dataController.getDataSet());
        if(dataController.getDataSet() == null) {
            if(parameters.get("GAMETYPE").equals("Reversi")) {
                dataController.setDatasetType(GameType.Reversi);
            }
        }
        dataController.setYourTurn(Yourturn);
        dataController.setPlayerOne(playerOne);
        dataController.setOpponentName(opponent);

        GameType type = dataController.getGameType();

        if(type == GameType.Tictactoe)
        {
            TicTacToeController contr = new TicTacToeController();
            contr.initializeGame();
        }
        if(type == GameType.Reversi)
        {
            ReversiController contr = new ReversiController();
            contr.initializeGame();
        }

    }

    /**
     * Handlers a single move
     * It checks whether this player is player1 or player2, it then checks which game is being played.
     * Depening on which game is beïng played it makes a appropiate controller and tells it to update
     * the board with the move that has been made and the player that made it
     * @param response
     */
    private void MoveHandler(String response)
    {
        int move, player;

        HashMap<String, String> parameters = this.parameterConvert(response);
        String playerName = parameters.get("PLAYER");
        String opponentName = dataController.getOpponentName();

        if(opponentName.equals(playerName)){
            //opponent has set a move
            player = 1;
            if(dataController.getPlayerOne() ) {
                player = 2;
            }
        } else {
            player = 2;
            if(dataController.getPlayerOne()) {
                player = 1;
            }
        }


        move = Integer.parseInt(parameters.get("MOVE"));
        GameType type = dataController.getGameType();

        if(type == GameType.Reversi){
            ReversiController contr = new ReversiController();
            contr.update(move, player);
        }

        if(type == GameType.Tictactoe){
            TicTacToeController contr = new TicTacToeController();
            contr.update(move, player);
        }
    }

    /**
     * Handles the receiving of a challenge
     * Puts up an alert that can be accepted or rejected
     * When accepted an answer is send to the server and the game is started
     * @param response
     */
    private void ChallengeHandler(String response)
    {
        HashMap<String, String> parameters = parameterConvert(response);
        if(response.substring(0,9).equals("CANCELLED")) {
            return;
        }
        Platform.runLater( () -> {
            synchronized (parameters) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CHALLENGE");
                alert.setHeaderText("Challenged by " + parameters.get("CHALLENGER"));
                alert.setContentText(parameters.get("CHALLENGER") + " wants to play " + parameters.get("GAMETYPE") + " with you!");
                Optional<ButtonType> input = alert.showAndWait();
                if(input.get() == ButtonType.OK) {
                    //start match
                    TCPConnection connection = TCPConnection.getInstance();
                    switch (parameters.get("GAMETYPE")) {
                        case "Tic-tac-toe": {
                            dataController.setDatasetType(GameType.Tictactoe);
                            break;
                        }
                        case "Reversi": {
                            dataController.setDatasetType(GameType.Reversi);
                            break;
                        }
                    }
                    connection.sentCommand("challenge accept " + parameters.get("CHALLENGENUMBER"));
                }
            }
            });
    }

    /**
     * Handles the end of a game whether it be Draw/Win or Lose
     * Displays a pop up that redirects back to the setup screen
     * @param response The server response
     * @param state win/lose or draw
     */
    private void EndGameHandler(String response, String state)
    {
        String headerText = "";
        String contentText = "" ;
        DataController.getInstance().setDatasetType(GameType.Reversi);

        switch(state)
        {
            case "WIN":
                headerText = "Congratulations!";
                contentText = "You have won!";
                break;
            case "DRAW":
                headerText = "Better luck next time";
                contentText = "At least you did not lose";
                break;
            case "LOSS":
                headerText = "Noob";
                contentText = "You have lost!";
                break;
        }
        ArrayList<String> alertText = new ArrayList<>();
        alertText.add(headerText);
        alertText.add(contentText);

        Platform.runLater( () -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("GAME STATUS");
            alert.setHeaderText(alertText.get(0));
            alert.setContentText(alertText.get(1));
            Optional<ButtonType> input = alert.showAndWait();

            if(input.get() == ButtonType.OK ) {
                SetupController setupController = new SetupController(dataController.getPrimaryStage());
                dataController.setScene(setupController.InitializeSetupView());
            }
        });
    }

    /**
     * Converts the response of the server to a hashmap
     * @param parameters The response of the server
     * @return A hashmap<String,String> containing the Parameters => values
     */
    private HashMap<String, String> parameterConvert(String parameters)
    {
        parameters = parameters.replaceAll(" ", "");
        parameters = parameters.substring(1, parameters.length() -1);
        String[] params = parameters.split(",");

        HashMap<String, String> parameterValues = new HashMap<>();

        for(String param : params)
        {
            String[] keyvaluepair = param.split(":");
            String key = keyvaluepair[0];
            String value = keyvaluepair[1].substring(1, keyvaluepair[1].length() - 1);
            parameterValues.put(key, value);
        }

        return parameterValues;

    }
}
