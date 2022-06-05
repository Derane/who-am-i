package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface SynchronousPlayer {

    void setCharacter(String character);

    void setName(String name);

    Future<String> getName();
}
