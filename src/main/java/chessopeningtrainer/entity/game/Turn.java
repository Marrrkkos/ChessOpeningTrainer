package chessopeningtrainer.entity.game;

public class Turn {
    public int ZugNummer;
    public String a1;
    public String b1;
    public String notationHandled;
    int ZugID;
    boolean playerColour;
    public Turn(int ZugID, int ZugNummer, String a1, String b1, String notationHandled) {
        this.ZugNummer = ZugNummer;
        this.a1 = a1;
        this.b1 = b1;
        this.ZugID = ZugID;
        this.notationHandled = notationHandled;
    }
}
