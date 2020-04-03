package compulsory;

import java.util.ArrayList;
import java.util.List;

/**
 * Player interface:
 * - each player will have a name.
 * - this class will implement the Runnable interface. In the run method the player will repeatedly extract one token from the board.
 */
public abstract class Player implements Runnable {
    protected String name;
    protected PlayerType type;
    protected Player next;
    protected Game game;
    protected int totalTokens;
    protected int id;
    protected static int totalId;
    protected List<Token> tokens = new ArrayList<Token>();
    protected boolean active = false;

    public String getName() {
        return name;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setNext(Player next) {
        this.next = next;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public synchronized void setActive() {
        active = true;
        notifyAll();
    }

    public synchronized void setInactive() {
        active = false;
    }

    public abstract boolean play();


    public Token getTokenFromBoard(int index) {
        Token token = game.getBoard().setTokenForPlayer(index, this);
        if (token != null) {
            tokens.add(token);
            return token;
        }
        return null;
    }

    public int computeScore(List<Token> tokenList) {
        int score = 0;

        int[] frequencies = new int[totalTokens];

        for (Token token : tokenList) {
            frequencies[token.getNumber()]++;
        }

        for (int index = 1; index < totalTokens; index++) {
            for (int start = 0; start < totalTokens; start++) {
                int currentScore = 0;
                for (int tokenValue = start; tokenValue < totalTokens; tokenValue += index) {
                    if (frequencies[tokenValue] == 0) {
                        break;
                    }
                    currentScore++;
                }

                if (score < currentScore) {
                    score = currentScore;
                }
            }
        }
        return score;
    }

    public int computeScore() {
        return computeScore(tokens);
    }

}
