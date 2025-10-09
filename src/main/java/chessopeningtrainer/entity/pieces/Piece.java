package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Position;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a chess piece.
 * Provides basic move generation, only the own piece knows on an empty 8x8 Field.
 * These gets filtered in the service Layer
 */
public abstract class Piece {

    /** Sets whether the piece has moved (important for castling, pawn first move). */
    public abstract void setHasMoved(boolean hasMoved);

    /** Returns true if the piece has moved before. */
    public abstract boolean getHasMoved();

    /** Returns a unique ID for the piece type. */
    public abstract int getID();

    /** Returns the color of the piece (true = white, false = black). */
    public abstract boolean getColour();

    /** Returns the image representing this piece. */
    public abstract Image getImage();

    /** Returns all basic moves for this piece without considering other pieces. */
    public abstract List<List<Position>> getBasicPieceMoves(Position currentPiecePosition);

    /** Checks if a position is within the 8x8 chessboard bounds. */
    protected boolean isInBounds(Position position){
        return (position.getX() <= 7 && position.getY() <= 7)
                && (position.getX() >= 0 && position.getY() >= 0);
    }

    /** Generates all diagonal moves from the current position. */
    protected List<List<Position>> getDiagonalMoves(Position currentPiecePosition){
        List<List<Position>> list = new ArrayList<>();
        List<Position> moves;

        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();

        int[] arr = {1, -1}; // directions
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

    /** Generates all straight (horizontal + vertical) moves from the current position. */
    protected List<List<Position>> getStraightMoves(Position currentPiecePosition){
        List<List<Position>> list = new ArrayList<>();
        List<Position> moves;

        int[] directions = {1, -1};

        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();

        for (int d : directions) {
            moves = new ArrayList<>();

            // Horizontal moves
            for (int x = posX + d; isInBounds(new Position(x, posY)); x += d) {
                moves.add(new Position(x, posY));
            }
            list.add(moves);

            moves = new ArrayList<>();

            // Vertical moves
            for (int y = posY + d; isInBounds(new Position(posX, y)); y += d) {
                moves.add(new Position(posX, y));
            }
            list.add(moves);
        }
        return list;
    }
}
