package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.*;

import com.eleks.academy.whoami.core.Player;

public class ClientPlayer implements Player, AutoCloseable {

    private final BufferedReader reader;
    private final PrintStream writer;
    private final Socket socket;
    private String name;
    private String suggestCharacter;

    public ExecutorService getExecutor() {
        return executor;
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ClientPlayer(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintStream(socket.getOutputStream());
    }

    @Override
    public String getName() {
        // TODO: save name for future
        return name;
    }

    public Future<String> setName() {
        // TODO: save name for future
        writer.println("Please write your name: ");
        return executor.submit(this::askName);
    }

    private String askName() {
        String name = "";

        try {
            name = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.name = name;

        return name;
    }

    @Override
    public Future<String> getQuestion() {
        return executor.submit(this::doGetQuestion);
    }

    private String doGetQuestion() {
        String question = "";

        try {
            writer.println("Ask your questinon: ");
            question = reader.readLine();
            System.out.println(name + ": " + question);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return question;
    }

    @Override
    public Future<String> answerQuestion(String question, String character) {
        Callable<String> callableAnswer = () -> {
            String answer = "";

            try {
                writer.println("Answer second player question: " + question + "Character is:" + character);
                answer = reader.readLine();
                System.out.println(name + ": " + answer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        };

        return executor.submit(callableAnswer);
    }

    @Override
    public Future<String> getGuess() {
        return executor.submit(this::doGetGuess);
    }

    private String doGetGuess() {
        String answer = "";


        try {
            writer.println("Write your guess: ");
            answer = reader.readLine();
            System.out.println(name + ": " + answer);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return answer;
    }

    @Override
    public Future<Boolean> isReadyForGuess() {
        return executor.submit(this::doIsReadyForGuess);
    }

    private boolean doIsReadyForGuess() {
        String answer = "";

        try {
            writer.println("Are you ready to guess? ");
            answer = reader.readLine();
            System.out.println("Is " + name + " ready to guess? " + answer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer.equals("Yes") ? true : false;
    }

    @Override
    public Future<String> answerGuess(String guess, String character) {
        return executor.submit(() -> doAnswerGuess(guess, character));
    }

    private String doAnswerGuess(String guess, String character) {
        String answer = "";

        try {
            writer.println("character: " + character + " has guess:  " + guess + ". Write your answer: ");
            answer = reader.readLine();
            System.out.println(name + ":" + answer);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return answer;
    }

    @Override
    public String getSuggestCharacter() {
        return suggestCharacter;
    }

    public boolean isThisPlayerSuggestCharacter(String character) {
        return this.suggestCharacter.equals(character);
    }

    @Override
    public Future<String> suggestCharacter() {
        return executor.submit(this::doSuggestCharacter);
    }

    private String doSuggestCharacter() {
        try {
            suggestCharacter = reader.readLine();
            return suggestCharacter;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        close(writer);
        close(reader);
        close(socket);
    }

    private void close(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}