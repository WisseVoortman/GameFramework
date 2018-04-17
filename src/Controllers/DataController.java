package Controllers;

import Models.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;


/***
 *  SingleTon class for handling all dataObjects for the application.
 */
public class DataController {
    private Stage primaryStage;
    private int aiDifficulty;

    /***
     *  SingleTon instancie for the data controller
     */
    private static DataController ourInstance = new DataController();

    /***
     * Static method for getting the instance
     * @return DataController
     */

    public static DataController getInstance() {
        return ourInstance;
    }


    /***
     *  generic Game datasets
     */
    private Dataset dataset;
    private PossibleMovesDataset pmDataset;
    private UserData userData;

    /***
     * Initializing the class
     */

    private DataController() {
        pmDataset = new PossibleMovesDataset();
        userData = new UserData();
    }


    /***
     * Sets the DatasetType and creates the corresponding dataset.
     * This method is required to start a game!
     * @param type gametype
     */

    public void setDatasetType(GameType type)
    {
        if(type == GameType.Reversi) {
            System.out.println("Setting dataset type to reversi");
            this.dataset = new ReversiDataset();
            this.dataset.gameType = type;
        }

        if(type == GameType.Tictactoe) {
            this.dataset = new TicTacToeDataset();
            this.dataset.gameType = type;
        }
        this.dataset.yourTurn = false;
    }

    /***
     * Saves the data of the game.
     * @param boardData game data
     */

    public void setData(int[] boardData)
    {
        //System.out.println("Setting data in dataController");
        this.dataset.gameData = boardData;
    }

    public void setScore(int playerOneScore, int playerTwoScore){
        this.dataset.playerOneScore = playerOneScore;
        this.dataset.playerTwoScore = playerTwoScore;
    }

    /**
     * Gets the score for player one
     * @return the score
     */
    public int getPlayerOneScore(){
        return this.dataset.playerOneScore;
    }

    /**
     * Gets the score for player two
     * @return the score
     */
    public int getPlayerTwoScore(){
        return this.dataset.playerTwoScore;
    }


    /***
     * Saves the option for using the AI
     * @param AI true if AI is enabled, false if not
     */

    public void setAI(boolean AI)
    {
        this.userData.AI = AI;
    }

    /**
     * Sets the ai difficulty
     * @param difficulty the difficulty of the AI
     */
    public void setAiDifficulty(int difficulty) {
        aiDifficulty = difficulty;
    }


    /***
     * Saves the possible moves after these are calculated.
     * @param boardData the possible moves
     */

    public void setPossibleMoves(int[] boardData)
    {
        this.pmDataset.Dataset = boardData;
    }


    /***
     * Saves yourturn boolean.
     * @param turn true if it is your turn, false if not
     */
    public void setYourTurn(boolean turn)
    {
        this.dataset.yourTurn = turn;
    }

    /**
     * Sets who is player one
     * @param playerOne true if you're player one, false if not
     */
    public void setPlayerOne(boolean playerOne) { this.dataset.playerOne = playerOne; }

    /**
     * Sets the player name
     * @param name the player name
     */
    public void setPlayerName(String name) { this.userData.PlayerName = name; }

    /**
     * Sets the opponent name
     * @param name the opponent name
     */
    public void setOpponentName(String name) { this.userData.OpponentName = name; }

    /**
     * Adds a game to the game list
     * @param game the game to be added
     */
    public void addGamelistItem(String game)
    { this.userData.gamelist.add(game); }

    /**
     * Adds a player to the player list
     * @param name the player to be added
     */
    public void addPlayerlistItem(String name) { this.userData.playerlist.add(name); }

    /***
     * Returns the game data
     * @return Game data
     */

    public int[] getData()
    {
        return this.dataset.gameData;
    }

    /**
     * Gets the dataset
     * @return the dataset
     */
    public Dataset getDataSet() {return dataset;}


    /***
     * Returns the game type
     * @return GameType
     */
    public GameType getGameType()
    {
        return this.dataset.gameType;
    }


    /***
     * Returns AI option
     * @return AI
     */
    public boolean getAI()
    {
        return this.userData.AI;
    }

    /**
     * Returns AI difficulty
     * @return AI difficulty
     */
    public int getAiDifficulty() { return aiDifficulty; }


    /***
     * Returns the possible moves dataset
     * @return Possible moves
     */
    public int[] getPossibleMoves()
    {
        return this.pmDataset.Dataset;
    }


    /***
     * Return Yourturn boolean
     * @return Yourturn
     */
    public boolean getYourTurn()
    {
        return this.dataset.yourTurn;
    }

    /**
     * Gets the boolean player one
     * @return true if you're player one, false if not
     */
    public boolean getPlayerOne() { return this.dataset.playerOne; }

    /**
     * Gets the player username
     * @return the player name
     */
    public String getPlayerName() { return this.userData.PlayerName; }

    /**
     * Gets the opponent username
     * @return the opponent name
     */
    public String getOpponentName() { return this.userData.OpponentName; }

    /**
     * Gets the game list from userdata
     * @return the game list
     */
    public List<String> getGamelist() { return this.userData.gamelist; }

    /**
     * Gets the player list from userdata
     * @return the player list
     */
    public List<String> getPlayerList() { return this.userData.playerlist; }

    /**
     * Empties the gamelist from userdata
     */
    public void clearGamelist() { this.userData.gamelist.clear(); }

    /**
     * Empties the playerlist from userdata
     */
    public void clearPlayerlist() { this.userData.playerlist.clear(); }

    /**
     * Sets the primary stage
     * @param primaryStage the stage to be set as primary stage
     */
    public void setPrimayStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    /**
     * Gets the primary stage
     * @return the primary stage
     */
    public Stage getPrimaryStage() { return primaryStage;}

    /**
     * Sets the scene on the primary stage
     * @param scene the scene to be set
     */
    public void setScene(Scene scene) {
        Platform.runLater( () -> {primaryStage.setScene(scene);});
    }

    /**
     * Gets the current scene
     * @return the scene
     */
    public Scene getScene(){ return primaryStage.getScene(); }
}
