package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Field;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.game.Game;
import chessopeningtrainer.entity.game.Player;
import chessopeningtrainer.entity.pieces.Pawn;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CheckPromotionTest {
    RootService rootService = new RootService();
    @BeforeEach
    void setUp() {
        Player[] players = {new Player(true), new Player(false)};
        Game game = new Game(new ArrayList<>(), new ArrayList<>(), players);
        initializeFields(game.getBoard());
        rootService.currentGame = game;
    }
    @Test
    void testPromotion(){
        Game game = rootService.currentGame;
        game.getBoard().getField()[0][1].setPiece(new Pawn(true));  // a2
        List<Position> possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,1));
        assertTrue(rootService.moveService.checkPromotion(new Position(0, 1), new Position(0, 0), new Button[8][8], possibleMoves));

        game.getBoard().getField()[0][2].setPiece(new Pawn(true));
        assertFalse(rootService.moveService.checkPromotion(new Position(0, 2), new Position(0, 1), new Button[8][8], new ArrayList<Position>()));

    }
    private void initializeFields(Board board) {

        int k = 8;
        for (int i = 0; i < 8; i++) {
            board.getField()[i][0] = new Field("a" + (k), new Position(i, 0));
            board.getField()[i][1] = new Field("b" + (k), new Position(i, 1));
            board.getField()[i][2] = new Field("c" + (k), new Position(i, 2));
            board.getField()[i][3] = new Field("d" + (k), new Position(i, 3));
            board.getField()[i][4] = new Field("e" + (k), new Position(i, 4));
            board.getField()[i][5] = new Field("f" + (k), new Position(i, 5));
            board.getField()[i][6] = new Field("g" + (k), new Position(i, 6));
            board.getField()[i][7] = new Field("h" + (k), new Position(i, 7));
            k--;
        }
    }
}
