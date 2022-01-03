package me.fineasgavre.pdp.lab8.Messages;

import java.util.StringJoiner;

public class CloseMessage extends BaseMessage {
    public final int rank;

    public CloseMessage(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CloseMessage.class.getSimpleName() + "[", "]")
                .add("rank=" + rank)
                .toString();
    }
}
