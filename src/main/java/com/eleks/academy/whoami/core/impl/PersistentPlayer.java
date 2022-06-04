package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.SynchronousPlayer;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class PersistentPlayer implements Player, SynchronousPlayer {

    private final String id;
    private CompletableFuture<String> name = new CompletableFuture<>();


    public Future<String> getName() {
        return name;
    }

    public void setName(String name) {
        if (!this.name.complete(name)) {
            throw new IllegalStateException("Name has already been suggested!");
        }
    }

    private final CompletableFuture<String> character = new CompletableFuture<>();

    public PersistentPlayer(String id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = CompletableFuture.completedFuture(name);
    }

    @Override
    public Future<String> getId() {
        return CompletableFuture.completedFuture(this.id);
    }

    @Override
    public Future<String> suggestCharacter() {
        return character;
    }

    @Override
    public String getQuestion() {
        return null;
    }

    @Override
    public String answerQuestion(String question, String character) {
        return null;
    }

    @Override
    public String getGuess() {
        return null;
    }

    @Override
    public boolean isReadyForGuess() {
        return false;
    }

    @Override
    public String answerGuess(String guess, String character) {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void setCharacter(String character) {
        if (!this.character.complete(character)) {
            throw new IllegalStateException("Character has already been suggested!");
        }
    }
}
