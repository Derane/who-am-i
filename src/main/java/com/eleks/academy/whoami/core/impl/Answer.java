package com.eleks.academy.whoami.core.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Extend this class in case more input is needed
 * for a specific {@link com.eleks.academy.whoami.core.state.GameState}
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public sealed class Answer permits StartGameAnswer {
	private String name;
	private  String player;
	private String message;

	public Answer(String player, String name) {
		this.player = player;
		this.name = name;
	}

	public Answer(String player) {
		this.player=player;
	}
}
