package me.fineasgavre.pdp.lab8.Messages;

import me.fineasgavre.pdp.lab8.Models.Variable;

import java.util.StringJoiner;

public class UpdateMessage extends BaseMessage {
    public final Variable variable;

    public UpdateMessage(Variable variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UpdateMessage.class.getSimpleName() + "[", "]")
                .add("variable=" + variable)
                .toString();
    }
}
