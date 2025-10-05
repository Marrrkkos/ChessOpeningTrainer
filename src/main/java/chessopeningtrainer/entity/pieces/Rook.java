package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rook extends Piece{
    boolean colour;
    boolean hasMoved;
    public Rook(boolean colour, boolean hasMoved) {
        this.colour = colour;
        this.hasMoved = hasMoved;
    }

    Image WRook = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-rook.png")));
    Image BRook = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-rook.png")));
    @Override
    public Image getImage(){
        if (this.colour) {
            return WRook;
        }else{
            return BRook;
        }

    }

    @Override
    public List<List<Position>> getBasicPieceMoves(Position currentPiecePosition) {
        return getStraightMoves(currentPiecePosition);
    }
    public boolean getColour() {
        return colour;
    }
}
