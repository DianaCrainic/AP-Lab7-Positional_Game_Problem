package optional;

import compulsory.Game;

import static java.lang.Thread.sleep;

/**
 * TimeKeeper class:
 * - timekeeper thread that runs concurrently with the player threads, as a daemon.
 * - this thread will display the running time of the game and it will stop the game
 * if it exceeds a certain time limit.
 */
public class TimeKeeper implements Runnable {
    private Game game;
    private int totalTime;
    private int remainingTime;

    @Override
    public void run() {
        long startTime = System.nanoTime();

        while (remainingTime > 0 && game.getBoard().getTokens().size() != 0 && !game.isOver()){
            remainingTime = totalTime - (int)(System.nanoTime() - startTime)/1_000_000_000;
            System.out.println("Remaining Time: " + remainingTime );

            try {
                sleep(200);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
        game.setOver(true);
    }
}
