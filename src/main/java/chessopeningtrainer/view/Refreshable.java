package chessopeningtrainer.view;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Piece;
import javafx.scene.control.Button;

import java.util.List;

/**
 * Interface for UI components that react to game state updates.
 */
public interface Refreshable {
    /**
     * Called to remove possiblePoints on a chess Board
     */
    default void refreshAfterMoveFinished(){}
    /**
     * Called after a new game starts.
     * @param board the current game board
     */
    default void refreshAfterGameStart(Board board) {}

    /**
     * Called after a normal move is made.
     * @param position1 the starting position
     * @param position2 the ending position
     * @param piece the moved piece
     */
    default void refreshAfterNormalMove(Position position1, Position position2, Piece piece) {}

    /**
     * Called after an en passant move.
     * @param startPos the starting position
     * @param endPos the ending position
     * @param piece the capturing pawn
     * @param board the current game board
     */
    default void refreshAfterEnPassant(Position startPos, Position endPos, Piece piece, Board board) {}

    /**
     * Called after a castle move.
     * @param startPos the king's starting position
     * @param endPos the king's ending position
     * @param king the king piece
     * @param rook the rook piece
     * @param board the current game board
     */
    default void refreshAfterCastle(Position startPos, Position endPos, Piece king, Piece rook, Board board) {}

    /**
     * Called when showing promotion options.
     * @param position1 the pawn's starting position
     * @param position2 the pawn's target position
     * @param buttonArray the board buttons
     * @param colour the player's colour
     */
    default void refreshAfterShowPromotion(Position position1, Position position2, Button[][] buttonArray, boolean colour) {}

    /**
     * Called after a promotion move is completed.
     * @param position1 the pawn's starting position
     * @param position2 the pawn's target position
     * @param buttonArray the board buttons
     * @param colour the player's colour
     * @param board the current game board
     * @param legal true if the move is legal
     */
    default void refreshAfterPromotion(Position position1, Position position2, Button[][] buttonArray, boolean colour, Board board, boolean legal) {}

    /**
     * Called when possible moves are displayed with circles
     * @param possibleMoves list of possible move positions
     */
    default void refreshAfterShowPossibleMoves(List<Position> possibleMoves) {}
}
