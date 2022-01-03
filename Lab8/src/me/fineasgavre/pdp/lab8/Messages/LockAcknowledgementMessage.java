package me.fineasgavre.pdp.lab8.Messages;

import java.util.StringJoiner;

public class LockAcknowledgementMessage extends BaseMessage {
    public final String variableName;
    public final int ownerRank;

    public LockAcknowledgementMessage(String variableName, int ownerRank) {
        this.variableName = variableName;
        this.ownerRank = ownerRank;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LockAcknowledgementMessage.class.getSimpleName() + "[", "]")
                .add("variableName='" + variableName + "'")
                .add("ownerRank=" + ownerRank)
                .toString();
    }
}
