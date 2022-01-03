package me.fineasgavre.pdp.lab8.Messages;

import java.io.Serializable;

public abstract class BaseMessage implements Serializable {
    protected final long timestamp = System.nanoTime();
}
