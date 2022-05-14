package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

  private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> findAvailableGames() {
        return this.gameService.findAvailableGames();
    }

}