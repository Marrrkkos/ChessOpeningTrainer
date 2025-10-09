package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Field;
import chessopeningtrainer.entity.board.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {
    Board board;
    @BeforeEach
    void setUp() {
        board = new Board();
        initializeFields(board);
    }
    @Test
    void getBasicQueenMoves() {
        Field field = board.getBoard()[3][3];
        field.setPiece(new Queen(true));
        List<List<Position>> possibleMoves = field.getPiece().getBasicPieceMoves(new Position(3,3));
        assertEquals(8, possibleMoves.size());  // 8 directions

        assertEquals(27, possibleMoves.stream().mapToInt(List::size).sum());
    }


    private void initializeFields(Board board){
        int k = 8;
        for (int i = 0; i < 8; i++) {
            board.getBoard()[i][0] = new Field("a" + (k), new Position(i,0));
            board.getBoard()[i][1] = new Field("b" + (k), new Position(i,1));
            board.getBoard()[i][2] = new Field("c" + (k), new Position(i,2));
            board.getBoard()[i][3] = new Field("d" + (k), new Position(i,3));
            board.getBoard()[i][4] = new Field("e" + (k), new Position(i,4));
            board.getBoard()[i][5] = new Field("f" + (k), new Position(i,5));
            board.getBoard()[i][6] = new Field("g" + (k), new Position(i,6));
            board.getBoard()[i][7] = new Field("h" + (k), new Position(i,7));
            k--;
        }
    }
}