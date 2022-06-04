package com.eleks.academy.whoami.core.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Extend this class in case more input is needed
 * for a specific {@link com.eleks.academy.whoami.core.state.GameState}
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public sealed class Answer permits StartGameAnswer {
    private String name;
    private String playerId;
    private String message;

    public Answer(String playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public Answer(String playerId) {
        this.playerId = playerId;
    }
}
