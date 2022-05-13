package com.eleks.academy.whoami.configuration;

public class ServerProperties {
    private final int port;
    private final int players;

    private static final int NOT_ALLOWED_PORT_VALUE = 1024;
    private static final int MINIMUM_PLAYERS = 2;

    public ServerProperties(int port, int players) {
        super();
        if (port < 0) {
            throw new IllegalArgumentException(String.format("Port value cannot be negative, value: %d ", port));
        }
        if (port <= NOT_ALLOWED_PORT_VALUE) {
            throw new IllegalArgumentException(String.format("Port value cannot be less than value: %d ", port));
        }
        if (players < 0) {
            throw new IllegalArgumentException(String.format("Players value cannot be negative value: %d ", players));
        }
        if (players <= MINIMUM_PLAYERS) {
            throw new IllegalArgumentException(String.format("Players value should be greater than: %d," +
                            " but provided %d", MINIMUM_PLAYERS, players));
        }
        this.port = port;
        this.players = players;
    }


    public int getPlayers() {
        return players;
    }

    public int getPort() {
        return port;
    }

}
