package me.fineasgavre.pdp.lab8.Messages;

public class SubscribeMessage extends BaseMessage {
    public final int rank;
    public final String variable;

    public SubscribeMessage(int rank, String variable) {
        this.rank = rank;
        this.variable = variable;
    }
}
