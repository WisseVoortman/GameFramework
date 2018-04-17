package Controllers;

import AI.AI;
import DAL.TCPConnection;
import Views.GameBoardView;

public class ReversiController extends GameController {

    private DataController dataController;
    private TCPConnection connection;

    public ReversiController() {
        this.dataController = DataController.getInstance();
        this.connection = TCPConnection.getInstance();
    }

    /**
     * Initialize the gameboard by using the datacontroller to set the scene
     */
    public void initializeGame(){
        dataController.setPossibleMoves(new int[64]);
        if(dataController.getPlayerOne()) {
            dataController.setPossibleMoves(updatePossibleMoves(dataController.getData(),1));
        } else {
            dataController.setPossibleMoves(updatePossibleMoves(dataController.getData(),2));
        }
        dataController.setScene(new GameBoardView(this, dataController.getData().length, dataController.getYourTurn()).createBoardScene(dataController.getData()));
    }

    /**
     * Updates the boardview when a new move is made
     * @param move the move that was made
     * @param player the player that made the move
     */
    @Override
    public void update(int move, int player) {
        System.out.println("Updating");
        // update the dataset.
        int[] dataSet = this.calculateMove(move, player, dataController.getData());
        dataController.setData(dataSet);

        // update the possible moves dataset
        int[] possibleMoves;
        if(dataController.getPlayerOne()) {
            possibleMoves = updatePossibleMoves(dataController.getData(),1);
        } else {
             possibleMoves = updatePossibleMoves(dataController.getData(),2);
        }
        dataController.setPossibleMoves(possibleMoves);

        // update the player score
        updateScore();

        dataController.setScene(new GameBoardView(this, dataController.getData().length, dataController.getYourTurn()).createBoardScene(dataController.getData()));
    }

    /**
     * Calculates the board result of a move
     * @param move the move that is made
     * @param player the player that made the move
     * @param newDataSet the board on which the move is made
     * @return an int[] of the new board after the move is made
     */
    public int[] calculateMove(int move, int player, int[] newDataSet){
        //System.out.println("Calculating move");
        // set the selected square tot the players color


        int[] dataSet = new int[newDataSet.length];
        for(int i = 0; i < dataSet.length; i++) {
            dataSet[i] = newDataSet[i];
        }

        dataSet[move] = player;

        // the number of squares next to the move.
        int up = move / 8;
        int left = move % 8;

        int down = 7 - up;
        int right = 7 - left;


        // check upward
        for (int counter = 1; counter <= up; counter++) {
            boolean allowTurning = false;
            if (dataSet[move - (8 * counter)] == 0) {
                break;
            }
            if (dataSet[move - (8 * counter)] == player) {
                allowTurning = true;
            }
            if (allowTurning) {
                for (counter = 1; counter <= up; counter++) {
                    if(dataSet[move - (8 * counter)] == 0 || dataSet[move - (8 * counter)] == player){
                        break;
                    }
                    dataSet[move - (8 * counter)] = player;
                }
                break;
            }
        }

        // check downward
        for (int counter = 1; counter <= down; counter++) {
            boolean allowTurning = false;
            if (dataSet[move + (8 * counter)] == 0) {
                break;
            }
            if (dataSet[move + (8 * counter)] == player) {
                allowTurning = true;
            }
            if (allowTurning) {
                for (counter = 1; counter <= down; counter++) {
                    if(dataSet[move + (8 * counter)] == 0 || dataSet[move + (8 * counter)] == player){
                        break;
                    }
                    dataSet[move + (8 * counter)] = player;
                }
                break;
            }
        }


        // check left
        for (int counter = 1; counter <= left; counter++) {
            boolean allowTurning = false;
            if (dataSet[move - counter] == 0) {
                break;
            }
            if (dataSet[move - counter] == player) {
                allowTurning = true;
            }
            if (allowTurning) {
                for (counter = 1; counter <= left; counter++) {
                    if(dataSet[move - counter] == 0 || dataSet[move - counter] == player){
                        break;
                    }
                    dataSet[move - counter] = player;
                }
                break;
            }
        }

        // check right
        for (int counter = 1; counter <= right; counter++) {
            boolean allowTurning = false;
            if (dataSet[move + counter] == 0) {
                break;
            }
            if (dataSet[move + counter] == player) {
                allowTurning = true;
            }
            if (allowTurning) {
                for (counter = 1; counter <= right; counter++) {
                    if(dataSet[move + counter] == 0 || dataSet[move + counter] == player){
                        break;
                    }
                    dataSet[move + counter] = player;
                }
                break;
            }
        }

        // check up left
        for (int counter = 1; counter <= left && counter <= up; counter++) {
            boolean allowTurning = false;
            if (dataSet[move - (9 * counter)] == 0) {
                break;
            }
            if (dataSet[move - (9 * counter)] == player) {
                allowTurning = true;
            }
            if (allowTurning) {
                for (counter = 1; counter <= left && counter <= up; counter++) {
                    if(dataSet[move - (9 * counter)] == 0 || dataSet[move - (9 * counter)] == player){
                        break;
                    }
                    dataSet[move - (9 * counter)] = player;
                }
                break;
            }
        }

        // check up right
        for (int counter = 1; counter <= right && counter <= up; counter++) {
            boolean allowTurning = false;
            if (dataSet[move - (7 * counter)] == 0) {
                break;
            }
            if (dataSet[move - (7 * counter)] == player) {
                allowTurning = true;
            }
            if (allowTurning) {
                for (counter = 1; counter <= right && counter <= up; counter++) {
                    if(dataSet[move - (7 * counter)] == 0 || dataSet[move - (7 * counter)] == player){
                        break;
                    }
                    dataSet[move - (7 * counter)] = player;
                }
                break;
            }
        }

        // check down left
        for (int counter = 1; counter <= left && counter <= down; counter++) {
            boolean allowTurning = false;
            if (dataSet[move + (7 * counter)] == 0) {
                break;
            }
            if (dataSet[move + (7 * counter)] == player) {
                allowTurning = true;
            }
            if (allowTurning) {
                for (counter = 1; counter <= left && counter <= down; counter++) {
                    if(dataSet[move + (7 * counter)] == 0 || dataSet[move + (7 * counter)] == player){
                        break;
                    }
                    dataSet[move + (7 * counter)] = player;
                }
                break;
            }
        }

        // check down right
        for (int counter = 1; counter <= right && counter <= down; counter++) {
            boolean allowTurning = false;
            if (dataSet[move + (9 * counter)] == 0) {
                break;
            }
            if (dataSet[move + (9 * counter)] == player) {
                allowTurning = true;
            }
            if (allowTurning) {
                for (counter = 1; counter <= right && counter <= down; counter++) {
                    if(dataSet[move + (9 * counter)] == 0 || dataSet[move + (9 * counter)] == player){
                        break;
                    }
                    dataSet[move + (9 * counter)] = player;
                }
                break;
            }
        }
        return dataSet;
    }

    /**
     * Updates the possible moves for a specific player
     * @param dataSet the current board
     * @param player the player for which the possible moves need to be checked
     * @return an int[] of the board where 0 is not possible and 1 is possible
     */
    public int[] updatePossibleMoves(int[] dataSet, int player){
        //System.out.println("Updating Possible Moves");
        int[] possibleMoves = new int[dataSet.length];


        for(int counter = 0; counter < 64; counter ++){
            if(checkPossibleMoves(counter, player, dataSet)){
                possibleMoves[counter] = 1; // move is possible
            }
            else{
                possibleMoves[counter] = 0; // move is not possible
            }
        }

        return possibleMoves;

    }

    /**
     * Checks if a specific move of a specific player is possible
     * @param move the specific move
     * @param player the specific player
     * @param dataSet the current board
     * @return true if possible, false if not
     */
    private boolean checkPossibleMoves(int move, int player, int[] dataSet){
        //System.out.println("Checking Possible Moves");
        if(dataSet[move] != 0){
            return false;
        }

        int opponent = 1;
        if(player == 1){
            opponent = 2;
        }

        int up = move / 8;
        int left = move % 8;

        int down = 7 - up;
        int right = 7 - left;
        boolean possibleRow = false;


        // check upward
        for (int counter = 1; counter <= up; counter++) {

            if (dataSet[move - (8 * counter)] == player) {
                if(possibleRow){
                    return true;
                }
                break;
            }
            if (dataSet[move - (8 * counter)] == 0) {
                break;
            }
            if(dataSet[move - (8 * counter)] == opponent){
                possibleRow = true;
            }
        }

        possibleRow = false;
        // check downward
        for (int counter = 1; counter <= down; counter++) {

            if (dataSet[move + (8 * counter)] == player) {
                if(possibleRow){
                    return true;
                }
                break;
            }
            if (dataSet[move + (8 * counter)] == 0) {
                break;
            }
            if(dataSet[move + (8 * counter)] == opponent){
                possibleRow = true;
            }
        }

        possibleRow = false;
        // check left
        for (int counter = 1; counter <= left; counter++) {
            if (dataSet[move - counter] == player) {
                if(possibleRow){
                    return true;
                }
                break;
            }
            if (dataSet[move - counter] == 0) {
                break;
            }
            if(dataSet[move - counter] == opponent){
                possibleRow = true;
            }
        }

        possibleRow = false;
        // check right
        for (int counter = 1; counter <= right; counter++) {
            if (dataSet[move + counter] == player) {
                if(possibleRow){
                    return true;
                }
                break;
            }
            if (dataSet[move + counter] == 0) {
                break;
            }
            if(dataSet[move + counter] == opponent){
                possibleRow = true;
            }
        }

        possibleRow = false;
        // check up left
        for (int counter = 1; counter <= left && counter <= up; counter++) {
            if (dataSet[move - (9 * counter)] == player) {
                if(possibleRow){
                    return true;
                }
                break;
            }
            if (dataSet[move - (9 * counter)] == 0) {
                break;
            }
            if(dataSet[move - (9 * counter)] == opponent){
                possibleRow = true;
            }
        }

        possibleRow = false;
        // check up right
        for (int counter = 1; counter <= right && counter <= up; counter++) {
            if (dataSet[move - (7 * counter)] == player) {
                if(possibleRow){
                    return true;
                }
                break;
            }
            if (dataSet[move - (7 * counter)] == 0) {
                break;
            }
            if (dataSet[move - (7 * counter)] == opponent){
                possibleRow = true;
            }
        }

        possibleRow = false;
        // check down left
        for (int counter = 1; counter <= left && counter <= down; counter++) {
            if (dataSet[move + (7 * counter)] == player) {
                if(possibleRow){
                    return true;
                }
                break;
            }
            if (dataSet[move + (7 * counter)] == 0) {
                break;
            }
            if (dataSet[move + (7 * counter)] == opponent){
                possibleRow = true;
            }
        }

        possibleRow = false;
        // check down right
        for (int counter = 1; counter <= right && counter <= down; counter++) {
            if (dataSet[move + (9 * counter)] == player) {
                if(possibleRow){
                    return true;
                }
                break;
            }
            if (dataSet[move + (9 * counter)] == 0) {
                break;
            }
            if(dataSet[move + (9 * counter)] == opponent){
                possibleRow = true;
            }
        }
        return false;
    }


    /**
     * Handles a turn, if AI is turned on an instance of AI is made and told to make a move
     */
    @Override
    public void turn() {
        //notify user or start ai.
        boolean AI = dataController.getAI();

        dataController.setScene(new GameBoardView(this, dataController.getData().length, dataController.getYourTurn()).createBoardScene(dataController.getData()));
        if(AI){
            //run AI code.
            AI ai = new AI();
            //System.out.println("AI is made");
            System.out.println("Possible moves");
            int move = ai.makeMove(dataController.getData(), dataController.getPossibleMoves());
            //System.out.println("AI made move " + move);
            /*try {
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }*/
            sentMove(move);
        }
    }

    /**
     * Sents a move tot the server it it is valid
     * @param move the move that needs to be send
     * @return true if succesfull(not your turn anymore) false if not
     */
    @Override
    public boolean sentMove(int move) {
        int[] possibleMoves = dataController.getPossibleMoves();

        if(dataController.getYourTurn()){
            if(possibleMoves[move] == 1){
                dataController.setYourTurn(false);
                this.connection.sentCommand("MOVE " + move);
                System.out.println("Move was supposed to be send");
                //System.out.println(move);
            }
        }

        return !dataController.getYourTurn();
    }


    /**
     * Returns the possible moves
     * @return int[] of possible moves
     */
    public int[] getPossibleMoves(){
        int[] pm = dataController.getPossibleMoves();
        return pm;
    }

    /**
     * Updates the score by counting each players tiles
     */
    public void updateScore(){
        int[] dataSet = dataController.getData();


        int playerOneScore = 0;
        int playerTwoScore = 0;

        for(int counter = 0; counter < 64; counter++){
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
     * Gets the image for the tile
     * @param val 1 for blue, 2 for red
     * @return the path to the image
     */
    @Override
    public String getImage(int val) {
        if (val == 1){
            return "/Images/blue.png";
        }if (val == 2){
            return "/Images/red.png";

        }
        return null;
    }

    /**
     * Gets the username for yourself
     * @return the username
     */
    @Override
    public String getNamePlayer1() {
        return  dataController.getPlayerName();
    }

    /**
     * Gets the username for your opponent
     * @return the username
     */
    @Override
    public String getNameOppenent() {
        return  dataController.getOpponentName();
    }

    /**
     * Gets a boolean whether you're player 1
     * @return true if you're player1, false if not
     */
    @Override
    public boolean getPlayer1() {
        return dataController.getPlayerOne();
    }
}