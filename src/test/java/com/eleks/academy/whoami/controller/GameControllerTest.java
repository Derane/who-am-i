package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.configuration.GameControllerAdvice;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.request.UserName;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    private final GameServiceImpl gameService = mock(GameServiceImpl.class);
    private final GameController gameController = new GameController(gameService);
    private final NewGameRequest gameRequest = new NewGameRequest();
    private MockMvc mockMvc;

    @BeforeEach
    public void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameController)
                .setControllerAdvice(new GameControllerAdvice()).build();
        gameRequest.setMaxPlayers(5);
    }

    @Test
    void findAvailableGames() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/games")
                                .header("X-Id", "player"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotHaveJsonPath());
    }

    @Test
    void createGame() throws Exception {
        GameDetails gameDetails = new GameDetails();
        gameDetails.setId("12613126");
        gameDetails.setStatus("WaitingForPlayers");
        when(gameService.createGame(eq("player"), any(NewGameRequest.class))).thenReturn(gameDetails);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/games")
                                .header("X-Id", "player")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"maxPlayers\": 2\n" +
                                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value("12613126"))
                .andExpect(jsonPath("status").value("WaitingForPlayers"));
    }

    @Test
    void createGameFailedWithException() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/games")
                                .header("X-Id", "player")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"maxPlayers\": null\n" +
                                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"Validation failed!\"," +
                        "\"details\":[\"maxPlayers must not be null\"]}"));
    }

    @Test
    void suggestCharacter() throws Exception {
        doNothing().when(gameService).suggestCharacter(eq("1234"), eq("player"), any(CharacterSuggestion.class), any(UserName.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/games/1234/characters")
                                .header("X-Id", "player")
                                .header("X-Name", "s")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"character\": \" char \"\n" +
                                        "}"))
                .andExpect(status().isOk());
        verify(gameService, times(1)).suggestCharacter(eq("1234"), eq("player"), any(CharacterSuggestion.class), any(UserName.class));
    }

    @Test
    void failValidationSuggestCharacter() throws Exception {
        doNothing().when(gameService).suggestCharacter(eq("1234"), eq("player"),
                eq(new CharacterSuggestion("Batman")), any(UserName.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/games/1234/characters")
                                .header("X-Id", "player")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"character\": \"a\"\n" +
                                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void failValidationUserName() throws Exception {
        doNothing().when(gameService).suggestCharacter(eq("1234"), eq("p"),
                eq(new CharacterSuggestion("Batman")), any(UserName.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/games/1234/characters")
                                .header("X-Id", "s")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"character\": \"Batman\"\n" +
                                        "}"))
                .andExpect(status().isOk());
    }
}
