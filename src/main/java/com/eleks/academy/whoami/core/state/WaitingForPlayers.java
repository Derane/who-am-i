package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class WaitingForPlayers extends AbstractGameState {

    private final Map<String, SynchronousPlayer> players;

    public WaitingForPlayers(int maxPlayers) {
        super(0, maxPlayers);
        this.players = new HashMap<>(maxPlayers);
    }

    private WaitingForPlayers(int maxPlayers, Map<String, SynchronousPlayer> players) {
        super(players.size(), maxPlayers);
        this.players = players;
    }

    @Override
    public GameState next() {
        return new SuggestingCharacters(this.players);
    }

    @Override
    public GameState makeTurn(Answer answer) {
        Map<String, SynchronousPlayer> nextPlayers = new HashMap<>(this.players);
        if (nextPlayers.containsKey(answer.getPlayerId())) {
            throw new GameException("Cannot enroll to the game");
        } else {
            nextPlayers.put(answer.getPlayerId(), new PersistentPlayer(answer.getPlayerId(), answer.getName()));
        }
        if (players.size() == getMaxPlayers()) {
            return new SuggestingCharacters(players);
        } else {
            return new WaitingForPlayers(getMaxPlayers(), nextPlayers);
        }
    }

    @Override
    public GameState makeLeave(Answer answer) {
        if (players.containsKey(answer.getPlayerId())) {
            players.remove(answer.getPlayerId());
        } else {
            throw new GameException("Cannot leave the game");
        }
        if (players.size() == getMaxPlayers()) {
            return new SuggestingCharacters(players);
        } else {
            return new WaitingForPlayers(getMaxPlayers(), players);
        }
    }

    @Override
    public Optional<SynchronousPlayer> findPlayer(String player) {
        return Optional.ofNullable(this.players.get(player));
    }

    @Override
    public int getPlayersInGame() {
        return this.players.size();
    }

}
