package chessopeningtrainer.entity.game;

/**
 * Currently only contains colour of a player
 * This class is for future updates
 *
 */
public class Player {
    boolean colour;

    /**
     * @param colour the player Colour (White or Black)
     */
    public Player(boolean colour) {
        this.colour = colour;
    }

    public boolean getColour() {
        return colour;
    }
}
