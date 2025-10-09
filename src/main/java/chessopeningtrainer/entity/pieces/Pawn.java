package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pawn extends Piece{
    boolean colour;
    public Pawn(boolean colour){
        this.colour = colour;
    }

    Image WPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-pawn.png")));
    Image BPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-pawn.png")));
    @Override
    public Image getImage(){
        if (this.colour) {
            return WPawn;
        }else{
            return BPawn;
        }

    }
    @Override
    public String toString(){
        if(this.colour){
            return "WPawn";
        }else{
            return "BPawn";
        }
    }
    @Override
    public List<List<Position>> getBasicPieceMoves(Position currentPiecePosition) { // Implemented in specialMovesService, cause pawn is special
        return new ArrayList<>(); //return list
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
        return 0;
    }

    public boolean getColour() {
        return colour;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pawn other)) return false;
        return this.getID() == other.getID() && this.getColour() == other.getColour();
    }
}
