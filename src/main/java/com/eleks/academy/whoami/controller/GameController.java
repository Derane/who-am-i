package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.request.UserRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.eleks.academy.whoami.utils.StringUtils.Headers.ID;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public List<GameLight> findAvailableGames(@RequestHeader(ID) String playerId) {
        return this.gameService.findAvailableGames(playerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameDetails createGame(@RequestHeader(ID) String playerId,
                                  @Valid @RequestBody NewGameRequest gameRequest) {
        return this.gameService.createGame(playerId, gameRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDetails> findById(@PathVariable("id") String id,
                                                @RequestHeader(ID) String playerId) {
        return this.gameService.findByIdAndPlayer(id, playerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/players")
    public void enrollToGame(@PathVariable("id") String id,
                             @RequestHeader(ID) String playerId) {
        this.gameService.enrollToGame(id, playerId);
    }

    @PostMapping("/{id}/leaving")
    public void leaveGame(@PathVariable("id") String id,
                          @RequestHeader(ID) String playerId) {
        this.gameService.leaveGame(id, playerId);
    }

    @PostMapping("/{id}/characters")
    @ResponseStatus(HttpStatus.OK)
    public void suggestCharacterAndSetName(@PathVariable("id") String id,
                                           @RequestHeader(ID) String playerId,
                                           @Valid @RequestBody UserRequest userRequest) {
        this.gameService.suggestCharacter(id, playerId,
                userRequest.getCharacter(), userRequest.getName());
    }

    @PostMapping("/{id}")
    public ResponseEntity<GameDetails> startGame(@PathVariable("id") String id,
                                                 @RequestHeader(ID) String playerId) {
        return this.gameService.startGame(id, playerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
