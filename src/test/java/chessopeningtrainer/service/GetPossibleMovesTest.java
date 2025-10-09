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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetPossibleMovesTest {
    RootService rootService = new RootService();
    @BeforeEach
    void setUp() {
        Player[] players = {new Player(true), new Player(false)};
        Game game = new Game(new ArrayList<Turn>(), new ArrayList<Turn>(), players);
        initializeFields(game.getBoard());
        rootService.currentGame = game;
    }
    @Test
    void testFinalPawnMoves() {
        Game game = rootService.currentGame;
        game.getBoard().getField()[0][6].setPiece(new Pawn(true));  // a2
        List<Position> possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,6));
        assertTrue(possibleMoves.contains(new Position(0,4)));  // a4           // has to be rank 2, to move 2 fields

        game.getBoard().getField()[0][5].setPiece(new Pawn(true));  // a3
        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,5));
        assertFalse(possibleMoves.contains(new Position(0,3)));

        game.getBoard().getField()[1][4].setPiece(new Pawn(false));     //capture
        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,5));
        assertTrue(possibleMoves.contains(new Position(1,4)));
    }
    @Test
    void testEnPassant(){
        Game game = rootService.currentGame;

        game.getBoard().getField()[0][3].setPiece(new Pawn(true));  // 5th rank a5
        game.getBoard().getField()[1][3].setPiece(new Pawn(false));    // 5th rank b5
        game.getTurns().add(new Turn(0,new Position(1,1), new Position(1,3),new Pawn(false), new Queen(true),'n'));  // last move has to be from a pawn and a 2 field move

        List<Position> possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,3));
        assertTrue(possibleMoves.contains(new Position(1,2)));  // b6           // has to be rank 2, to move 2 fields


        game.getTurns().removeLast();   // remove last turn, now it's not possible

        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,3));
        assertFalse(possibleMoves.contains(new Position(1,2)));  // b6           // has to be rank 2, to move 2 fields

        game.getTurns().add(new Turn(0,new Position(1,1), new Position(1,3),new Queen(false), new Queen(true),'n'));  // last turn has to be a pawn, or it dont work
        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(0,3));
        assertFalse(possibleMoves.contains(new Position(1,2)));  // b6
    }
    @Test
    void testCastle(){
        Game game = rootService.currentGame;

        game.getBoard().getField()[4][0].setPiece(new King(true, false));  // e1 (normal white king position)
        game.getBoard().getField()[7][0].setPiece(new Rook(true, false));
        game.getBoard().getField()[0][0].setPiece(new Rook(true, false));

        List<Position> possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(4,0));
        assertTrue(possibleMoves.contains(new Position(2,0)));  //long castle allowed
        assertTrue(possibleMoves.contains(new Position(6,0)));  //short castle allowed

        game.getBoard().getField()[7][0].setPiece(new Rook(true, true));    //hasMoved
        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(4,0));
        assertFalse(possibleMoves.contains(new Position(6,0)));  //short castle not allowed

        game.getBoard().getField()[7][0].setPiece(new Rook(true, false));
        game.getBoard().getField()[4][0].setPiece(new King(true, true));    //king hasMoved
        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(4,0));
        assertFalse(possibleMoves.contains(new Position(6,0)));  //short castle not allowed

    }
    @Test
    void testKingInCheckCastle(){
        Game game = rootService.currentGame;

        game.getBoard().getField()[4][0].setPiece(new King(true, false));  // e1 (normal white king position)
        game.getBoard().getField()[7][0].setPiece(new Rook(true, false));
        game.getBoard().getField()[5][5].setPiece(new Rook(false, false));  // enemy rook in the way from castling (not in check, just in the way)

        List<Position> possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(4,0));
        assertFalse(possibleMoves.contains(new Position(6,0)));  //short castle not allowed

        game.getBoard().getField()[5][5].setPiece(null);
        game.getBoard().getField()[4][4].setPiece(new Rook(false, false));  // now king is in check
        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(new Position(4,0));
        assertFalse(possibleMoves.contains(new Position(6,0)));  //short castle not allowed


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
