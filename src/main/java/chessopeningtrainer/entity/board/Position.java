package chessopeningtrainer.entity.board;

/**
 * Every Field in {@link Board} has a {@link Position}
 * It is a helping class, for manage effective Position collection on the Board,
 * without having to run over all 64 Fields of the board
 */
public class Position {

    int x;
    int y;
    char z;         // e = enPassant, c = castle,

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param z you can specify rules for En-Passant: e, Castle: c and Promotion: Q,R,B,K (chess pieces)
     */
    public Position(char z, int x, int y) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public char getChar() {
        return z;
    }
    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return (char)('a' + x) + "" + (8-y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position other)) return false;
        return this.x == other.x && this.y == other.y;
    }
}
