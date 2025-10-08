package chessopeningtrainer.view;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Piece;
import javafx.scene.control.Button;

import java.util.List;

public interface Refreshable {
    default void refreshAfterGameStart(Board board) {}
    default void refreshAfterNormalMove(Position position1, Position position2, Piece piece) {}
    default void refreshAfterEnPassant(Position startPos, Position endPos, Piece piece, Board board) {}
    default void refreshAfterCastle(Position startPos, Position endPos, Piece king, Piece Rook, Board board){}
    default void refreshAfterShowPromotion(Position position1, Position position2, Button[][] buttonArray, boolean colour) {}
    default void refreshAfterPromotion(Position position1, Position position2, Button[][] buttonArray, boolean colour, Board board, boolean legal){}
    default void refreshAfterShowPossibleMoves(List<Position> possibleMoves){}

}
