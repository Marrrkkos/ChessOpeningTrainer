package chessopeningtrainer.entity.game;

import chessopeningtrainer.entity.board.Board;

import java.util.ArrayList;

/**
 * This class contains the entire game played so far in an Array-list of Turns
 */
public class Game {
    ArrayList<Turn> turns;
    Player[] players;
    ArrayList<Turn> currentTurnsSave;
    int turnCounter;
    private Board board;
    int currentPlayer;
    /**
     *
     * @param turns an Arraylist of Turns containing the moves made throughout the game
     * @param currentTurnsSave  This is for Forward/Backward functions, to make life easier for the user. Only
     *                          changes to turns, when new Move out of the current Line is done
     */
    public Game(ArrayList<Turn> turns, ArrayList<Turn> currentTurnsSave, Player[] players) {
        this.turns = turns;
        this.players = players;
        this.currentTurnsSave = currentTurnsSave;
        this.turnCounter = 0;
        this.board = new Board();
        currentPlayer = 0;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public Board getBoard() {
        return board;
    }

    public Player[] getPlayer() {
        return players;
    }
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    public void nextPlayer() {
        this.currentPlayer = (1 + currentPlayer) % players.length;
    }
    public ArrayList<Turn> getCurrentTurnsSave() {
        return currentTurnsSave;
    }

    public void setTurns(ArrayList<Turn> turns) {
        this.turns = turns;
    }

    public void setPlayer(Player[] player) {
        this.players = player;
    }

    public void setCurrentTurnsSave(ArrayList<Turn> currentTurnsSave) {
        this.currentTurnsSave = currentTurnsSave;
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
