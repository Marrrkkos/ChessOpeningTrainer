package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pawn extends Piece{
    boolean colour;
    public Pawn(boolean colour, boolean hasMovedOnlyOnes){
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
    public List<List<Position>> getBasicPieceMoves(Position currentPiecePosition) {
        List<List<Position>> list = new ArrayList<>();
        List<Position> moves = new ArrayList<>();
        int direction = (this.colour) ? -1 : 1;     // Counts from top right corner. So White is at bottom on line 7-8

        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();

        moves.add(new Position(posX,posY + direction));
        if(direction == 1 && posY == 1){                            //first pawn move can go 2 moves
            moves.add(new Position(posX,posY + 2*direction));
        }
        if(direction == -1 && posY == 6){
            moves.add(new Position(posX,posY + 2*direction));
        }
        list.add(moves);
        moves = new ArrayList<>();
        Position pos1 = new Position(posX + -1,posY + direction);       //capture left and right
        Position pos2 = new Position(posX + +1,posY + direction);

        if(isInBounds(pos1)){
            moves.add(pos1);
        }
        if(isInBounds(pos2)){
            moves.add(pos2);
        }

        list.add(moves);
        return list;
    }
    public boolean getColour() {
        return colour;
    }
}
