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
        List<List<Position>> possibleMoves;
        Piece currentPiece;

        if(board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece() != null) {         //Get Basic Piece Moves
            currentPiece = board.getBoard()[currentPiecePosition.getX()][currentPiecePosition.getY()].getPiece();
            possibleMoves = currentPiece.getBasicPieceMoves(currentPiecePosition);

            List<Position> moves = checkForPieces(currentPiecePosition, possibleMoves);    // Filter by enemy/or own Pieces in the Way
            return checkForChecks(currentPiece, moves);         // Filter checks to get final possible Moves
        }
        return new ArrayList<>();
    }
    private List<Position> checkForPieces(Position currentPiecePosition, List<List<Position>> possibleMoves){
        List<Position> list = new ArrayList<>();

        Board board = rootService.currentGame.getBoard();

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
    private List<Position> checkForChecks(Piece currentPiece, List<Position> moves){

        return moves;
    }

}
