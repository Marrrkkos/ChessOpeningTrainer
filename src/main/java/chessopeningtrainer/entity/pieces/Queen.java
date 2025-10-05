package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Queen extends Piece{
    boolean colour;
    public Queen(boolean colour){
        this.colour = colour;
    }

    Image WQueen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-queen.png")));
    Image BQueen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-queen.png")));
    @Override
    public Image getImage(){
        if (this.colour) {
            return WQueen;
        }else{
            return BQueen;
        }

    }

    @Override
    public List<List<Position>> getBasicPieceMoves(Position currentPiecePosition) {
        List<List<Position>> moves = new ArrayList<>();
        moves.addAll(getDiagonalMoves(currentPiecePosition));
        moves.addAll(getStraightMoves(currentPiecePosition));
        return moves;
    }

    public boolean getColour() {
        return colour;
    }
}
