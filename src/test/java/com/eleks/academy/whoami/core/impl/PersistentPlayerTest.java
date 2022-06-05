package com.eleks.academy.whoami.core.impl;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class PersistentPlayerTest {

    @Test
    void allowToSuggestCharacterOnlyOnce() {
        PersistentPlayer player = new PersistentPlayer("PLayerId", "PlayerName");
        Future<String> character = player.suggestCharacter();
        assertFalse(character.isDone());
        player.setCharacter("character");
        assertTrue(character.isDone());
        assertThrows(IllegalStateException.class, () -> player.setCharacter("character"));
    }
}
