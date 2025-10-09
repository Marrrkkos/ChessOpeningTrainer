package chessopeningtrainer.entity.pieces;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Field;
import chessopeningtrainer.entity.board.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

    Board board;
    @BeforeEach
    void setUp() {
        board = new Board();
        initializeFields(board);
    }
    @Test
    void getBasicKnightMoves() {
        Field field = board.getField()[3][3];
        field.setPiece(new Knight(true));
        List<List<Position>> possibleMoves = field.getPiece().getBasicPieceMoves(new Position(3,3));
        assertEquals(8, possibleMoves.size());  // 8 possible Moves in the middle
        assertEquals(possibleMoves.size(), possibleMoves.stream().mapToInt(List::size).sum());  // has to be the same cause no piece cant be in the way

        Field field1 = board.getField()[0][0];  //corner
        field1.setPiece(new Knight(true));
        List<List<Position>> possibleMoves1 = field1.getPiece().getBasicPieceMoves(new Position(0,0));
        assertEquals(2, possibleMoves1.size());  // 2 possible Moves in the corner
        assertEquals(possibleMoves1.size(), possibleMoves1.stream().mapToInt(List::size).sum());       // has to be the same cause no piece cant be in the way
    }


    private void initializeFields(Board board){
        int k = 8;
        for (int i = 0; i < 8; i++) {
            board.getField()[i][0] = new Field("a" + (k), new Position(i,0));
            board.getField()[i][1] = new Field("b" + (k), new Position(i,1));
            board.getField()[i][2] = new Field("c" + (k), new Position(i,2));
            board.getField()[i][3] = new Field("d" + (k), new Position(i,3));
            board.getField()[i][4] = new Field("e" + (k), new Position(i,4));
            board.getField()[i][5] = new Field("f" + (k), new Position(i,5));
            board.getField()[i][6] = new Field("g" + (k), new Position(i,6));
            board.getField()[i][7] = new Field("h" + (k), new Position(i,7));
            k--;
        }
    }
}