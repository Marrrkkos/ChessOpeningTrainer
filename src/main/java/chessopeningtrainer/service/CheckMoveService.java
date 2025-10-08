package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class CheckMoveService extends AbstractRefreshingService{
    private RootService rootService;

    public CheckMoveService(RootService rootService) {
        this.rootService = rootService;
    }
    public List<Position> getFinalPossibleMoves(Position currentPiecePosition){
        Board board = rootService.currentGame.getBoard();
        if(board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece() == null) {     // no piece at this position
            return new ArrayList<>();
        }
        List<Position> moves = getBasicMoves(currentPiecePosition);    // Filter by enemy/or own Pieces in the Way
        moves = rootService.specialMovesService.checkForSpecialMoves(currentPiecePosition, moves);      //Filter by En-Pasant, Pawn Moves, Castle
        return checkForChecks(currentPiecePosition, moves);         // Filter checks to get final possible Moves

    }

    /**
     * gets you the basic piece moves, filtered from own, or enemy pieces. but it is not filtered from checks
     * @param currentPiecePosition  the position of the piece you want the possible moves for
     * @return  list of possible moves you can play, not filtered from checks
     */
    private List<Position> getBasicMoves(Position currentPiecePosition){
        List<Position> list = new ArrayList<>();
        List<List<Position>> possibleMoves = new ArrayList<>();
        Piece currentPiece;

        Board board = rootService.currentGame.getBoard();
            currentPiece = board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece();       //Get Basic Piece Moves
            possibleMoves = currentPiece.getBasicPieceMoves(currentPiecePosition);
            for(List<Position> lines : possibleMoves){                  //Filter the Moves, if enemy Piece or own piece is in the way
            for(Position position : lines){
                int positionX = position.getX();
                int positionY = position.getY();

                int currentX = currentPiecePosition.getX();
                int currentY = currentPiecePosition.getY();

                Piece piece = board.getBoard()[positionX][positionY].getPiece();

                if(piece != null){
                    if(piece.getColour() != board.getBoard()[currentX][currentY].getPiece().getColour()){       //if enemy piece, you can capture
                        list.add(position);
                    }
                    break;      // Line stops here (go next (chess)-line)
                }
                list.add(position);
            }
        }
        return list;
    }
    private List<Position> checkForChecks(Position currentPiecePosition, List<Position> currentPiecePossibleMoves){
        Board board = rootService.currentGame.getBoard();

        List<Position> list = new ArrayList<>();

        Piece currentPiece = board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece();

        for(Position targetPosition : currentPiecePossibleMoves){

            rootService.moveService.doMove(currentPiecePosition, targetPosition, currentPiecePossibleMoves, true);

            if(!isInCheck(currentPiece.getColour())){       // Colour from own Piece you move at this point
                list.add(targetPosition);
            }

            rootService.moveService.undoMove(targetPosition);
        }

        if(currentPiece.getID() == 5 && !list.isEmpty()){
            removeCastleChecks(list, currentPiecePosition, currentPiece);
        }

        return list;
    }
    private void removeCastleChecks(List<Position> possibleMoves, Position kingPosition, Piece king) {
        possibleMoves.removeIf(possiblePosition -> {
            if (possiblePosition.getChar() != 'c') {
                return false; // no castling move
            }

            if (isInCheck(king.getColour())) {      // king cant castle while in check
                return true;
            }

            // king cant castle through check
            Position checkPosition;
            if (possiblePosition.getX() > kingPosition.getX()) {
                checkPosition = new Position(possiblePosition.getX() - 1, possiblePosition.getY()); // short castle
            } else {
                checkPosition = new Position(possiblePosition.getX() + 1, possiblePosition.getY()); // long castle
            }

            //
            return !possibleMoves.contains(checkPosition);
        });
    }
    /**
     *
     * @param kingColour own king colour
     * @return returns true, when king is in check and false, when king is not in check
     */
    private boolean isInCheck(boolean kingColour){
        Board board = rootService.currentGame.getBoard();
        Piece enemyPiece;

        Position kingPosition = findKing(kingColour);
        System.out.println(kingPosition);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board.getBoard()[i][j].getPiece() !=  null){
                    enemyPiece = board.getBoard()[i][j].getPiece();

                    if(enemyPiece.getColour() != kingColour){        // checks if enemy piece can "capture own king"
                        List<Position> currentPossiblePieceMoves = getBasicMoves(new Position(i, j));   //gets all moves from current enemyPiece
                        currentPossiblePieceMoves = rootService.specialMovesService.checkForSpecialMoves(new Position(i, j), currentPossiblePieceMoves);
                        if(currentPossiblePieceMoves.contains(kingPosition)){           // checks if kingPosition and the enemyPiece possibleMoves are the same
                            return true;        // is in check
                        }

                    }
                }
            }
        }
        return false;
    }
    private Position findKing(boolean colour){
        Board board = rootService.currentGame.getBoard();
        Position position;
        Piece piece;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board.getBoard()[i][j].getPiece() !=  null){
                    piece = board.getBoard()[i][j].getPiece();

                    if(piece.getColour() == colour && piece.getID() == 5){
                        position = board.getBoard()[i][j].getPosition();
                        return position;
                    }
                }
            }
        }
        return null;
    }
}
