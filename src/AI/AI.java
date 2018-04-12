package AI;

import Controllers.DataController;
import Controllers.ReversiController;

import java.util.*;

public class AI {
    private DataController dataController;
    private int totalTiles;
    public AI(ReversiController controller) {
        this.dataController = DataController.getInstance();
        totalTiles = 0;
    }

    /**
     * Makes a move depending on the AI difficulty, 0 for a random move, 1 for a max priority move, 2 for a hardMove(C)
     * @param dataset
     * @param pMoves
     * @return
     */
    public int makeMove(int[] dataset, int[] pMoves) {
        int move;
        switch (dataController.getAiDifficulty()) {
            case 0: {
                move = randomMove(pMoves);
                break;
            }
            case 1: {
                ArrayList<Integer> moves = maxPrioMove(dataset, getPriotitymoves(pMoves));
                move = moves.get(new Random().nextInt(moves.size()));
                break;
            }
            case 2:{
                move = hardMove(dataset, getPriotitymoves(pMoves));
                break;
            }
            default: {
                move = -1;
                System.out.println("Not a difficulty option");
                break;
            }
        }
        return move;
    }



    /**
     * Chooses a random move from all the posible Moves
     * Checks for each move if it is possible, if it is it will save the index from that move
     * It chooses a random index from the saved indexes
     * It returns the move associated with the random index
     * @param possibleMoves an int[] from the board where 0 = not possible, 1 = possible
     * @return int move The index on the board that is the move made
     */
    private int randomMove(int[] possibleMoves) {
        //System.out.println("AI is making random move");
        Random random = new Random();
        ArrayList<Integer> moves = new ArrayList<>(possibleMoves.length);
        for(int i = 0; i < possibleMoves.length; i++) {
            if(possibleMoves[i] == 1) {
                //System.out.println("Empty cell found: " + i);
                moves.add(i);
            }
        }
        moves.trimToSize();
        //System.out.println(moves.size());
        int index = random.nextInt(moves.size());
        //System.out.println("Random index: " + index);
        int move = moves.get(index);

        //System.out.println("Made up move: " + move);
        return move;
    }


    

    /**
     * Combines the number of tiles gained from a move and the priority of the move, the move with the lowest score (low priority is good)
     * is made, if there are more moves that have the same score a random move is picked from those options
     * @param dataset
     * @param priorityMoves
     * @return
     */
    
     public int hardMove(int[] dataset ,HashMap<Integer, ArrayList<Integer>> priorityMoves){
        ArrayList<Integer> moves = new ArrayList<>();


        // check if hardAV move is good
        moves.addAll(this.checkHardAv(dataset,priorityMoves));
        if(moves.size() > 0){
            Random r = new Random();
            int index = r.nextInt(moves.size());
            return moves.get(index);
        }

         // check if hardAV move is good
         moves.addAll(this.checkSoftAv(dataset,priorityMoves));
         if(moves.size() > 0){
             Random r = new Random();
             int index = r.nextInt(moves.size());
             return moves.get(index);
         }


        if(this.getTotalTiles(dataset) < 32 && this.aheadOfOpponent(dataset)){
            // try blocking the opponent.
            moves.addAll(blockingMove(dataset, priorityMoves));

        }
        // get max tiles based on priority
        moves.addAll(this.maxPrioMove(dataset,priorityMoves));

        Random r = new Random();
        int index = r.nextInt(moves.size());
        return moves.get(index);

    }

    private ArrayList<Integer> blockingMove(int[] dataset ,HashMap<Integer, ArrayList<Integer>> priorityMoves) {
        ReversiController controller = new ReversiController();
        ArrayList<Integer> moves = new ArrayList<>();
        if(priorityMoves.get(4).size() > 0) {
            moves = priorityMoves.get(4);
        } else if(priorityMoves.get(3).size() > 0) {
            moves = priorityMoves.get(3);

        } else if(priorityMoves.get(2).size() > 0) {
            moves = priorityMoves.get(2);

        } else if(priorityMoves.get(1).size() > 0) {
            moves = priorityMoves.get(1);

        } else if(priorityMoves.get(0).size() > 0) {
            moves = priorityMoves.get(0);
        }

        ArrayList<Integer> blockingMoves = new ArrayList<>();
        int prefPrio = 0;

        int player = 2;
        int opponent = 1;

        if(dataController.getPlayerOne()) {
            player = 1;
            opponent = 2;
        }

        for(int move : moves) {
            int[] newBoard = controller.calculateMove(move, player, dataset);
            int[] opponentPm = controller.updatePossibleMoves(newBoard, opponent);
            int amount = 0;
            int priosum = 0;
            if(getPriotitymoves(opponentPm).get(4).size() > 0){
                // this move gives opponent a corner.
                continue;
            }

            priosum += getPriotitymoves(opponentPm).get(0).size();
            priosum += getPriotitymoves(opponentPm).get(1).size() * 1;
            priosum += getPriotitymoves(opponentPm).get(2).size() * 2;
            priosum += getPriotitymoves(opponentPm).get(3).size() * 3;

            amount += getPriotitymoves(opponentPm).get(0).size();
            amount += getPriotitymoves(opponentPm).get(1).size();
            amount += getPriotitymoves(opponentPm).get(2).size();
            amount += getPriotitymoves(opponentPm).get(3).size();

            // no moves for opponent
            if(amount == 0){
                blockingMoves.clear();
                blockingMoves.add(move);
                return blockingMoves;
            }

            int prio = priosum / amount;

            if(prio < prefPrio){
                blockingMoves.clear();
                blockingMoves.add(move);
            }

            if(prio == prefPrio){
                blockingMoves.add(move);
            }
        }

        return blockingMoves;

    }


    private ArrayList<Integer> maxPrioMove(int[] dataset ,HashMap<Integer, ArrayList<Integer>> priorityMoves) {
        ReversiController controller = new ReversiController();

        ArrayList<Integer> doableMoves = new ArrayList<>();

        int player = 2;
        int opponent = 1;

        if(dataController.getPlayerOne()) {
            player = 1;
            opponent = 2;
        }

        int prevPlayerTiles = 0;
        double prevMovePoint = 0;

        for(Map.Entry entrySet : priorityMoves.entrySet()) {
            int prio = (int) entrySet.getKey();

            ArrayList<Integer> moves = (ArrayList) entrySet.getValue();
            for(int move : moves) {
                int playerTiles = 0;
                int opponentTiles = 0;

                int[] newBoard = controller.calculateMove(move, player, dataset);
                for (int cell : newBoard) {
                    if (cell == player) {
                        playerTiles++;
                    } else if (cell == opponent) {
                        opponentTiles++;
                    }
                }
                double movePoint = 0.0;
                double prioWorth = 0.85;
                double tileWorth = 0.15;
                if(prio != 0) {
                    if(prio == 4) {
                        movePoint = (0.95 * prio) + (0.05 * playerTiles);
                    }
                    movePoint = (prioWorth * prio) + (tileWorth * playerTiles);
                } else {
                    movePoint = tileWorth * playerTiles;
                }
                totalTiles += playerTiles;
                //System.out.println("Total Tiles: " + totalTiles);

                if (movePoint > prevMovePoint) {
                    prevMovePoint = movePoint;
                    doableMoves.clear();
                    doableMoves.add(move);
                } else if (movePoint == prevMovePoint) {
                    doableMoves.add(move);
                }
            }
        }

        return doableMoves;
    }

    private ArrayList<Integer> checkHardAv(int[] dataset, HashMap<Integer, ArrayList<Integer>> priorityMoves){
        ArrayList<Integer> moves = new ArrayList<>();
        ArrayList<Integer> avoid = this.getHardDiagonalAvoidance();
        if(priorityMoves.get(0).size() > 0){
            ArrayList<Integer> hardAv = priorityMoves.get(0);
            for(int pmmove : hardAv){
                if(avoid.contains(pmmove)){
                    continue;
                }

                HashMap<Integer, ArrayList<Integer>> opponentPriorityMoves = this.getPriotitymoves(this.calculateOpponentmoves(dataset, pmmove));
                if(opponentPriorityMoves.get(4).size() == 0){
                    moves.add(pmmove);
                }
            }
        }
        return moves;
    }

    private ArrayList<Integer> checkSoftAv(int[] dataset, HashMap<Integer, ArrayList<Integer>> priorityMoves) {
        ArrayList<Integer> moves = new ArrayList<>();
        ReversiController controller = new ReversiController();
        boolean equal = false;
        int player = 2;
        int opponent = 1;

        if(dataController.getPlayerOne()){
            player = 1;
            opponent = 2;
        }

        HashMap<Integer, ArrayList<Integer>> opponentmoves = this.getPriotitymoves(controller.updatePossibleMoves(dataset, opponent));
        int opponentprio1moves = opponentmoves.get(1).size();

        for(int move : priorityMoves.get(3)) {
            HashMap<Integer, ArrayList<Integer>> newmoves = this.getPriotitymoves(controller.updatePossibleMoves(controller.calculateMove(move, player, dataset), opponent));
            if(newmoves.get(1).size() > opponentprio1moves){
                continue;
            }
            if(newmoves.get(1).size() < opponentprio1moves){
                if(equal){
                    moves.clear();
                    equal = false;
                }
                moves.add(move);
            }
            if(newmoves.get(1).size() == opponentprio1moves){
                moves.add(move);
                equal = true;
            }
        }

        return moves;
    }


    private int predictOpponent(int[] board, int[] possibleMoves, int opponent) {
        int move = -1;

        int numTiles = 0;
        int prevNumTiles = 0;

        ReversiController controller = new ReversiController();
        for(int i = 0; i< possibleMoves.length; i++) {
            if(possibleMoves[i] == 1) {
                int[] newBoard = controller.calculateMove(i,opponent,board);
                for(int a = 0; a< newBoard.length; a++) {
                    if(newBoard[a] == opponent) {
                        numTiles++;
                    }
                }
                if(numTiles > prevNumTiles) {
                    prevNumTiles = numTiles;
                    move = i;
                }
            }
        }

        return move;
    }

    private boolean aheadOfOpponent(int[] dataset){
        int player = 2;
        int opponent = 1;

        if(dataController.getPlayerOne()){
            player = 1;
            opponent = 2;
        }

        int playerTiles = 0;
        int opponentTiles = 0;

        for(int data : dataset){
            if(data == 0){
                continue;
            }
            if(data == player){
                playerTiles++;
            }
            if(data == opponent){
                opponentTiles++;
            }
        }

        if(playerTiles > opponentTiles ){
            return true;
        }

        return false;
    }

    private int getTotalTiles(int[] board){
         int totaltiles = 0;
         for (int move : board){
             if(move != 0){
                 totaltiles++;
             }
         }
         return totaltiles;
    }

    private HashMap<Integer, ArrayList<Integer>> getPriotitymoves(int[] pmdataset){
        HashMap<Integer, ArrayList<Integer>> priomoves = new HashMap<>();
        ArrayList<Integer> cornerMoves = new ArrayList<>();
        ArrayList<Integer> sideMoves = new ArrayList<>();
        ArrayList<Integer> softAvMoves = new ArrayList<>();
        ArrayList<Integer> hardAvMoves = new ArrayList<>();
        ArrayList<Integer> restMoves = new ArrayList<>();


        for(int i = 0; i < pmdataset.length; i++){
            if(pmdataset[i] == 0){
                continue;
            }

            if(getCorners().contains(i)){
                cornerMoves.add(i);
            }
            else if(getSides().contains(i)){
                sideMoves.add(i);
            }
            else if(getSoftAvoidance().contains(i)){
                softAvMoves.add(i);
            }
            else if(getHardAvoidance().contains(i)){
                hardAvMoves.add(i);
            }
            else{
                restMoves.add(i);
            }
        }

        priomoves.put(4,cornerMoves);
        priomoves.put(3,sideMoves);
        priomoves.put(2,restMoves);
        priomoves.put(1,softAvMoves);
        priomoves.put(0,hardAvMoves);

        return priomoves;
    }

    private int[] calculateOpponentmoves(int[] board, int move){
        int player = 2;
        int opponent = 1;

        if(dataController.getPlayerOne()){
            player = 1;
            opponent = 2;
        }

        ReversiController controller = new ReversiController();
        int[] newboard = controller.calculateMove(move, player, board);

        return controller.updatePossibleMoves(newboard, opponent);
    }

    private int getplayer(){
        int player = 2;
        if(dataController.getPlayerOne()){
            player = 1;
        }
        return player;
    }

    private int getopponent(){
        int opponent = 1;
        if(dataController.getPlayerOne()){
            opponent = 2;
        }
        return opponent;
    }


    private ArrayList<Integer> getCorners() {
        ArrayList<Integer> corners = new ArrayList<>();
        corners.add(0);
        corners.add(7);
        corners.add(56);
        corners.add(63);
        return corners;
    }

    private ArrayList<Integer> getSides() {
        ArrayList<Integer> sides = new ArrayList<>();
        sides.add(2);
        sides.add(3);
        sides.add(4);
        sides.add(5);
        sides.add(16);
        sides.add(24);
        sides.add(32);
        sides.add(40);
        sides.add(23);
        sides.add(31);
        sides.add(39);
        sides.add(47);
        sides.add(58);
        sides.add(59);
        sides.add(60);
        sides.add(61);
        return sides;
    }

    private ArrayList<Integer> getSoftAvoidance() {
        ArrayList<Integer> softAvoidance = new ArrayList<>();
        softAvoidance.add(10);
        softAvoidance.add(11);
        softAvoidance.add(12);
        softAvoidance.add(13);
        softAvoidance.add(17);
        softAvoidance.add(25);
        softAvoidance.add(33);
        softAvoidance.add(41);
        softAvoidance.add(22);
        softAvoidance.add(30);
        softAvoidance.add(38);
        softAvoidance.add(46);
        softAvoidance.add(50);
        softAvoidance.add(51);
        softAvoidance.add(52);
        softAvoidance.add(53);
        return softAvoidance;
    }

    private ArrayList<Integer> getHardAvoidance() {
        ArrayList<Integer> hardAvoidance = new ArrayList<>();
        hardAvoidance.add(1);
        hardAvoidance.add(8);
        hardAvoidance.add(9);
        hardAvoidance.add(6);
        hardAvoidance.add(14);
        hardAvoidance.add(15);
        hardAvoidance.add(48);
        hardAvoidance.add(49);
        hardAvoidance.add(57);
        hardAvoidance.add(54);
        hardAvoidance.add(55);
        hardAvoidance.add(62);
        return hardAvoidance;
    }

    private ArrayList<Integer> getHardDiagonalAvoidance(){
        ArrayList<Integer> hardAvoidance = new ArrayList<>();
        hardAvoidance.add(9);
        hardAvoidance.add(14);
        hardAvoidance.add(49);
        hardAvoidance.add(54);
        return hardAvoidance;
    }
}
