package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Field;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.game.Game;
import chessopeningtrainer.entity.game.Player;
import chessopeningtrainer.entity.pieces.*;
import chessopeningtrainer.view.Refreshable;

import java.util.ArrayList;

public class GameService extends AbstractRefreshingService{
    private final RootService rootService;

    GameService(RootService rootService) {
        this.rootService = rootService;
    }

    /**
     * starts the game by initializing the 64 Fields, with it Positions and the startPosition by every {@link Piece}
     * and sets it to rootService.currentGame
     */
    public void startGame() {
        initializeGame();
        Board board = rootService.currentGame.getBoard();
        for (Refreshable r : refreshables) {
            r.refreshAfterGameStart(board);
        }
    }


    private void initializeGame() {
        Player[] players = {new Player(true), new Player(false)};
        Game game = new Game(new ArrayList<>(), new ArrayList<>(), players);
        initializeFields(game.getBoard());
        initializePieces(game.getBoard());
        rootService.currentGame = game;
    }
    private void initializePieces(Board board){
        board.getField()[0][0].setPiece(new Rook(false, false));  // Rooks
        board.getField()[7][0].setPiece(new Rook(false, false));
        board.getField()[0][7].setPiece(new Rook(true, false));
        board.getField()[7][7].setPiece(new Rook(true, false));

        board.getField()[1][0].setPiece(new Knight(false));  // Knights
        board.getField()[6][0].setPiece(new Knight(false));
        board.getField()[6][7].setPiece(new Knight(true));
        board.getField()[1][7].setPiece(new Knight(true));

        board.getField()[2][0].setPiece(new Bishop(false));  // Bishops
        board.getField()[5][0].setPiece(new Bishop(false));
        board.getField()[5][7].setPiece(new Bishop(true));
        board.getField()[2][7].setPiece(new Bishop(true));

        board.getField()[3][0].setPiece(new Queen(false));  // Queens
        board.getField()[3][7].setPiece(new Queen(true));

        board.getField()[4][0].setPiece(new King(false, false));  // Kings
        board.getField()[4][7].setPiece(new King(true, false));

        for (int i = 0; i < 8; i++) {  // Pawns
            board.getField()[i][1].setPiece(new Pawn(false));
        }
        for (int i = 0; i < 8; i++) {
            board.getField()[i][6].setPiece(new Pawn(true));
        }
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