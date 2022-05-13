package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.exception.GameInterruptedException;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

public class RandomGame implements Game {

    private static final int DURATION = 2;
    private static final TimeUnit UNIT = TimeUnit.MINUTES;

    private Map<String, String> playersCharacter = new ConcurrentHashMap<>();
    private final List<Player> players;
    private final List<String> availableCharacters;
    private Turn currentTurn;


    private final static String YES = "Yes";
    private final static String NO = "No";

    public RandomGame(List<Player> players, List<String> availableCharacters) {
        this.availableCharacters = new ArrayList<>(availableCharacters);
        this.players = new ArrayList<>(players.size());
        players.forEach(this::addPlayer);

    }

    public void addPlayer(Player player) {
        // TODO: Add test to ensure that player has not been added to the lists when failed to obtain suggestion
        Future<String> maybeCharacter = player.suggestCharacter();
        try {
            String character = maybeCharacter.get(DURATION, UNIT);
            this.players.add(player);
            this.availableCharacters.add(character);
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Player did not suggest a charatern within %d %s".formatted(DURATION, UNIT));
        }
    }

    @Override
    public boolean makeTurn() {
        Player currentGuesser = currentTurn.getGuesser();
        Set<String> answers;
        String guessersName;
        guessersName = currentGuesser.getName();
        try {
            if (currentGuesser.isReadyForGuess().get()) {
                String guess = currentGuesser.getGuess().get();
                answers = currentTurn.getOtherPlayers().stream().parallel()
                        .map(player -> {
                            try {
                                return player.answerGuess(guess, this.playersCharacter.get(guessersName)).get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                throw new GameInterruptedException("Failed to obtain a player's name", e);
                            }
                        })
                        .collect(Collectors.toSet());
                long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
                long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();

                boolean win = positiveCount > negativeCount;

                if (win) {
                    players.remove(currentGuesser);
                }
                return win;

            } else {
                String question = currentGuesser.getQuestion().get();
                answers = currentTurn.getOtherPlayers().stream().parallel()
                        .map(player -> {
                            try {
                                return player.answerQuestion(question, this.playersCharacter.get(guessersName)).get();
                            } catch (InterruptedException | ExecutionException e) {
                                throw new GameInterruptedException("Failed to obtain a player's questions", e);
                            }
                        })
                        .collect(Collectors.toSet());
                long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
                long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();
                return positiveCount > negativeCount;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new GameInterruptedException("Failed to obtain a player's guesses", e);
        }

    }

    private void assignCharacters() {

        players.stream().map(Player::setName).parallel().map(f -> {
            try {
                f.get(DURATION, UNIT);
                return f.get(DURATION, UNIT);
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                // TODO: Add custom runtime exception implementation
                throw new GameInterruptedException("Failed to obtain a player's name", e);
            } catch (TimeoutException e) {
                throw new GameInterruptedException("Player did not provide a name within %d %s".formatted(DURATION, UNIT));
            }
        }).forEach(name -> this.playersCharacter.put(name, this.getRandomCharacter(name)));

    }

    @Override
    public void initGame() {
        this.assignCharacters();
        this.currentTurn = new TurnImpl(this.players);

    }

    @Override
    public void play() {

        boolean gameStatus = true;
        while (gameStatus) {
            boolean turnResult = this.makeTurn();

            while (turnResult) {
                turnResult = this.makeTurn();
            }
            this.changeTurn();
            gameStatus = !this.isFinished();
        }
    }

    @Override
    public boolean isFinished() {
        return players.size() == 1;
    }

    private String getRandomCharacter(String name) {
        for (var a : players
             ) {
            System.out.println(a.getName());

        }
        List<String> collect = players.stream().map(Player::getName).collect(Collectors.toList());
        int i = collect.indexOf(name);
        int randomPos = getRandomPos();
        while (players.get(i).getSuggestCharacter().equals(availableCharacters.get(randomPos))) {
            randomPos = getRandomPos();
        }
        // TODO: Ensure player never receives own suggested character
        return this.availableCharacters.remove(randomPos);
    }

    private int getRandomPos() {
        return (int) (Math.random() * this.availableCharacters.size());
    }

    @Override
    public void changeTurn() {
        this.currentTurn.changeTurn();
    }

}