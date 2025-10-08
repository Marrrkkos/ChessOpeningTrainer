package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Field;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.game.Game;
import chessopeningtrainer.entity.game.Turn;
import chessopeningtrainer.entity.pieces.*;
import chessopeningtrainer.view.Refreshable;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class MoveService extends AbstractRefreshingService{
    private RootService rootService;

    public MoveService(RootService rootService) {
        this.rootService = rootService;

    }
    public List<Position> getPossibleMoves(Position currentPiecePosition){
        List<Position> possibleMoves = new ArrayList<>();
        Game game = rootService.currentGame;
        Board board = game.getBoard();
        Piece currentPiece = board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece();
        if(currentPiece != null && currentPiece.getColour() == game.getPlayer()[game.getCurrentPlayer()].getColour()){
            possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(currentPiecePosition);
        }


        for (Refreshable r : refreshables) {
            r.refreshAfterShowPossibleMoves(possibleMoves);
        }
        return possibleMoves;
    }

    /**
     *
     * @param position1
     * @param position2
     * @param moves
     * @param simulation    if this method is used with game update, simulation is false
     */
    public void doMove(Position position1, Position position2, List<Position> moves, boolean simulation) {
        Game game = rootService.currentGame;
        Board board = game.getBoard();
        Piece piece = board.getBoard()[position1.getX()][position1.getY()].getPiece();
        if (!position1.equals(position2)) {
            System.out.println("test");

            Piece capturedPiece = board.getBoard()[position2.getX()][position2.getY()].getPiece();
            for (Position p : moves) {
                if (p.equals(position2)) {
                    if (p.getChar() == 'e') {         // En Passant
                        capturedPiece =  board.getBoard()[position2.getX()][position1.getY()].getPiece();   // En passant captured piece is not the endPosition

                        board.getBoard()[position2.getX()][position2.getY()].setPiece(piece);
                        board.getBoard()[position1.getX()][position1.getY()].setPiece(null);
                        board.getBoard()[position2.getX()][position1.getY()].setPiece(null);        // In between Pos 1 and pos2 (the piece right next to pos1) = null: EnPassant

                        game.getTurns().add(new Turn(0, position1, position2, capturedPiece, 'e'));
                        if (!simulation) {
                            for (Refreshable r : refreshables) {
                                r.refreshAfterEnPassant(position1, position2, piece, board);
                                r.refreshAfterGameStart(board);
                            }
                        }
                    } else if (p.getChar() == 'c') {           // Castle
                        Piece rook;
                        board.getBoard()[position2.getX()][position2.getY()].setPiece(piece);
                        board.getBoard()[position1.getX()][position1.getY()].setPiece(null);

                        //short castle
                        if (position2.getX() > position1.getX()) {
                            rook = board.getBoard()[position2.getX() + 1][position2.getY()].getPiece();
                            board.getBoard()[position2.getX() - 1][position2.getY()].setPiece(rook);
                            board.getBoard()[position2.getX() + 1][position2.getY()].setPiece(null);
                        } else {          //long castle
                            rook = board.getBoard()[position2.getX() - 2][position2.getY()].getPiece();
                            board.getBoard()[position2.getX() + 1][position2.getY()].setPiece(rook);
                            board.getBoard()[position2.getX() - 2][position2.getY()].setPiece(null);
                        }
                        game.getTurns().add(new Turn(0, position1, position2, capturedPiece, 'c'));
                        if (!simulation) {
                            for (Refreshable r : refreshables) {
                                r.refreshAfterCastle(position1, position2, piece, rook, board);
                                r.refreshAfterGameStart(board);
                            }
                        }
                    } else {
                        board.getBoard()[position2.getX()][position2.getY()].setPiece(piece);       // Normal Move
                        board.getBoard()[position1.getX()][position1.getY()].setPiece(null);
                        game.getTurns().add(new Turn(0, position1, position2, capturedPiece, 'n'));
                        if (!simulation) {
                            for (Refreshable r : refreshables) {
                                r.refreshAfterNormalMove(position1, position2, piece);
                                r.refreshAfterGameStart(board);
                            }
                        }
                    }
                    if(!simulation) {       // only set pieceHasMoves when its in real game
                        piece.setHasMoved(true);
                        rootService.currentGame.nextPlayer();
                    }
                }
            }
        }
    }
    public void undoMove(Position movedPiecePos) {
        Game game = rootService.currentGame;
        Board board = game.getBoard();
        Piece movedPiece = board.getBoard()[movedPiecePos.getX()][movedPiecePos.getY()].getPiece();
        Turn lastTurn = game.getTurns().getLast();
        Piece capturedPiece = lastTurn.getCapturedPiece();

        int startPosX = lastTurn.getStartPos().getX();   // last Move starting Position
        int startPosY = lastTurn.getStartPos().getY();

        int endPosX = movedPiecePos.getX();   // last Move end Postion
        int endPosY = movedPiecePos.getY();

        if(lastTurn.getRule() == 'e'){         // En Passant
            board.getBoard()[endPosX][endPosY].setPiece(null);
            board.getBoard()[startPosX][startPosY].setPiece(movedPiece);
            board.getBoard()[endPosX][startPosY].setPiece(capturedPiece);        // In between pos1 and pos2 (the piece right next to pos1) = null: EnPassant

        }else if(lastTurn.getRule() == 'c'){           // Castle
            Piece rook;
            board.getBoard()[startPosX][startPosY].setPiece(movedPiece);      //King
            //short castle
            if(endPosX > startPosX){
                rook = board.getBoard()[endPosX - 1][startPosY].getPiece();   //rook Position after castling

                board.getBoard()[endPosX+1][endPosY].setPiece(rook);
                board.getBoard()[endPosX][endPosY].setPiece(null);
                board.getBoard()[endPosX-1][endPosY].setPiece(null);
            }else{          //long castle
                rook = board.getBoard()[startPosX - 1][startPosY].getPiece();   //rook Position after castling

                board.getBoard()[endPosX-2][endPosY].setPiece(rook);
                board.getBoard()[endPosX-1][endPosY].setPiece(null);
                board.getBoard()[endPosX][endPosY].setPiece(null);
                board.getBoard()[endPosX+1][endPosY].setPiece(null);
            }
        }else{
            board.getBoard()[endPosX][endPosY].setPiece(capturedPiece);       // Normal Move
            board.getBoard()[startPosX][startPosY].setPiece(movedPiece);
        }
        game.getTurns().removeLast();
    }
    public void doPromotion(Position position1, Position position2, Button[][] buttonArray, Position clickedPosition){
        Game game = rootService.currentGame;
        Board board = game.getBoard();

        char p;
        boolean colour = board.getBoard()[position1.getX()][position1.getY()].getPiece().getColour();
        int direction = colour ? 1 : -1;
        Piece promotionPiece;

        if(position2.equals(clickedPosition)){
            promotionPiece = new Queen(colour);
            p = 'Q';
        }else if(position2.getX() == clickedPosition.getX() && position2.getY()+direction == clickedPosition.getY()){
            promotionPiece = new Rook(colour, true);
            p = 'R';
        }else if(position2.getX() == clickedPosition.getX() && position2.getY()+2*direction == clickedPosition.getY()){
            promotionPiece = new Bishop(colour);;
            p = 'B';
        }else if(position2.getX() == clickedPosition.getX() && position2.getY()+3*direction == clickedPosition.getY()){
            promotionPiece = new Knight(colour);
            p = 'K';
        }else{
            promotionPiece = null;
            p = 'X';    // Never used
        }
        if(promotionPiece != null){
            Piece capturedPiece = board.getBoard()[position2.getX()][position2.getY()].getPiece();
            board.getBoard()[position2.getX()][position2.getY()].setPiece(promotionPiece);       // Normal Move
            board.getBoard()[position1.getX()][position1.getY()].setPiece(null);
            game.getTurns().add(new Turn(0, position1, position2, capturedPiece, p));
        }
        boolean legal = p != 'X';
        for (Refreshable r : refreshables) {
            r.refreshAfterPromotion(position1, position2, buttonArray, colour, board, legal);
        }
        rootService.currentGame.nextPlayer();
    }
    public boolean checkPromotion(Position position1, Position position2, Button[][] buttonArray, List<Position> list) {
        Game game = rootService.currentGame;
        Board board = game.getBoard();

        Field field =  board.getBoard()[position1.getX()][position1.getY()];
        Piece piece = field.getPiece();
        if(piece == null || !list.contains(position2)){
            return false;
        }
        if(piece.getID() == 0){
            if(position2.getY() == 0 && piece.getColour()){
                for (Refreshable r : refreshables) {
                    r.refreshAfterShowPromotion(position1, position2, buttonArray, piece.getColour());
                }
                return true;
            }else if( position2.getY() == 7 && !piece.getColour()){
                for (Refreshable r : refreshables) {
                    r.refreshAfterShowPromotion(position1, position2, buttonArray, piece.getColour());
                }
                return true;
            }
        }

        return false;
    }

}
