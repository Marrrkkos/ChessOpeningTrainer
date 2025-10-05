package chessopeningtrainer.service;

import chessopeningtrainer.entity.board.Board;
import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.entity.pieces.Piece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CheckMoveService extends AbstractRefreshingService{
    private RootService rootService;

    public CheckMoveService(RootService rootService) {
        this.rootService = rootService;
    }
    public List<Position> getFinalPossibleMoves(Position currentPiecePosition){
        Board board = rootService.currentGame.getBoard();

        List<Position> moves = getBasicMoves(currentPiecePosition);    // Filter by enemy/or own Pieces in the Way
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


        if(board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece() != null) {         //Get Basic Piece Moves
            currentPiece = board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece();
            possibleMoves = currentPiece.getBasicPieceMoves(currentPiecePosition);
        }
            for(List<Position> lines : possibleMoves){                  //Filter the Moves, if enemy Piece or own piece is in the way
            for(Position position : lines){
                int positionX = position.getX();
                int positionY = position.getY();

                int currentX = currentPiecePosition.getX();
                int currentY = currentPiecePosition.getY();

                if(board.getBoard()[positionX][positionY].getPiece() != null){
                    if(board.getBoard()[positionX][positionY].getPiece().getColour() != board.getBoard()[currentX][currentY].getPiece().getColour()){       //if enemy piece, you can capture
                        list.add(position);
                    }
                    break;      // Line stops here (go next line)
                }
                list.add(position);
            }
        }
        return list;
    }
    private List<Position> checkForChecks(Position currentPiecePosition, List<Position> moves){
        Board board = rootService.currentGame.getBoard();

        List<Position> list = new ArrayList<>();

        Piece currentPiece = board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece();
        Piece targetPiece;

        for(Position targetPosition : moves){
            targetPiece = board.getBoard()[targetPosition.getX()][targetPosition.getY()].getPiece();        //save targetPiece to undo move

            board.getBoard()[targetPosition.getX()][targetPosition.getY()].setPiece(currentPiece);                 //doMove
            board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].setPiece(null);

            if(!isInCheck(currentPiece.getColour())){       // Colour from own Piece you move at this point
                list.add(targetPosition);
            }


            board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].setPiece(currentPiece);
            board.getBoard()[targetPosition.getX()][targetPosition.getY()].setPiece(targetPiece);                 //undoMove
        }

        return list;
    }

    /**
     *
     * @param kingColour
     * @return returns true, when king is in check and false, when king is not in check
     */
    private boolean isInCheck(boolean kingColour){
        Board board = rootService.currentGame.getBoard();
        Piece enemyPiece;

        Position kingPosition = findKing(kingColour);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board.getBoard()[i][j].getPiece() !=  null){
                    enemyPiece = board.getBoard()[i][j].getPiece();

                    if(enemyPiece.getColour() != kingColour){        // checks if enemy piece can "capture own king"
                        List<Position> currentPossiblePieceMoves = getBasicMoves(new Position(i, j));   //gets all moves from current enemyPiece
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
