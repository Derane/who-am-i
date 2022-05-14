package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    public GameServiceImpl(final GameRepository gameRepository) {

        this.gameRepository = requireNonNull(gameRepository);

    }

    @Override
    public List<Game> findAvailableGames() {
        return this.gameRepository.findAllAvailable();
    }
}

