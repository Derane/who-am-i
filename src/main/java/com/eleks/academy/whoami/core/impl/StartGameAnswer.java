package com.eleks.academy.whoami.core.impl;

public final class StartGameAnswer extends Answer {

    public StartGameAnswer(String player, String name) {
        super(player, name);
    }

    public static StartGameAnswer of(String player, String name) {
        return new StartGameAnswer(player, name);
    }

}
