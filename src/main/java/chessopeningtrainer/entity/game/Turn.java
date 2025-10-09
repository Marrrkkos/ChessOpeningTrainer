package chessopeningtrainer.entity.game;

import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Piece;

/**
 * Represents a single chess move in a game.
 */
public class Turn {
    Position startPos;
    Position targetPos;
    int moveID;
    Piece capturedPiece;
    char rule;
    Piece movedPiece;

    public Turn(int moveID, Position startPos, Position targetPos,Piece movedPiece, Piece capturedPiece, char rule) {
        this.startPos = new Position(startPos.getX(), startPos.getY());
        this.targetPos = new Position(targetPos.getX(), targetPos.getY());
        this.moveID = moveID;
        this.capturedPiece = capturedPiece;
        this.rule = rule;
        this.movedPiece = movedPiece;
    }

    public Position getTargetPos() {
        return targetPos;
    }

    public Position getStartPos() {
        return startPos;
    }
    public Piece getMovedPiece() {
        return movedPiece;
    }
    public Piece getCapturedPiece() {
        return capturedPiece;
    }
    public char getRule() {
        return rule;
    }
}
