package Views;

import Controllers.DataController;
import Controllers.GameController;
import DAL.TCPConnection;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GameBoardView{

    private GameController controller;
    private int boardSize;
    boolean turn;
    private ArrayList<Tile> board;


    public GameBoardView(GameController controller,int boardSize,boolean turn) {
        this.controller = controller;
        this.boardSize = boardSize;
        this.turn = turn;

    }


    public Scene createBoardScene(int[] dataSet){
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new TopPane(turn));
        borderPane.setBottom(new BottomPane());
        CenterPane centerPane = new CenterPane(boardSize,controller, dataSet);
        board = centerPane.getBoard();
        borderPane.setCenter(centerPane);
        borderPane.setLeft(new LeftPane(controller));
        borderPane.setRight(new RightPane(controller));
        return new Scene(borderPane,1000,900);
    }
}


class BottomPane extends HBox {

    // private Label serverMessage;

    public BottomPane() {
        setAlignment(Pos.CENTER);
        setForfeit();
        setLayout();
    }

    private void setForfeit() {
        Button forfeit = new Button("Give up");
        forfeit.setEffect(new DropShadow());
        forfeit.setOnAction((ActionEvent e) -> {
            TCPConnection connection = TCPConnection.getInstance();
            connection.sentCommand("forfeit");
        });
        getChildren().add(forfeit);
    }

    private void setLayout(){
        setStyle("-fx-border-color: black");
        setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
    }
}

class TopPane extends HBox {

    private Label labelTurn;
    private boolean turn;

    public TopPane(boolean turn) {
        this.turn = turn;

        setLayout();
        setTurn();

    }

    private void setTurn(){
        labelTurn = new Label();
        labelTurn.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        if (turn){
            labelTurn.setText("Your turn");
        }else {
            labelTurn.setText("Opponents turn");
        }
        getChildren().add(labelTurn);
    }

    private void setLayout(){
        setAlignment(Pos.CENTER);
        setSpacing(5);
        setStyle("-fx-border-color: black");
        setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
    }
}

class CenterPane extends GridPane {

   private int boardSize;
   private Image img;
   private Image img2;


    ArrayList<Tile> board;
    GameController controller;

    public CenterPane(int boardSize, GameController controller, int[] dataSet) {

        this.boardSize = boardSize;
        this.controller = controller;
        boardSize =  (int) Math.sqrt(boardSize);
        drawBoard(boardSize, dataSet);
        //setStylingPane();
        setLayout();

    }

    private void drawBoard(int boardSize, int[] dataset) {
        int count = 0;
        setAlignment(Pos.CENTER);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Tile tile = new Tile(controller,count);
                tile.setTranslateY(i * -12);
                int val = dataset[count];
                Rectangle rec = tile.fillTile();
                if(val == 1) {
                    img = new Image(controller.getImage(val));
                    rec.setFill(new ImagePattern(img));

                } else if(val == 2) {
                    img2 = new Image(controller.getImage(val));
                    rec.setFill(new ImagePattern(img2));
                }
                add(tile, j, i);
                count++;
            }
        }

    }

    public ArrayList<Tile> getBoard() {
        return board;
    }

    private void setLayout(){
        setStyle("-fx-background-color: #FAFAFA");
        setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
    }
}

class LeftPane extends VBox {

    private Label p1;
    DataController dataController = DataController.getInstance();
    private Label score = new Label("" + dataController.getPlayerOneScore());
    private Separator separator = new Separator();

    public LeftPane(GameController controller){
        if (controller.getPlayer1()) {
            String player1 = controller.getNamePlayer1();
            p1 = new Label(player1 + " \n  score");
        }else {
            String oppenent = controller.getNameOppenent();
            p1 = new Label(oppenent + " \n  score");
        }
        setLayout();
        setText();
    }

    private void setText(){
        p1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        score.setFont(Font.font("Verdana", FontWeight.BOLD, 35));

        separator.setOrientation(Orientation.HORIZONTAL);

        getChildren().add(p1);
        getChildren().add(separator);
        getChildren().add(score);
    }

    private void setLayout(){
        setMaxWidth(50.0);
        setAlignment(Pos.TOP_CENTER);
        setSpacing(5.0);
        setStyle("-fx-background-color: #005b96");
        setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
    }
}

class RightPane extends VBox{

    private Label p2;
    DataController dataController = DataController.getInstance();
    private Label score = new Label("" + dataController.getPlayerTwoScore());
    private Separator separator = new Separator();

    public RightPane(GameController controller){
        if (controller.getPlayer1()) {
            //System.out.println("right" + controller.getPlayer1());
            String oppenent = controller.getNameOppenent();
            p2 = new Label(oppenent + " \n  score");
        }else {
            String player1 = controller.getNamePlayer1();
            p2 = new Label(player1 + " \n  score");
        }
        setLayout();
        setText();
    }

    private void setText(){
        p2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        score.setFont(Font.font("Verdana", FontWeight.BOLD, 35));

        separator.setOrientation(Orientation.HORIZONTAL);

        getChildren().add(p2);
        getChildren().add(separator);
        getChildren().add(score);
    }

    private void setLayout(){
        setAlignment(Pos.TOP_CENTER);
        setSpacing(5.0);
        setStyle("-fx-background-color:  #ff4c4c");
        setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
    }

}
class Tile extends StackPane {
    private Text text = new Text();
    private Rectangle border;
    private GameController controller;
    private int index;




    public Tile(GameController controller, int count) {
        this.controller = controller;
        int[] pm = controller.getPossibleMoves();
        index = count;
        border = new Rectangle(80, 80);
        border.setFill(null);
        if (pm[count] == 1 && pm.length > 0) {
            border.setFill(Color.rgb(144,238,144));
        }else {
            border.setFill(null);
        }
        border.setStroke(Color.BLACK);
        text.setFont(Font.font(72));
        setAlignment(Pos.CENTER);
        getChildren().addAll(border, text);

        this.setOnMouseClicked(event -> {
            controller.sentMove(index);
        });
    }

  public Rectangle fillTile(){
      return border;
  }

}