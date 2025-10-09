package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.game.Turn;
import chessopeningtrainer.entity.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class SpecialMovesService extends AbstractRefreshingService {
    private RootService rootService;

    public SpecialMovesService(RootService rootService) {
        this.rootService = rootService;
    }

    public List<Position> checkForSpecialMoves(Position position, List<Position> moves) {
        Board board = rootService.currentGame.getBoard();
        Piece currentPiece = board.getBoard()[position.getX()][position.getY()].getPiece();
        if(currentPiece != null) {
            if (currentPiece.getID() == 0) {
                moves = checkPawnMoves(position, currentPiece.getColour());
            }
            if (currentPiece.getID() == 5) {
                checkCastle(position, currentPiece, moves);
            }
        }


        return moves;
    }
    private List<Position> checkPawnMoves(Position currentPiecePosition, boolean colour) {
        Board board = rootService.currentGame.getBoard();

        Piece piece;
        List<Position> moves = new ArrayList<>();
        int direction = colour ? -1 : 1;

        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();

            if (board.getBoard()[posX][posY + direction].getPiece() == null) {    // One forward
                moves.add(new Position(posX, posY + direction));
            }
            if(posX+1 < 8) {
                piece = board.getBoard()[posX + 1][posY + direction].getPiece();
                if (piece != null && piece.getColour() != colour) {               // capture right
                    moves.add(new Position(posX + 1, posY + direction));
                }else if(checkEnPassant(currentPiecePosition, colour, 1)){
                    moves.add(new Position('e',posX + 1, posY + direction));
                }
            }
            if(posX-1 >= 0) {
                piece = board.getBoard()[posX - 1][posY + direction].getPiece();
                if (piece != null && piece.getColour() != colour) {        // capture left
                    moves.add(new Position(posX - 1, posY + direction));
                }else if(checkEnPassant(currentPiecePosition, colour, -1)){
                    moves.add(new Position('e',posX - 1, posY + direction));
                }

            }
            if((posY + 2 * direction) <= 7 && (posY + 2 * direction) >= 0) {
                piece = board.getBoard()[posX][posY + 2 * direction].getPiece();
                if (direction == 1 && posY == 1 && piece == null) {              //first pawn move can go 2 moves
                    moves.add(new Position(posX, posY + 2 * direction));
                }
                if (direction == -1 && posY == 6 && piece == null) {
                    moves.add(new Position(posX, posY + 2 * direction));
                }
            }



        return moves;
    }

    /**
     *
     * @param currentPiecePosition
     * @param colour
     * @param captureDirection  1 = capture right, -1 = capture left
     * @return
     */
    private boolean checkEnPassant(Position currentPiecePosition, boolean colour, int captureDirection) {
        int posX = currentPiecePosition.getX();
        int posY = currentPiecePosition.getY();

        int direction = colour ? -1 : 1;    // Inverted, cause enemy Move
        Position enPassantStartPos1 = new Position(posX + captureDirection, posY + 2 * direction);          // these are the positions, the last move has to be, in order to get the enPassant working
        Position enPassantEndPos1 = new Position(posX + captureDirection, posY);

        ArrayList<Turn> gameTurns = rootService.currentGame.getTurns();
        if(!gameTurns.isEmpty()) {
            Turn turner = gameTurns.getLast();
            if(turner.getMovedPiece()==null){
                return false;
            }
            if(turner.getMovedPiece().getID() == 0) {

                if (colour && posY == 3) {   // White and on 5th rank
                    if (turner.getStartPos().equals(enPassantStartPos1) && turner.getTargetPos().equals(enPassantEndPos1)) {
                        return true;
                    }
                }
                if (!colour && posY == 4) {  // Black and on 4th rank
                    return turner.getStartPos().equals(enPassantStartPos1) && turner.getTargetPos().equals(enPassantEndPos1);
                }
            }
        }
        return false;
    }
    private void checkCastle(Position currentPiecePosition, Piece currentPiece, List<Position> moves) {
        Board board = rootService.currentGame.getBoard();
        if(!currentPiece.getHasMoved()) {
            int posX = currentPiecePosition.getX();
            int posY = currentPiecePosition.getY();

            Piece shortRook = board.getBoard()[posX + 3][posY].getPiece();        //short site castle
            Piece longRook = board.getBoard()[posX - 4][posY].getPiece();         // long site castle

            if (board.getBoard()[posX + 1][posY].getPiece() == null && board.getBoard()[posX + 2][posY].getPiece() == null) {                //All places between rook and king must be empty
                if (shortRook != null && !shortRook.getHasMoved()) {        // Short Castle Rook and King no move
                    moves.add(new Position('c', posX + 2, posY));
                }
            }
            if (board.getBoard()[posX - 1][posY].getPiece() == null && board.getBoard()[posX - 2][posY].getPiece() == null && board.getBoard()[posX - 2][posY].getPiece() == null) {   //All places between rook and king must be empty
                if (longRook != null && !longRook.getHasMoved()) {           // Long Castle Rook and King no move
                    moves.add(new Position('c', posX - 2, posY));
                }
            }
        }
    }
}
