package chessopeningtrainer.entity.board;

public class Position {

    int x;
    int y;
    char z;         // e = enpassant, c = castle,

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
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
