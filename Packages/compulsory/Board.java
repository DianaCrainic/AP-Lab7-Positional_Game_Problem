package compulsory;

import java.util.ArrayList;
import java.util.List;

/**
 * Board class:
 * - an instance of this class will contain n tokens
 * (you may consider the numbers from 1 to n).
 */
public class Board {
    int size;
    List<Token> tokens;
    private Game game;

    public Board(int size) {
        this.size = size;
        tokens = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tokens.add(new Token(i));
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public synchronized List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public synchronized Token setTokenForPlayer(int index, Player player) {
        if (index >= tokens.size()) {
            return null;
        }
        if (tokens.get(index).getPlayer() == null) {
            Token token = tokens.get(index);
            token.setPlayer(player);
            tokens.remove(index);
            return token;
        }
        return null;
    }

}
