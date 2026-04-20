package project.game.selectors;

import project.network.ClientHandler;

public abstract class Selector {
    
    protected final ClientHandler client;

    public Selector(ClientHandler c) {
        this.client = c;
    }

    abstract public Object select();
} 
