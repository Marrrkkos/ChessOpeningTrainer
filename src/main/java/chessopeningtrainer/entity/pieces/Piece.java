package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    public abstract void setHasMoved(boolean hasMoved);
    public abstract boolean getHasMoved();
    public abstract int getID();
    public abstract boolean getColour();
    public abstract Image getImage();
    public abstract List<List<Position>> getBasicPieceMoves(Position currentPiecePosition);
    protected boolean isInBounds(Position position){
        return (position.getX() <= 7 && position.getY() <= 7) && (position.getX() >= 0 && position.getY() >= 0);
    }
    protected List<List<Position>> getDiagonalMoves(Position currentPiecePosition){
        List<List<Position>> list = new ArrayList<>();
        List<Position> moves;

        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();

        int[] arr = {1, -1};        // All 4 directions
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                moves = new ArrayList<>();
                int x = posX + arr[i];
                int y = posY + arr[j];

                while(isInBounds(new Position(x,y))){
                    moves.add(new Position(x,y));

                    x += arr[i];
                    y += arr[j];
                }
                list.add(moves);
            }
        }
        return list;
    }
    protected List<List<Position>> getStraightMoves(Position currentPiecePosition){
        List<List<Position>> list = new ArrayList<>();
        List<Position> moves;

        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();

        int[] arr = {1, -1};        // All 4 directions (1,0)(-1,0)(0,1)(0,-1)
        for (int i = 0; i < 2; i++) {
            moves = new ArrayList<>();

            int x = posX + arr[i];
            int y = posY + arr[i];

            while(isInBounds(new Position(x,posY))){
                moves.add(new Position(x,posY));

                x += arr[i];
            }
            list.add(moves);
            moves = new ArrayList<>();
            while(isInBounds(new Position(posX,y))){
                moves.add(new Position(posX,y));

                y += arr[i];
            }
            list.add(moves);
        }
        return list;
    }
}
