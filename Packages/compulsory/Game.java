package compulsory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Game class:
 * simulate the game using a thread for each player
 */
public class Game {
    private Board board;
    private final List<Player> players = new ArrayList<>();
    private boolean over = false;
    private int maxScore;
    private int timeLimit;

    public Game() {
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
        board.setGame(this);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setGame(this);
    }


    /**
     * start method: method that will start the game: start one thread for each player
     */
    public void start() {
        //setting the next player
        for (int i = 0; i < players.size() - 1; i++) {
            players.get(i).setNext(players.get(i + 1));
        }
        //in the case of the last player from the list, the next player is the first from the list
        players.get(players.size() - 1).setNext(players.get(0));

        Thread[] threads = new Thread[players.size()];

        for (int i = 0; i < players.size(); i++) {
            players.get(i).setInactive();
            threads[i] = new Thread(players.get(i));
            threads[i].start();
        }

        try {
            sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        players.get(0).setActive();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        displayScore();
    }

    public void displayScore() {
        for (Player player : players) {
            player.getTokens().sort(Comparator.comparingInt(Token::getNumber));
            System.out.println(player.getName() + " has " + player.getTokens().size()
                    + " tokens: " + player.getTokens().toString());
        }
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
}
