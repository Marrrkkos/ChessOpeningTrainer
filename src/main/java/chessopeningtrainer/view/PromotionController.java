package chessopeningtrainer.view;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Bishop;
import chessopeningtrainer.entity.pieces.Knight;
import chessopeningtrainer.entity.pieces.Queen;
import chessopeningtrainer.entity.pieces.Rook;
import chessopeningtrainer.service.RootService;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class PromotionController implements Refreshable{
        RootService rootService = new RootService();

        boolean inPromotion;
        public PromotionController(boolean inPromotion) {
            this.inPromotion = inPromotion;
        }
        public String writePromotion(int p){
            return switch (p) {
                case 1 -> "=Q";
                case 2 -> "=R";
                case 3 -> "=K";
                case 4 -> "=B";
                default -> "=Q";
            };
        }

    @Override
    public void refreshAfterShowPromotion(Position position1, Position position2, Button[][] buttonArray, boolean colour) {
        Refreshable.super.refreshAfterShowPromotion(position1, position2, buttonArray, colour);
        int posX = position2.getX();
        int posY = position2.getY();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setOpacity(0.2);
            }
        }
        Queen queen = new Queen(colour);
        Rook rook = new Rook(colour, true);
        Bishop bishop = new Bishop(colour);
        Knight knight = new Knight(colour);

        ImageView queenView = new ImageView(queen.getImage());
        ImageView rookView = new ImageView(rook.getImage());
        ImageView bishopView = new ImageView(bishop.getImage());
        ImageView knightView = new ImageView(knight.getImage());

        ImageView[] imageViews = new ImageView[] {queenView, rookView, bishopView, knightView};

        int direction = colour ? 1 : -1;
        for (int i = 0; i < 4; i++) {
            buttonArray[posX][posY + i * direction].setStyle("-fx-background-radius: 90");
            buttonArray[posX][posY + i * direction].setOpacity(1);
            buttonArray[posX][posY + i * direction].setGraphic(imageViews[i]);
        }
        inPromotion = true;
    }

    @Override
    public void refreshAfterPromotion(Position position1, Position position2, Button[][] buttonArray, boolean colour, Board board, boolean legal) {
        Refreshable.super.refreshAfterPromotion(position1, position2, buttonArray, colour, board, legal);

        int posX = position2.getX();
        int posY = position2.getY();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setOpacity(1);
            }
        }

        int direction = colour ? 1 : -1;
        ImageView pieceImage;
        for (int i = 0; i < 4; i++) {
            if(board.getBoard()[posX][posY + i * direction].getPiece() != null) {
                pieceImage = new ImageView(board.getBoard()[posX][posY + i * direction].getPiece().getImage());
                buttonArray[posX][posY + i * direction].setGraphic(pieceImage);
            }else{
                buttonArray[posX][posY + i * direction].setGraphic(null);

            }
            buttonArray[posX][posY + i * direction].setStyle("-fx-background-radius: 0");
        }
        if(legal) {
            buttonArray[position1.getX()][position1.getY()].setGraphic(null);
        }
        inPromotion = false;

    }

}
