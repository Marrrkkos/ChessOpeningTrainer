package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bishop extends Piece{
    boolean colour;

    public Bishop(boolean colour) {
        this.colour = colour;
    }


    Image WBishop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-bishop.png")));
    Image BBishop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-bishop.png")));
    @Override
    public Image getImage(){
        if (this.colour) {
            return WBishop;
        }else{
            return BBishop;
        }

    }

    @Override
    public List<List<Position>> getBasicPieceMoves(Position currentPiecePosition) {
        return getDiagonalMoves(currentPiecePosition);
    }
    @Override
    public int getID() {
        return 3;
    }
    public boolean getColour() {
        return colour;
    }
}
