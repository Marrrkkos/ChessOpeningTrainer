package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Field;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.game.Game;
import chessopeningtrainer.entity.game.Player;
import chessopeningtrainer.entity.game.Turn;
import chessopeningtrainer.entity.pieces.King;
import chessopeningtrainer.entity.pieces.Pawn;
import chessopeningtrainer.entity.pieces.Queen;
import chessopeningtrainer.entity.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoMoveTest {
    RootService rootService = new RootService();
    @BeforeEach
    void setUp() {
        Player[] players = {new Player(true), new Player(false)};
        Game game = new Game(new ArrayList<>(), new ArrayList<>(), players);
        initializeFields(game.getBoard());
        rootService.currentGame = game;
    }
    @Test
    void testPawnMoves() {
        Game game = rootService.currentGame;
        game.getBoard().getBoard()[0][6].setPiece(new Pawn(true));  // a2
        List<Position> possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,6));
        rootService.moveService.doMove(new Position(0,6), new Position(0 ,4), possibleMoves,true);

        assertEquals(new Pawn(true) , game.getBoard().getBoard()[0][4].getPiece());
        assertNull(game.getBoard().getBoard()[0][6].getPiece());    //moved Place

        rootService.moveService.undoMove();
        assertEquals(new Pawn(true) , game.getBoard().getBoard()[0][6].getPiece());
        assertNull(game.getBoard().getBoard()[0][4].getPiece());


        game.getBoard().getBoard()[1][5].setPiece(new Pawn(false));     //capture
        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,6));
        assertTrue(possibleMoves.contains(new Position(1,5)));

        rootService.moveService.doMove(new Position(0,6), new Position(1 ,5), possibleMoves,true);  //capture

        assertEquals(new Pawn(true) , game.getBoard().getBoard()[1][5].getPiece());     // black pawn captured
        assertNull(game.getBoard().getBoard()[0][6].getPiece());

        rootService.moveService.undoMove();
        assertEquals(new Pawn(true) , game.getBoard().getBoard()[0][6].getPiece());     //old position
        assertEquals(new Pawn(false) , game.getBoard().getBoard()[1][5].getPiece());
    }
    @Test
    void testEnPassantMoves(){
        Game game = rootService.currentGame;

        game.getBoard().getBoard()[0][3].setPiece(new Pawn(true));  // 5th rank a5
        game.getBoard().getBoard()[1][3].setPiece(new Pawn(false));    // 5th rank b5
        game.getTurns().add(new Turn(0,new Position(1,1), new Position(1,3),new Pawn(false), new Queen(true),'n'));  // last move has to be from a pawn and a 2 field move

        List<Position> possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,3));
        rootService.moveService.doMove(new Position(0,3), new Position(1 ,2), possibleMoves,true);

        assertEquals(new Pawn(true) , game.getBoard().getBoard()[1][2].getPiece());
        assertNull(game.getBoard().getBoard()[1][3].getPiece());    //moved Place

        rootService.moveService.undoMove();
        assertEquals(new Pawn(true) , game.getBoard().getBoard()[0][3].getPiece());
        assertEquals(new Pawn(false) , game.getBoard().getBoard()[1][3].getPiece());
        assertNull(game.getBoard().getBoard()[1][2].getPiece());
    }
    @Test
    void testCastle(){
        Game game = rootService.currentGame;

        game.getBoard().getBoard()[4][0].setPiece(new King(true, false));  // e1 (normal white king position)
        game.getBoard().getBoard()[7][0].setPiece(new Rook(true, false));
        game.getBoard().getBoard()[0][0].setPiece(new Rook(true, false));

        List<Position> possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(4,0));

        rootService.moveService.doMove(new Position(4,0), new Position(6 ,0), possibleMoves,true);      // short castle

        assertEquals(new Rook(true, false) , game.getBoard().getBoard()[5][0].getPiece());
        assertEquals(new King(true, false) , game.getBoard().getBoard()[6][0].getPiece());
        assertNull(game.getBoard().getBoard()[4][0].getPiece());    //moved Place
        assertNull(game.getBoard().getBoard()[7][0].getPiece());    //moved Place

        rootService.moveService.undoMove();
        assertEquals(new Rook(true, false) , game.getBoard().getBoard()[7][0].getPiece());
        assertEquals(new King(true, false) , game.getBoard().getBoard()[4][0].getPiece());
        assertNull(game.getBoard().getBoard()[5][0].getPiece());    //moved Place
        assertNull(game.getBoard().getBoard()[6][0].getPiece());    //moved Place

        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(4,0));


        rootService.moveService.doMove(new Position(4,0), new Position(2 ,0), possibleMoves,true);  //long castle

        assertEquals(new Rook(true, false) , game.getBoard().getBoard()[3][0].getPiece());
        assertEquals(new King(true, false) , game.getBoard().getBoard()[2][0].getPiece());
        assertNull(game.getBoard().getBoard()[4][0].getPiece());    //moved Place
        assertNull(game.getBoard().getBoard()[0][0].getPiece());    //moved Place

        rootService.moveService.undoMove();
        assertEquals(new Rook(true, false) , game.getBoard().getBoard()[0][0].getPiece());
        assertEquals(new King(true, false) , game.getBoard().getBoard()[4][0].getPiece());
        assertNull(game.getBoard().getBoard()[3][0].getPiece());    //moved Place
        assertNull(game.getBoard().getBoard()[2][0].getPiece());    //moved Place

    }

    private void initializeFields(Board board) {

        int k = 8;
        for (int i = 0; i < 8; i++) {
            board.getBoard()[i][0] = new Field("a" + (k), new Position(i, 0));
            board.getBoard()[i][1] = new Field("b" + (k), new Position(i, 1));
            board.getBoard()[i][2] = new Field("c" + (k), new Position(i, 2));
            board.getBoard()[i][3] = new Field("d" + (k), new Position(i, 3));
            board.getBoard()[i][4] = new Field("e" + (k), new Position(i, 4));
            board.getBoard()[i][5] = new Field("f" + (k), new Position(i, 5));
            board.getBoard()[i][6] = new Field("g" + (k), new Position(i, 6));
            board.getBoard()[i][7] = new Field("h" + (k), new Position(i, 7));
            k--;
        }
    }

}
