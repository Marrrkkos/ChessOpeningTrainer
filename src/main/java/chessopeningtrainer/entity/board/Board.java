package chessopeningtrainer.entity.board;

/**
 * This is the actual ChessBoard, containing a 8x8 Array of {@link Field}
 */
public class Board {
    Field[][] board;

    /**
     * The actual chessBoard, containing 64 {@link Field}
     */
    public Board() {
        this.board = new Field[8][8];
    }

    public Field[][] getField() {
        return board;
    }

}
