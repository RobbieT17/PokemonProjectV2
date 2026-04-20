package project.game.selectors;

import project.network.ClientHandler;

public abstract class Selector {
    
    private final ClientHandler client;

    public Selector(ClientHandler c) {
        this.client = c;
    }

    abstract public Selectable select();
} 
