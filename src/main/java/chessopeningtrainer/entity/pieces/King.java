package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class King extends Piece{
    boolean colour;
    boolean hasMoved;
    public King(boolean colour, boolean hasMoved){
        this.colour = colour;
        this.hasMoved = hasMoved;
    }

    Image WKing = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-king.png")));
    Image BKing = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-king.png")));
    @Override
    public Image getImage(){
        if (this.colour) {
            return WKing;
        }else{
            return BKing;
        }

    }

    @Override
    public List<List<Position>> getBasicPieceMoves(Position currentPiecePosition) {
        List<List<Position>> list = new ArrayList<>();
        List<Position> moves = new ArrayList<>();

        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();
        Position pos;
        int[] arr = {0, 1, -1};
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                pos = new Position(posX + arr[j], posY + arr[i]);
                if(isInBounds(pos)&& !(i==0 && j==0) ) {                // Make sure 0 0(own king position) is not in possible Moves
                    moves.add(pos);
                    /*if(!hasMoved && arr[i]==0 && arr[j]==1){            // adds the possible castle-move to the line where the king moves right
                        moves.add(new Position(posX + 2, posY));
                    }
                    if(!hasMoved && arr[i]==0 && arr[j]==-1){       // adds the possible castle-move to the line where the king moves left
                        moves.add(new Position(posX - 2, posY));
                    }*/

                    list.add(moves);
                    moves = new ArrayList<>();
                }
            }
        }
        return list;
    }

    @Override
    public boolean getHasMoved() {
        return hasMoved;
    }
    @Override
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

        @Override
    public int getID() {
        return 5;
    }
    public boolean getColour() {
        return colour;
    }
}
