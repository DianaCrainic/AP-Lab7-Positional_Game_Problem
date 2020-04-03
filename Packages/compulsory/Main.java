package compulsory;

import optional.ManualPlayer;
import optional.RandomPlayer;
import optional.SmartPlayer;
import optional.TimeKeeper;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.setBoard(new Board(16));
        game.addPlayer(new RandomPlayer("Player1"));
        game.addPlayer(new RandomPlayer("Player2"));
        game.addPlayer(new RandomPlayer("Player3"));
        System.out.println("In total there are " + game.getBoard().getSize() + " tokens.");
        TimeKeeper timeKeeper = new TimeKeeper();
        game.start();

    }
}
