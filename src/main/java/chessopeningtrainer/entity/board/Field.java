package chessopeningtrainer.entity.board;

import chessopeningtrainer.entity.pieces.Piece;

/**
 * {@link Board} contains 8x8=64 of these Fields
 */
public class Field {
    Piece piece;
    String name;
    Position position;
    /**
     *
     * @param piece The Piece standing on this Field, Null when no piece
     * @param name The name of the Fields a1-h8 to make Notation
     */
    public Field(Piece piece, String name, Position position) {
        this.piece = piece;
        this.name = name;
        this.position = position;
    }

    /**
     * Only the Name and Position of the Fields to initialize the Board
     */
    public Field(String name, Position position) {
        this.name = name;
        this.piece = null;
        this.position = position;
    }

    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Position getPosition() {
        return position;
    }
}
