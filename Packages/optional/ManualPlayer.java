package optional;

import compulsory.Player;
import compulsory.PlayerType;
import compulsory.Token;

import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * ManualPlayer class
 * A "manual" player will use the keyboard
 */
public class ManualPlayer extends Player {

    int selectedToken;

    public ManualPlayer(String name){
        this.name = name;
        this.type = PlayerType.MANUAL;
        this.id = totalId;
        totalId++;
    }

    @Override
    public synchronized boolean play() {
        while (!active && game.getBoard().getTokens().size() != 0){
            System.out.println(name + " is waiting for his turn");
            try {
                wait();
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }

        int numberOfTokensLeft = game.getBoard().getTokens().size();

        if(numberOfTokensLeft == 0 || game.isOver()){
            return false;
        }

        System.out.println(name + ": it's my turn. There are " + numberOfTokensLeft + " tokens left");

        selectedToken = -1;
        Scanner scanner = new Scanner(System.in);
        selectedToken = scanner.nextInt();

        while (selectedToken == -1 && !game.isOver()){
            try{
                sleep(300);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
        if (game.isOver()){
            return false;
        }

        Token token = getTokenFromBoard(selectedToken);
        System.out.println("Player " + name + " has taken the token " + selectedToken
                                + ", " + game.getBoard().getTokens().size() + " tokens left.");
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
