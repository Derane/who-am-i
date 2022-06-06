package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSuggestion {
    @Size(min = 2, max = 50, message = "Character must be between 2 and 50 characters!")
    @NotBlank(message = "Character must not be empty!")
    private String character;

}

