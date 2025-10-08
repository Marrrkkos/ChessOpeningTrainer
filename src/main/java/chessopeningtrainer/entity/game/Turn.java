package chessopeningtrainer.entity.game;

import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Piece;

public class Turn {
    Position startPos;
    Position targetPos;
    int moveID;
    Piece capturedPiece;
    char rule;
    public Turn(int moveID, Position startPos, Position targetPos, Piece capturedPiece, char rule) {
        this.startPos = new Position(startPos.getX(), startPos.getY());
        this.targetPos = new Position(targetPos.getX(), targetPos.getY());
        this.moveID = moveID;
        this.capturedPiece = capturedPiece;
        this.rule = rule;
    }

    public Position getTargetPos() {
        return targetPos;
    }

    public Position getStartPos() {
        return startPos;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }
    public char getRule() {
        return rule;
    }
}
