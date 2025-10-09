package chessopeningtrainer.service;

import chessopeningtrainer.entity.game.Game;
import chessopeningtrainer.view.Refreshable;

/**
 * This is the root-class, this whole application runs over rootService, and rootService.currentGame
 */
public class RootService {
    public GameService gameService = new GameService(this);
    public MoveService moveService = new MoveService(this);
    public CheckMoveService checkMoveService = new CheckMoveService(this);
    public SpecialMovesService specialMovesService = new SpecialMovesService(this);
    Game currentGame = null;

    public void addRefreshable(Refreshable r) {
        gameService.addRefreshable(r);
        moveService.addRefreshable(r);
    }
}