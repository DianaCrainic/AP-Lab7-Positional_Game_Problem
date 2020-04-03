package optional;

import compulsory.Player;
import compulsory.PlayerType;
import compulsory.Token;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * SmartPlayer class:
 * A "smart" player should try to extend its largest arithmetic progression,
 * while not allowing others to extend theirs
 */
public class SmartPlayer extends Player {

    public SmartPlayer(String name) {
        this.name = name;
        this.type = PlayerType.SMART;
        this.id = totalId;
        totalId++;
    }

    @Override
    public boolean play() {
        while (!active & game.getBoard().getTokens().size() != 0) {
            System.out.println(name + " is waiting for his turn");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int numberOfTokensLeft = game.getBoard().getTokens().size();

        if (numberOfTokensLeft == 0 || game.isOver()) {
            return false;
        }

        System.out.println(name + ": it's my turn. " +
                "There are " + numberOfTokensLeft + " tokens left.");
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (game.isOver()) {
            return false;
        }

        Token chosenToken;
        double chance = Math.random();
        int tokenVal;
        //There are 3 players, 0.66 chances for one player
        if (chance < 0.66) {
            tokenVal = getBestTokenVal(tokens, 2);
        } else {
            tokenVal = getBestTokenVal();
        }

        if (tokenVal != -1) {
            int index = 0;
            for (Token token : game.getBoard().getTokens()) {
                if (token.getNumber() == tokenVal) {
                    break;
                }
                index++;
            }

            chosenToken = getTokenFromBoard(index);
        } else {
            if (chance < 0.66) {
                tokenVal = getBestTokenVal();
            } else {
                tokenVal = getBestTokenVal(tokens, 2);
            }

            if (tokenVal != -1) {
                int index = 0;
                for (Token token : game.getBoard().getTokens()) {
                    if (token.getNumber() == tokenVal) {
                        break;
                    }
                    index++;
                }

                chosenToken = getTokenFromBoard(index);
            } else {
                chosenToken = getTokenFromBoard((int) (Math.random() * game.getBoard().getTokens().size()));
            }
        }
        System.out.println("Player " + name + " has taken the token " + chosenToken
                                + ", " + game.getBoard().getTokens().size() + " tokens left.");
        setInactive();
        System.out.println("The next player is " + next.getName());
        next.setActive();
        return true;
    }


    private int getBestTokenVal(List<Token> givenTokens, int consideredMoves) {
        int maximumScore = computeScore(givenTokens);
        int[] frequencies = new int[totalTokens];
        int[] isAvailable = new int[totalTokens];
        List<Integer> bestOptions = new ArrayList<>();
        for (Token token : givenTokens) {
            frequencies[token.getNumber()]++;
        }
        for (Token token : game.getBoard().getTokens()) {
            isAvailable[token.getNumber()]++;
        }
        for (int index = 1; index < totalTokens; index++) {
            for (int start = 0; start < totalTokens; start++) {
                int score = 0;
                int futureMoves = 0;
                List<Integer> currentOptions = new ArrayList<>();

                for (int tokenVal = start; tokenVal < totalTokens; tokenVal += index) {
                    if (frequencies[tokenVal] == 0) {
                        if (isAvailable[tokenVal] == 1) {
                            futureMoves++;
                            if (futureMoves > consideredMoves)
                                break;
                        } else {
                            break;
                        }
                        currentOptions.add(tokenVal);
                    }
                    score++;
                }
                if (maximumScore < score) {
                    maximumScore = score;
                    bestOptions = currentOptions;
                }
            }
        }

        int randomTokenIndex = (int) (Math.random() * bestOptions.size());
        int tokenVal = -1;
        if (bestOptions.size() != 0) {
            tokenVal = bestOptions.get(randomTokenIndex);
        }
        return tokenVal;
    }


    private int getBestTokenVal() {
        List<Player> players = new ArrayList<>();
        int tokenValue = -1;
        for (Player player : game.getPlayers()) {
            if (this == player) {
                continue;
            }
            players.add(player);
        }
        players.sort(Comparator.comparingInt(Player::computeScore));
        for (Player player : players) {
            tokenValue = getBestTokenVal(player.getTokens(), 1);
            if (tokenValue != -1) {
                break;
            }
        }
        return tokenValue;
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
