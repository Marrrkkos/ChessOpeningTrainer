package chessopeningtrainer.entity.board;

/**
 * This is the actual ChessBoard
 */
public class Board {
    Field[][] board;

    /**
     * The actual chessBoard, containing 64 Field-Objects
     */
    public Board() {
        this.board = new Field[8][8];
    }

    public Field[][] getBoard() {
        return board;
    }

}
