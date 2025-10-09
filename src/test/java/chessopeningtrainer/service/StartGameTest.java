package chessopeningtrainer.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StartGameTest {
    RootService rootService = new RootService();
    @Test
    void startGame() {
        rootService.gameService.startGame();
        assertNotNull(rootService.currentGame);
        assertNotNull(rootService.currentGame.getBoard());
        assertNotNull(rootService.currentGame.getBoard().getBoard()[0][0].getPiece());
    }
}
