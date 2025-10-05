package chessopeningtrainer.view;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Piece;

import java.util.List;

public interface Refreshable {
    default void refreshAfterGameStart(Board board) {}
    default void refreshAfterFirstClick() {}
    default void refreshAfterNormalMove(Position position1, Position position2, Piece piece) {}
    default void refreshAfterEnPassant() {}
    default void refreshAfterCastle(){}
    default void refreshAfterShowPromotion(){}
    default void refreshAfterPromotion(){}
    default void refreshAfterShowPossibleMoves(List<Position> possibleMoves){}

}
