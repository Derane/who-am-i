package com.eleks.academy.whoami.core.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class TurnImpl implements Turn {

    private List<Player> players;
    private int currentPlayerIndex = 0;

    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex >= this.players.size() ? 0 : this.currentPlayerIndex;

    }

    public TurnImpl(List<Player> players) {
        this.players = players;
    }

    @Override
    public Player getGuesser() {
        try {
            Objects.checkIndex(currentPlayerIndex, players.size());
        } catch (Exception e) {
            currentPlayerIndex -= 1;
        }
        return this.players.get(currentPlayerIndex);
    }

    @Override
    public List<Player> getOtherPlayers() {
        return this.players.stream()
                .filter(player -> !player.getName().equals(this.getGuesser().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void changeTurn() {
        this.currentPlayerIndex = this.currentPlayerIndex + 1 >= this.players.size() ? 0 : this.currentPlayerIndex + 1;
    }


}
