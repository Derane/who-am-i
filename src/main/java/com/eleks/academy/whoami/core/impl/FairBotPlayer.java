package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;

import java.util.ArrayList;
import java.util.List;

public class FairBotPlayer implements Player {


    private String name;
    private List<String> availableQuestions;
    private List<String> availableGuesses;

    public FairBotPlayer(String name, List<String> availableQuestions, List<String> availableGuesses) {
        this.name = name;
        this.availableQuestions = new ArrayList<>(availableQuestions);
        this.availableGuesses = new ArrayList<>(availableGuesses);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getQuestion() {
        String question = availableQuestions.remove(0);
        System.out.println("Player: " + name + ". Asks: " + question);
        return question;
    }

    @Override
    public String answerQuestion(String question, String character) {
        String answer = Math.random() < 0.5 ? "Yes" : "No";
        System.out.println("Player: " + name + ". Answers: " + answer);
        return answer;
    }


    @Override
    public String answerGuess(String guess, String character) {
        System.out.println(guess);
        Integer questionChar = guess.lastIndexOf("?");
        String substring = guess.substring(7, questionChar);
        boolean trueGuess = substring.equals(character);
        return trueGuess ? "Yes" : "No";
    }

    @Override
    public String getGuess() {
        int randomPos = (int) (Math.random() * this.availableGuesses.size());
        String guess = this.availableGuesses.remove(randomPos);
        System.out.println("Player: " + name + ". Guesses: Am I " + guess);
        return guess;
    }

    @Override
    public boolean isReadyForGuess() {
        return availableQuestions.isEmpty();
    }

}