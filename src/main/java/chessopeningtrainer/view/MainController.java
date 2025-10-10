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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * the main interface, so the gui of the chessBoard
 */
public class MainController implements Refreshable{
    RootService rootService = new RootService();
    @FXML
    private Pane overlayPane;
    @FXML
    private GridPane gridPane;

    final int BUTTONSIZE = 100;
    private ImageView dragImageView; // <-- jetzt als Feld

    private final Button[][] buttonArray = new Button[8][8];    // all buttons in chess board

    String m1 = "", m2 = "";                            //move holders
    Position position1 = new Position(-1, 0);   // initialize with impossible Pos
    Position position2 = new Position(-1, 0);
    List<Position> possibleMoves = new ArrayList<>();

    PromotionController promotionController = new PromotionController(false);

    Button previousButton = new Button();
    @FXML
    public void initialize() {
        rootService.addRefreshable(this);
        rootService.addRefreshable(promotionController);

        dragImageView = new ImageView();
        dragImageView.setVisible(false);
        dragImageView.setManaged(false);
        dragImageView.setMouseTransparent(true);
        overlayPane.getChildren().add(dragImageView);
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
                    if(button.getGraphic() != null) {
                        dragImageView.setImage(button.getGraphic() instanceof ImageView iv ? iv.getImage() : null);
                        dragImageView.setVisible(true);
                        dragImageView.toFront();

                        dragImageView.setLayoutX(mouseEvent.getSceneX() - gridPane.getWidth() / 8);
                        dragImageView.setLayoutY(mouseEvent.getSceneY() - gridPane.getHeight() / 8);

                        button.getGraphic().setOpacity(0.4);
                    }
                    handleMousePressed(arr[0], arr[1], button, mouseEvent);
                });
                button.setOnMouseDragged(mouseEvent -> {
                    int x = (int) (mouseEvent.getSceneX() - 50) / 100;
                    int y = (int) (mouseEvent.getSceneY() - 50) / 100;

                    if (possibleMoves.contains(new Position(x, y)) ) {
                        previousButton.setOpacity(1);
                        previousButton = buttonArray[x][y];
                        buttonArray[x][y].setOpacity(0.4);

                    }else{
                        previousButton.setOpacity(1);
                    }

                    dragImageView.setLayoutX(mouseEvent.getSceneX() - gridPane.getWidth() / 8);
                    dragImageView.setLayoutY(mouseEvent.getSceneY() - gridPane.getHeight() / 8);
                });
                button.setOnMouseReleased(mouseEvent -> {
                    if(button.getGraphic() != null) {
                        button.getGraphic().setOpacity(1);
                    }
                    dragImageView.setVisible(false);
                    previousButton.setOpacity(1);
                    handleMouseReleased(arr[0], arr[1], button, mouseEvent);

                });
            }


        }
        rootService.gameService.startGame();
    }

    private void handleMousePressed(int col, int row, Button button, MouseEvent mouseEvent) {

        if(!(position1.getX() == -1)) {
            position2.setPosition(col, row);
            if(!position1.equals(position2) && !possibleMoves.contains(position2)) {
                position1.setPosition(col, row);

                removeHoverListeners(possibleMoves);
                possibleMoves = rootService.moveService.getPossibleMoves(position1);

            }else if(possibleMoves.contains(position2)){
                System.out.println(position1 + " " + position2);
                rootService.moveService.doMove(position1, position2, possibleMoves, false);

                removeHoverListeners(possibleMoves);
                possibleMoves.clear();

                position1.setPosition(-1, 0);

            }
        }else{
            position1.setPosition(col, row);
            System.out.println(position1);

            removeHoverListeners(possibleMoves);
            possibleMoves = rootService.moveService.getPossibleMoves(position1);
        }

    }
    private void handleMouseReleased(int col, int row, Button button, MouseEvent mouseEvent) {
        col = (int)(mouseEvent.getSceneX()-50)/100;
        row = (int)(mouseEvent.getSceneY()-50)/100;

        position2.setPosition(col, row);
        if(!(position1.equals(position2)) && position1.getX() != -1) {
            System.out.println(position1 + " " + position2);
            rootService.moveService.doMove(position1, position2, possibleMoves, false);

            removeHoverListeners(possibleMoves);
            possibleMoves.clear();

            position1.setPosition(-1,0);
        }
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
        Field[][] field = board.getField();

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

    @Override
    public void refreshAfterMoveFinished() {
        Refreshable.super.refreshAfterMoveFinished();
        removeAllPoints();
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

    private void removeHoverListeners(List<Position> possibleMoves){
        previousButton.setOpacity(1);
        for(Position position : possibleMoves){
            buttonArray[position.getX()][position.getY()].setOnMouseEntered(null);
            buttonArray[position.getX()][position.getY()].setOnMouseExited(null);
        }
    }

    @Override
    public void refreshAfterShowPossibleMoves(List<Position> possibleMoves) {
        Refreshable.super.refreshAfterShowPossibleMoves(possibleMoves);
        removeAllPoints();
        for(Position position : possibleMoves){
            gridPane.getChildren().add(drawPoint(position));
            buttonArray[position.getX()][position.getY()].setOnMouseEntered(event -> {
                    buttonArray[position.getX()][position.getY()].setOpacity(0.4);
                    previousButton = buttonArray[position.getX()][position.getY()];
            });
            buttonArray[position.getX()][position.getY()].setOnMouseExited(event -> {
                    buttonArray[position.getX()][position.getY()].setOpacity(1);
            });
        }

    }

    @Override
    public void refreshAfterCastle(Position startPos, Position endPos, Piece king, Piece rook, Board board) {
        Refreshable.super.refreshAfterCastle(startPos, endPos, king, rook, board);

        Field[][] field = board.getField();

        int startX = startPos.getX();
        int startY = startPos.getY();

        int endX = endPos.getX();

        if(startX>endX){        //long castle
            for (int i = 0; i <= startX; i++) {

                if(field[i][startY].getPiece() != null){
                    ImageView pieceImage = new ImageView(field[i][startY].getPiece().getImage());
                    pieceImage.setFitHeight(BUTTONSIZE);
                    pieceImage.setFitWidth(BUTTONSIZE);
                    buttonArray[i][startY].setGraphic(pieceImage);
                }else{
                    buttonArray[i][startY].setGraphic(null);
                }

            }

        }else{          //short castle
            for (int i = startX; i <= 7; i++) {
                System.out.println("1");
                if(field[i][startY].getPiece() != null) {
                    ImageView pieceImage = new ImageView(field[i][startY].getPiece().getImage());
                    pieceImage.setFitHeight(BUTTONSIZE);
                    pieceImage.setFitWidth(BUTTONSIZE);
                    buttonArray[i][startY].setGraphic(pieceImage);
                }else{
                    buttonArray[i][startY].setGraphic(null);
                }
            }
        }
    }

    @Override
    public void refreshAfterEnPassant(Position startPos, Position endPos, Piece piece, Board board) {
        Refreshable.super.refreshAfterEnPassant(startPos, endPos, piece, board);
        int startX = startPos.getX();
        int startY = startPos.getY();

        int endX = endPos.getX();
        int endY = endPos.getY();

        Field[][] field = board.getField();

        if(field[endX][endY].getPiece() != null) {
            ImageView pawnImage = new ImageView(field[endX][endY].getPiece().getImage());
            pawnImage.setFitHeight(BUTTONSIZE);
            pawnImage.setFitWidth(BUTTONSIZE);
            buttonArray[endX][endY].setGraphic(pawnImage);
        }
        if(field[startX][startY].getPiece() == null) {
            buttonArray[startX][startY].setGraphic(null);
        }
        if(field[endX][startY].getPiece() == null) {
            buttonArray[endX][startY].setGraphic(null);
        }
    }
}
