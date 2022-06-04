package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.request.UserName;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.eleks.academy.whoami.utils.StringUtils.Headers.*;

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

    // TODO: Should return enrolled player
    @PostMapping("/{id}/players")
    public void enrollToGame(@PathVariable("id") String id,
                             @RequestHeader(ID) String playerId) {
        this.gameService.enrollToGame(id, playerId);
    }

    @PostMapping("/{id}/characters")
    @ResponseStatus(HttpStatus.OK)
    public void suggestCharacterAndSetName(@PathVariable("id") String id,
                                           @RequestHeader(ID) String playerId,
                                           @Valid @RequestHeader(NAME) @Pattern(regexp =  "^\\d{4}-\\d{2}-\\d{2}$") UserName name,
                                           @Valid @RequestBody CharacterSuggestion suggestion) {
        this.gameService.suggestCharacter(id, playerId, suggestion, name);
    }

    @PostMapping("/{id}")
    public ResponseEntity<GameDetails> startGame(@PathVariable("id") String id,
                                                 @RequestHeader(ID) String playerId) {
        return this.gameService.startGame(id, playerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
