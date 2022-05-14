package com.eleks.academy.whoami;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.eleks.academy.whoami.core.impl.RandomPlayer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void randomPlayerSuggestsCharacter() throws ExecutionException, InterruptedException {
        Collection<String> characterSuggestions = List.of("A", "B");
        RandomPlayer p = new RandomPlayer("P", characterSuggestions, new ArrayList<>(), new ArrayList<>());
        String character = p.suggestCharacter().get();
        assertNotNull(character);
        assertTrue(characterSuggestions.contains(character));
    }

    @Test
    public void convertToYearsAndDaysTest() {
        App app = new App();
    }
}
