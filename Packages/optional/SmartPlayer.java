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

        if (numberOfTokensLeft == 0 | game.isOver()) {
            return false;
        }

        System.out.println(name + ": I have received permission to take my turn. " +
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

        boolean attack = false;
        if (chance < 0.66) {
            attack = true;
        }

        int tokenVal;
        if (attack) {
            tokenVal = getBestTokenVal(tokens, 2);
        } else {
            tokenVal = getBestBlockTokenVal();
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
            if (attack) {
                tokenVal = getBestBlockTokenVal();
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
        int maxPossibleScore = computeScore(givenTokens);

        int[] frequencies = new int[totalTokens];

        for (Token token : givenTokens) {
            frequencies[token.getNumber()]++;
        }

        int[] isAvailable = new int[totalTokens];

        for (Token token : game.getBoard().getTokens()) {
            isAvailable[token.getNumber()]++;
        }

        List<Integer> bestOptions = new ArrayList<>();

        for (int ratio = 1; ratio < totalTokens; ratio++) {
            for (int start = 0; start < totalTokens; start++) {
                int ratioScore = 0;
                int futureMoves = 0;
                List<Integer> currentOptions = new ArrayList<>();

                for (int tokenVal = start; tokenVal < totalTokens; tokenVal += ratio) {
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
                    ratioScore++;
                }

                if (maxPossibleScore < ratioScore) {
                    maxPossibleScore = ratioScore;
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


    private int getBestBlockTokenVal() {
        List<Player> others = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            if (this == player) {
                continue;
            }
            others.add(player);
        }

        others.sort(Comparator.comparingInt(Player::computeScore));

        int tokenValue = -1;
        for (Player player : others) {
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
