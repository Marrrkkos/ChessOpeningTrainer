package chessopeningtrainer.view;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Field;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Piece;
import chessopeningtrainer.service.RootService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class MainController implements Refreshable{
    RootService rootService = new RootService();
    @FXML
    private GridPane gridPane;
    final int BUTTONSIZE = 100;

    private final Button[][] buttonArray = new Button[8][8];    // all buttons in chess board

    String m1 = "", m2 = "";                            //move holders
    Position position1 = new Position(0, 0);
    Position position2 = new Position(0, 0);
    List<Position> possibleMoves = new ArrayList<>();

    @FXML
    public void initialize() {
        rootService.addRefreshable(this);


        /*ImageView dragImageView = new ImageView(); // This is the dragImageView, which gets to the piece you drag
        dragImageView.setVisible(false);            // Invisible till you drag your own piece
        dragImageView.setManaged(false);
        gridPane.getChildren().add(dragImageView);*/

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {

                Integer row = GridPane.getRowIndex(button);
                Integer col = GridPane.getColumnIndex(button);
                row = (row == null) ? 0 : row;
                col = (col == null) ? 0 : col;
                // save current button in Array
                buttonArray[col][row] = button;
                int[] arr = {col, row};
                button.setOnMousePressed(mouseEvent -> {
                    if(m1.isEmpty()){
                        m1 = button.getId();
                        position1.setPosition(arr[0], arr[1]);
                        possibleMoves = rootService.moveService.getPossibleMoves(position1);
                    }else{

                        m2 = button.getId();
                        position2.setPosition(arr[0], arr[1]);

                        rootService.moveService.doMove(position1, position2, possibleMoves);
                        possibleMoves.clear();
                        m1 = "";
                        m2 = "";
                        removeAllPoints();
                    }
                });
                button.setOnMouseDragged(mouseEvent -> {

                });
                button.setOnMouseReleased(mouseEvent -> {

                });
            }


        }
        rootService.gameService.startGame();
    }
    @Override
    public void refreshAfterNormalMove(Position startPos, Position endPos, Piece piece){
        Refreshable.super.refreshAfterNormalMove(startPos, endPos, piece);

        int startX = startPos.getX();
        int startY = startPos.getY();

        int endX = endPos.getX();
        int endY = endPos.getY();

        ImageView pieceImage = new ImageView(piece.getImage());

        pieceImage.setFitHeight(BUTTONSIZE);
        pieceImage.setFitWidth(BUTTONSIZE);

        buttonArray[endX][endY].setGraphic(pieceImage);
        buttonArray[startX][startY].setGraphic(null);


    }

    @Override
    public void refreshAfterGameStart(Board board) {
        Refreshable.super.refreshAfterGameStart(board);
        Field[][] field = board.getBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                if (field[i][j].getPiece() != null) {
                    ImageView pieceImage = new ImageView(field[i][j].getPiece().getImage());        // initialize Black Pieces
                    pieceImage.setFitHeight(BUTTONSIZE);
                    pieceImage.setFitWidth(BUTTONSIZE);
                    buttonArray[i][j].setGraphic(pieceImage);
                } else {
                    buttonArray[i][j].setGraphic(null);
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 6; j < 8; j++) {
                if (field[i][j].getPiece() != null) {
                    ImageView pieceImage = new ImageView(field[i][j].getPiece().getImage());    // initialize White Pieces
                    pieceImage.setFitHeight(100);
                    pieceImage.setFitWidth(100);
                    buttonArray[i][j].setGraphic(pieceImage);
                } else {
                    buttonArray[i][j].setGraphic(null);
                }
            }
        }
    }
    public void removeAllPoints(){
        gridPane.getChildren().removeIf(node -> node instanceof Circle);
    }
    private Circle drawPoint(Position position){
        Circle circle = new Circle();
        circle.setCenterX(position.getX()*100+50);
        circle.setCenterY(position.getY()*100+50);
        circle.setManaged(false);
        circle.setRadius(12);
        circle.setOpacity(0.8);
        circle.setFill(Color.GRAY);
        circle.setMouseTransparent(true);

        return circle;
    }

    @Override
    public void refreshAfterShowPossibleMoves(List<Position> possibleMoves) {
        Refreshable.super.refreshAfterShowPossibleMoves(possibleMoves);

        for(Position position : possibleMoves){
            gridPane.getChildren().add(drawPoint(position));
        }

    }
}
