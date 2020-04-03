package compulsory;

/**
 * Token class:
 * - an instance of this class will hold a number from 1 to m
 * - consider the case when a token may be blank,
 * meaning that it can take the place of any number.
 */
public class Token {
    private int number;
    private Player player;

    public Token(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
