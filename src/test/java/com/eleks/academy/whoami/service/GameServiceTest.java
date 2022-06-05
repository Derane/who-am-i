package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.request.UserName;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.repository.impl.GameInMemoryRepository;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class GameServiceTest {
    private final GameRepository gameRepository = new GameInMemoryRepository();
    private final GameServiceImpl gameService = new GameServiceImpl(gameRepository);
    private final NewGameRequest gameRequest = new NewGameRequest();
    private String gameId;

    @BeforeEach
    public void setMaxPlayers() {
        gameRequest.setMaxPlayers(2);
        gameId = gameService.createGame("host", gameRequest).getId();
    }

    @Test
    void findAvailableGames() {
        List<GameLight> games = gameService.findAvailableGames("player");
        assertEquals(1, games.size());
    }

    @Test
    public void enrollToGame() {
        String playerId = "53521";
        gameService.enrollToGame(gameId, playerId);
        assertNotNull(gameService.findByIdAndPlayer(gameId, playerId));
        List<GameLight> gameLight = gameService.findAvailableGames(playerId);
        assertThat(gameLight).isNotEmpty();
        assertNotNull(gameLight.get(0).getId());
        assertNotNull(gameLight.get(0).getStatus());
        assertNotNull(gameLight.get(0).getPlayersInGame());
    }

    @Test
    @SneakyThrows
    void suggestCharacterAndSetPlayerNames() {
        final String playerId = "player123";
        final String name = "Anton";
        final String previousName = "Player-1";
        final UserName userName = new UserName(name);
        CharacterSuggestion character = new CharacterSuggestion("char");
        gameService.enrollToGame(gameId, playerId);
        Optional<SynchronousPlayer> synchronousPlayer = this.gameRepository.findById(gameId)
                .flatMap(game -> game.findPlayer(playerId));
        String playerName = synchronousPlayer.get().getName().get();
        assertEquals(previousName, playerName);
        gameService.suggestCharacter(gameId, playerId, character, userName);
        playerName = synchronousPlayer.get().getName().get();
        assertEquals(name, playerName);

    }

    @Test
    void createGame() {
        String waitingForPlayersStatus = "com.eleks.academy.whoami.core.state.WaitingForPlayers";
        GameDetails gameDetails = gameService.createGame("player-1", gameRequest);
        String status = gameDetails.getStatus();
        assertEquals(waitingForPlayersStatus, status);
    }
}