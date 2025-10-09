package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Represents a Queen chess piece.
 * Can move any number of squares diagonally, vertically and horizontal.
 */
public class Queen extends Piece{
    boolean colour;
    public Queen(boolean colour){
        this.colour = colour;
    }

    Image WQueen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-queen.png")));
    Image BQueen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-queen.png")));
    @Override
    public Image getImage(){
        return colour ? WQueen : BQueen;

    }

    @Override
    public List<List<Position>> getBasicPieceMoves(Position currentPiecePosition) {
        List<List<Position>> moves = new ArrayList<>();
        moves.addAll(getDiagonalMoves(currentPiecePosition));
        moves.addAll(getStraightMoves(currentPiecePosition));
        return moves;
    }

    @Override
    public boolean getHasMoved() {
        return false;
    }
    @Override
    public void setHasMoved(boolean hasMoved) {
    }

        @Override
    public int getID() {
        return 4;
    }
    public boolean getColour() {
        return colour;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Queen other)) return false;
        return this.getID() == other.getID() && this.getColour() == other.getColour();
    }
}
