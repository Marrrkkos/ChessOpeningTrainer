package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.game.Game;
import chessopeningtrainer.entity.game.Turn;
import chessopeningtrainer.entity.pieces.*;
import chessopeningtrainer.view.Refreshable;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the primary class for the gui to work with. It provides the basic stuff, like doMove, undoMove etc
 */
public class MoveService extends AbstractRefreshingService{
    private final RootService rootService;

    MoveService(RootService rootService) {
        this.rootService = rootService;

    }

    /**
     * it gives you the final moves you can play with currentPiece, filtered from checks etc
     *
     * @param currentPiecePosition  the current piece position
     * @return the list of final possible Moves of the piece from currentPiecePosition
     */
    public List<Position> getPossibleMoves(Position currentPiecePosition){
        List<Position> possibleMoves = new ArrayList<>();
        Game game = rootService.currentGame;
        Board board = game.getBoard();
        Piece currentPiece = board.getField()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece();
        if(currentPiece != null && currentPiece.getColour() == game.getPlayer()[game.getCurrentPlayer()].getColour()){
            possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(currentPiecePosition);
        }


        for (Refreshable r : refreshables) {
            r.refreshAfterShowPossibleMoves(possibleMoves);
        }
        return possibleMoves;
    }

    /**
     * This method gets a move as input and checks if this move is in the list of PossibleMoves. When it is
     * it performs the move (including castling, en passant)  no promotion
     * @param position1     startPosition   from this move
     * @param position2     endPosition from this move
     * @param moves         the possibleMoves from getPossibleMoves
     * @param simulation    if this method is used with gui update, simulation is false: simulation is used in checkForChecks
     */
    public void doMove(Position position1, Position position2, List<Position> moves, boolean simulation) {
        Game game = rootService.currentGame;
        Board board = game.getBoard();
        Piece piece = board.getField()[position1.getX()][position1.getY()].getPiece();
        if (!position1.equals(position2)) {

            Piece capturedPiece = board.getField()[position2.getX()][position2.getY()].getPiece();
            for (Position p : moves) {
                if (p.equals(position2)) {
                    if (p.getChar() == 'e') {         // En Passant
                        capturedPiece =  board.getField()[position2.getX()][position1.getY()].getPiece();   // En passant captured piece is not the endPosition

                        board.getField()[position2.getX()][position2.getY()].setPiece(piece);
                        board.getField()[position1.getX()][position1.getY()].setPiece(null);
                        board.getField()[position2.getX()][position1.getY()].setPiece(null);        // In between Pos 1 and pos2 (the piece right next to pos1) = null: EnPassant

                        game.getTurns().add(new Turn(0, position1, position2,piece, capturedPiece, 'e'));
                        if (!simulation) {
                            for (Refreshable r : refreshables) {
                                r.refreshAfterEnPassant(position1, position2, piece, board);
                            }
                        }
                    } else if (p.getChar() == 'c') {           // Castle
                        Piece rook;
                        board.getField()[position2.getX()][position2.getY()].setPiece(piece);
                        board.getField()[position1.getX()][position1.getY()].setPiece(null);

                        //short castle
                        if (position2.getX() > position1.getX()) {
                            rook = board.getField()[position2.getX() + 1][position2.getY()].getPiece();
                            board.getField()[position2.getX() - 1][position2.getY()].setPiece(rook);
                            board.getField()[position2.getX() + 1][position2.getY()].setPiece(null);
                        } else {          //long castle
                            rook = board.getField()[position2.getX() - 2][position2.getY()].getPiece();
                            board.getField()[position2.getX() + 1][position2.getY()].setPiece(rook);
                            board.getField()[position2.getX() - 2][position2.getY()].setPiece(null);
                        }
                        game.getTurns().add(new Turn(0, position1, position2,piece, capturedPiece, 'c'));
                        if (!simulation) {
                            for (Refreshable r : refreshables) {
                                r.refreshAfterCastle(position1, position2, piece, rook, board);
                            }
                        }
                    } else {
                        board.getField()[position2.getX()][position2.getY()].setPiece(piece);       // Normal Move
                        board.getField()[position1.getX()][position1.getY()].setPiece(null);
                        game.getTurns().add(new Turn(0, position1, position2,piece, capturedPiece, 'n'));
                        if (!simulation) {
                            for (Refreshable r : refreshables) {
                                r.refreshAfterNormalMove(position1, position2, piece);
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
        for (Refreshable r : refreshables) {
            r.refreshAfterMoveFinished();
        }
    }

    /**
     * It Takes the last turn from the gameList of Turns and undo it (including castling, en passant and promotion)
     */
    public void undoMove() {
        Game game = rootService.currentGame;
        Board board = game.getBoard();

        Turn lastTurn = game.getTurns().getLast();
        Piece capturedPiece = lastTurn.getCapturedPiece();

        Piece movedPiece = lastTurn.getMovedPiece();

        int startPosX = lastTurn.getStartPos().getX();   // last Move starting Position
        int startPosY = lastTurn.getStartPos().getY();

        int endPosX = lastTurn.getTargetPos().getX();
        int endPosY = lastTurn.getTargetPos().getY();

        if(lastTurn.getRule() == 'e'){         // En Passant
            board.getField()[endPosX][endPosY].setPiece(null);
            board.getField()[startPosX][startPosY].setPiece(movedPiece);
            board.getField()[endPosX][startPosY].setPiece(capturedPiece);        // In between pos1 and pos2 (the piece right next to pos1) = null: EnPassant

        }else if(lastTurn.getRule() == 'c'){           // Castle
            Piece rook;
            board.getField()[startPosX][startPosY].setPiece(movedPiece);      //King
            //short castle
            if(endPosX > startPosX){
                rook = board.getField()[endPosX - 1][startPosY].getPiece();   //rook Position after castling

                board.getField()[endPosX+1][endPosY].setPiece(rook);
                board.getField()[endPosX][endPosY].setPiece(null);
                board.getField()[endPosX-1][endPosY].setPiece(null);
            }else{          //long castle
                rook = board.getField()[startPosX - 1][startPosY].getPiece();   //rook Position after castling

                board.getField()[endPosX-2][endPosY].setPiece(rook);
                board.getField()[endPosX-1][endPosY].setPiece(null);
                board.getField()[endPosX][endPosY].setPiece(null);
                board.getField()[endPosX+1][endPosY].setPiece(null);
            }
        }else{
            board.getField()[endPosX][endPosY].setPiece(capturedPiece);       // Normal Move
            board.getField()[startPosX][startPosY].setPiece(movedPiece);
        }
        game.getTurns().removeLast();
    }

    /**
     * if checkPromotion returns true you do not need any further checking. just does the promotion
     * @param buttonArray   to update the gui
     * @param clickedPosition  .getY(): For white 7:Queen, 6:Rook, 5:Bishop, 4:Knight, for black 1:Queen...
     */
    public void doPromotion(Position startPos, Position endPos, Button[][] buttonArray, Position clickedPosition){
        Game game = rootService.currentGame;
        Board board = game.getBoard();

        char p;
        Piece currentPiece =  board.getField()[startPos.getX()][startPos.getY()].getPiece();
        boolean colour = currentPiece.getColour();

        int direction = colour ? 1 : -1;
        Piece promotionPiece;

        if(endPos.equals(clickedPosition)){
            promotionPiece = new Queen(colour);
            p = 'Q';
        }else if(endPos.getX() == clickedPosition.getX() && endPos.getY()+direction == clickedPosition.getY()){
            promotionPiece = new Rook(colour, true);
            p = 'R';
        }else if(endPos.getX() == clickedPosition.getX() && endPos.getY()+2*direction == clickedPosition.getY()){
            promotionPiece = new Bishop(colour);
            p = 'B';
        }else if(endPos.getX() == clickedPosition.getX() && endPos.getY()+3*direction == clickedPosition.getY()){
            promotionPiece = new Knight(colour);
            p = 'K';
        }else{
            promotionPiece = null;
            p = 'X';    // Never used
        }
        if(promotionPiece != null){
            Piece capturedPiece = board.getField()[endPos.getX()][endPos.getY()].getPiece();
            board.getField()[endPos.getX()][endPos.getY()].setPiece(promotionPiece);       // Normal Move
            board.getField()[startPos.getX()][startPos.getY()].setPiece(null);
            game.getTurns().add(new Turn(0, startPos, endPos,currentPiece, capturedPiece, p));
        }
        boolean legal = p != 'X';
        for (Refreshable r : refreshables) {
            r.refreshAfterPromotion(startPos, endPos, buttonArray, colour, board, legal);
        }
        rootService.currentGame.nextPlayer();
    }

    /**
     * checks if a promotion is done in this move (pawn reaching 7 or 0 rank) and if this move is possible
     * @param buttonArray   to update the gui
     * @param list  the possibleMove to check if this list contains the input move
     * @return  true if the move-input is a promotion
     */
    public boolean checkPromotion(Position position1, Position position2, Button[][] buttonArray, List<Position> list) {
        Game game = rootService.currentGame;
        Board board = game.getBoard();

        Piece piece = board.getField()[position1.getX()][position1.getY()].getPiece();
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
