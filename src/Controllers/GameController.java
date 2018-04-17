package Controllers;

/**
 * Abstract gamecontroller, has all the methods that a concrete implementation of a game controller needs
 */
public abstract class GameController {
    public abstract void update(int move, int player);
    public abstract void turn();
    public abstract boolean sentMove(int move);
    public abstract int[] getPossibleMoves();
    public abstract void updateScore();
    public abstract String getImage(int val);
    public abstract String getNamePlayer1();
    public abstract String getNameOppenent();
    public abstract boolean getPlayer1();

}
