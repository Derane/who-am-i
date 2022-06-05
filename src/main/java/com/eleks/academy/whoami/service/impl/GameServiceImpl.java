package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.core.impl.StartGameAnswer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.request.UserName;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    Stack<String> defaultNames = new Stack<>();

    {
        defaultNames.push("Player-4");
        defaultNames.push("Player-3");
        defaultNames.push("Player-2");
        defaultNames.push("Player-1");
    }

    @Override
    public List<GameLight> findAvailableGames(String player) {
        return this.gameRepository.findAllAvailable(player)
                .map(GameLight::of)
                .toList();
    }

    @Override
    public GameDetails createGame(String player, NewGameRequest gameRequest) {
        final var game = this.gameRepository.save(new PersistentGame(player, gameRequest.getMaxPlayers()));

        return GameDetails.of(game);
    }

    @Override
    public void enrollToGame(String id, String playerId) {
        this.gameRepository.findById(id)
                .filter(SynchronousGame::isAvailable)
                .ifPresentOrElse(
                        game -> game.makeTurn(new Answer(playerId, defaultNames.pop())),
                        () -> {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot enroll to a game");
                        }
                );
    }

    @Override
    public Optional<GameDetails> findByIdAndPlayer(String id, String playerId) {
        return this.gameRepository.findById(id)
                .filter(game -> game.findPlayer(playerId).isPresent())
                .map(GameDetails::of);
    }

    @Override
    public void suggestCharacter(String id, String playerId, CharacterSuggestion suggestion, UserName name) {
        this.gameRepository.findById(id)
                .flatMap(game -> game.findPlayer(playerId))
                .ifPresent(p -> p.setCharacter(suggestion.getCharacter()));
        this.gameRepository.findById(id)
                .flatMap(game -> game.findPlayer(playerId))
                .ifPresent(p -> p.setName(name.getName()));
    }

    @Override
    @SneakyThrows
    public Optional<GameDetails> startGame(String id, String playerId) {
        UnaryOperator<SynchronousGame> startGame = game -> {
            game.makeTurn(new StartGameAnswer(playerId, "S"));

            return game;
        };

        return this.gameRepository.findById(id)
                .map(startGame)
                .map(GameDetails::of);
    }

    @Override
    public void leaveGame(String playerId) {

    }

}
