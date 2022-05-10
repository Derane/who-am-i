package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class RandomGameTest {
    @Test
    public void askPlayersForCharactersSuggestion() {
        Game game = new RandomGame(List.of("C"));
        TestPlayer p1 = new TestPlayer("P1");
        TestPlayer p2 = new TestPlayer("P2");
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.assignCharacters();
        // TODO: assert that game asks for a character suggestion
        assertAll(() -> assertTrue(p1.suggested),
                () -> assertTrue(p2.suggested));
    }

    private static final class TestPlayer implements Player {
        public TestPlayer(String name) {
            this.name = name;
        }

        private final String name;
        private boolean suggested;

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getSuggestCharacter() {
            return null;
        }

        @Override
        public Future<String> suggestCharacter() {
            suggested = true;
            return CompletableFuture.completedFuture("char");
        }

        @Override
        public Future<String> getQuestion() {
            return null;
        }

        @Override
        public Future<String> answerQuestion(String question, String character) {
            return null;
        }

        @Override
        public Future<String> getGuess() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<Boolean> isReadyForGuess() {
            return CompletableFuture.completedFuture(false);
        }

        @Override
        public Future<String> answerGuess(String guess, String character) {
            return null;
        }

        @Override
        public Future<String> setName() {
            return CompletableFuture.completedFuture(this.name);
        }
    }
}