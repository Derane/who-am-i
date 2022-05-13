package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ServerImpl implements Server {

    private List<String> characters = List.of("Batman", "Superman");
    private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?");
    private List<String> guessess = List.of("Batman", "Superman");

    private final ServerSocket serverSocket;
    private final int players;
    private final List<Player> clientPlayers;

    public ServerImpl(int port, int players) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.players = players;
        this.clientPlayers = new ArrayList<>(players);
    }

    @Override
    public Game startGame() throws IOException {
        RandomGame game = new RandomGame(clientPlayers, characters);
        game.initGame();

        return game;
    }


    @Override
    @PostConstruct
    public void waitForPlayer() throws IOException {
        System.out.println("Server starts");
        System.out.println("Waiting for a client connect....");
        for (int i = 0; i < players; i++) {

            clientPlayers.add(new ClientPlayer(serverSocket.accept()));

        }
        System.out.printf("Got %d players. Starting a game.%n", players);
    }


    @PreDestroy
    public void stop() {
        for (Player clientPlayer : clientPlayers) {
            try {
                clientPlayer.close();
            } catch (Exception e) {
                System.err.println(String.format("Could not close a socket (%s)", e.getMessage()));
            }


        }
    }

}