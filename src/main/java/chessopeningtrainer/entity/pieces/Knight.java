package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Knight extends Piece{
    boolean colour;
    public Knight(boolean colour){

        this.colour = colour;
    }
    Image WKnight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-knight.png")));
    Image BKnight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-knight.png")));
    @Override
    public Image getImage(){
        if (this.colour) {
            return WKnight;
        }else{
            return BKnight;
        }

    }

    @Override
    public List<List<Position>> getBasicPieceMoves(Position currentPiecePosition) {
        List<List<Position>> list = new ArrayList<>();
        List<Position> moves = new ArrayList<>();

        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();
        Position pos;
        int[] arr = {1, -1};        // All possible L Shapes for the Knight
        int[] arr2 = {-2, 2};
        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++){
                pos = new Position(posX + arr2[j], posY + arr[i]);
                if(isInBounds(pos)) {
                    moves.add(pos);
                }
                list.add(moves);
                moves = new ArrayList<>();
                pos = new Position(posX + arr[j], posY + arr2[i]);
                if(isInBounds(pos)) {
                    moves.add(pos);
                }
                list.add(moves);
                moves = new ArrayList<>();
            }
        }

        return list;
    }
    public boolean getColour() {
        return colour;
    }
}
