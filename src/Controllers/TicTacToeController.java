package Controllers;

import DAL.TCPConnection;
import Views.GameBoardView;


public class TicTacToeController extends GameController {

    private DataController dataController;
    private TCPConnection connection;


    public TicTacToeController() {
        this.dataController = DataController.getInstance();
        this.connection = TCPConnection.getInstance();
    }

    /**
     * Initialize the gameboard by using the datacontroller to set the scene
     */
    public void initializeGame() {

        //dataController.setDatasetType(GameType.Tictactoe);

        dataController.setPossibleMoves(new int[]{1,1,1,1,1,1,1,1,1});
        dataController.setScene(new GameBoardView(this, dataController.getData().length, dataController.getYourTurn()).createBoardScene(dataController.getData()));
    }

    /**
     * Updates the screen with a new board
     */
    @Override
    public void turn() {
        dataController.setScene(new GameBoardView(this, dataController.getData().length, dataController.getYourTurn()).createBoardScene(dataController.getData()));
    }


    /**
     * returns the possible moves
     * @return the possible moves in an int array
     */
    @Override
    public int[] getPossibleMoves() {
        int[] pm = dataController.getPossibleMoves();
        return pm;
    }

    /**
     * sents the move to the server. First checks if it is this player' turn
     * @param move the move
     * @return true if the move was send, false otherwise
     */
    @Override
    public boolean sentMove(int move) {
        int[] pm = dataController.getPossibleMoves();
        if(dataController.getYourTurn()){
            if(pm[move] == 1){
                dataController.setYourTurn(false);
                this.connection.sentCommand("MOVE " + move);
            }
        }

        return !dataController.getYourTurn();
    }

    /**
     * Updates the game, sets the new dataset, updates the possible moves, updates the score and updates the boardview
     * @param move the move that was made
     * @param player the player that made the move
     */
    @Override
    public void update(int move, int player) {
        int[] dataSet = dataController.getData();
        dataSet[move] = player;

        dataController.setData(dataSet);

        int[] possibleMoves = new int[dataSet.length];

        for(int i = 0; i < dataSet.length; i++){
            possibleMoves[i] = 1;
            if(dataSet[i] != 0){
                possibleMoves[i] = 0 ;
            }
        }

        dataController.setPossibleMoves(possibleMoves);

        updateScore();
        dataController.setScene(new GameBoardView(this, dataController.getData().length, dataController.getYourTurn()).createBoardScene(dataSet));
    }

    /**
     * Updates the score by counting the player and opponent tiles
     */
    public void updateScore(){
        int[] dataSet = dataController.getData();

        int playerOneScore = 0;
        int playerTwoScore = 0;

        for(int counter = 0; counter < 9; counter++){
            if(dataSet[counter] == 1){
                playerOneScore++;
            }
            else if(dataSet[counter] == 2){
                playerTwoScore++;
            } else{
                // do nothing
            }
        }
        dataController.setScore(playerOneScore, playerTwoScore);
    }

    /**
     * gets the appropiate image, red or blue tile
     * @param val 1 for red, 2 for blue
     * @return the path to the image
     */
    @Override
    public String getImage(int val) {
        if (val == 1){
            return "/Images/redCircle.png";
        }if (val == 2){
            return "/Images/blueX.png";

        }
        return null;
    }

    /**
     * Gets the username from player1
     * @return the name
     */
    @Override
    public String getNamePlayer1() {
        return  dataController.getPlayerName();
    }

    /**
     * Gets the username from player2
     * @return the name
     */
    @Override
    public String getNameOppenent() {
        return  dataController.getOpponentName();
    }

    /**
     * Returns true if you're player1
     * @return
     */
    @Override
    public boolean getPlayer1() {
        return dataController.getPlayerOne();
    }
}

