package optional;

import compulsory.Player;
import compulsory.PlayerType;
import compulsory.Token;

/**
 * RandomPlayer class
 *  A random player will choose tokens
 * in a random way
 *
 */
public class RandomPlayer extends Player {

    public RandomPlayer(String name) {
        this.name = name;
        this.type = PlayerType.RANDOM;
    }

    public synchronized boolean play() {
        while (!active && game.getBoard().getTokens().size() != 0) {
            System.out.println(name + " is waiting for his turn");
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        int numberOfTokensLeft = game.getBoard().getTokens().size();
        System.out.println(name + ": it's my turn. There are " + numberOfTokensLeft + " tokens left");

        if (numberOfTokensLeft == 0 || game.isOver()) {
            return false;
        }

        Token token = getTokenFromBoard((int) (Math.random() * game.getBoard().getTokens().size()));
        System.out.println(name + " has taken the token: " + token
                + ". There are " + game.getBoard().getTokens().size() + " tokens left.");
        setInactive();
        System.out.println("The next player is " + next.getName());
        next.setActive();
        return true;
    }

    @Override
    public void run() {
        System.out.println(name + " is in the game. The next players is " + next.getName());

        while (game.getBoard().getTokens().size() != 0) {
            if (game.getBoard().getTokens().size() == 0) {
                break;
            }
            if (!play())
                break;
        }
        next.setActive();
    }

}
