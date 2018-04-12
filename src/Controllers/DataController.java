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
            this.dataset = new ReversiDataset();
            this.dataset.gameType = type;
        }

        if(type == GameType.Tictactoe) {
            //System.out.println("Setting dataset type to tictactoe");
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

    public int getPlayerOneScore(){
        return this.dataset.playerOneScore;
    }

    public int getPlayerTwoScore(){
        return this.dataset.playerTwoScore;
    }


    /***
     * Saves the option for using the AI
     * @param AI
     */

    public void setAI(boolean AI)
    {
        this.userData.AI = AI;
    }

    /**
     * Sets the ai difficulty
     * @param difficulty
     */
    public void setAiDifficulty(int difficulty) {
        aiDifficulty = difficulty;
    }


    /***
     * Saves the possible moves after these are calculated.
     * @param boardData
     */

    public void setPossibleMoves(int[] boardData)
    {
        this.pmDataset.Dataset = boardData;
    }


    /***
     * Saves yourturn boolean.
     * @param turn
     */
    public void setYourTurn(boolean turn)
    {
        this.dataset.yourTurn = turn;
    }

    public void setPlayerOne(boolean playerOne)
    {
        this.dataset.playerOne = playerOne;
    }

    public void setPlayerName(String name)
    {
        this.userData.PlayerName = name;
    }

    public void setOpponentName(String name)
    {
        this.userData.OpponentName = name;
    }

    public void addGamelistItem(String game)
    {
        this.userData.gamelist.add(game);
    }

    public void addPlayerlistItem(String name)
    {
        this.userData.playerlist.add(name);
    }

    /***
     * Returns the game data
     * @return Game data
     */

    public int[] getData()
    {
        return this.dataset.gameData;
    }

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

    public boolean getPlayerOne()
    {
        return this.dataset.playerOne;
    }

    public String getPlayerName()
    {
        return this.userData.PlayerName;
    }

    public String getOpponentName()
    {
        return this.userData.OpponentName;
    }

    public List<String> getGamelist()
    {
        return this.userData.gamelist;
    }

    public List<String> getPlayerList()
    {
        return this.userData.playerlist;
    }

    public void clearGamelist()
    {
        this.userData.gamelist.clear();
    }

    public void clearPlayerlist()
    {
        this.userData.playerlist.clear();
    }

    public void setPrimayStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() { return primaryStage;}

    /**
     * Sets the scene on the primary stage
     * @param scene
     */
    public void setScene(Scene scene) {
        Platform.runLater( () -> {primaryStage.setScene(scene);});
    }

    public Scene getScene(){
        return primaryStage.getScene();
    }
}
