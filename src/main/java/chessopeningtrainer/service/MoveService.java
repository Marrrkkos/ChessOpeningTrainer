package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.game.Game;
import chessopeningtrainer.entity.pieces.Piece;
import chessopeningtrainer.view.Refreshable;

import java.util.ArrayList;
import java.util.List;

public class MoveService extends AbstractRefreshingService{
    private RootService rootService;

    public MoveService(RootService rootService) {
        this.rootService = rootService;

    }
    public List<Position> getPossibleMoves(Position currentPiecePosition){
        List<Position> possibleMoves;
        possibleMoves = rootService.checkMoveService.getFinalPossibleMoves(currentPiecePosition);

        for (Refreshable r : refreshables) {
            r.refreshAfterShowPossibleMoves(possibleMoves);
        }
        return possibleMoves;
    }
    public void doMove(Position position1, Position position2, List<Position> moves) {
        Board board = rootService.currentGame.getBoard();
        Piece piece = board.getBoard()[position1.getX()][position1.getY()].getPiece();

        if(moves.contains(position2)) {

            board.getBoard()[position2.getX()][position2.getY()].setPiece(piece);
            board.getBoard()[position1.getX()][position1.getY()].setPiece(null);

            for (Refreshable r : refreshables) {
                r.refreshAfterNormalMove(position1, position2, piece);
            }
        }

    }
}
