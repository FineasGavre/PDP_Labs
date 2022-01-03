package me.fineasgavre.pdp.lab8.Messages;

import mpi.MPI;

import java.util.StringJoiner;

public class SubscribeMessage extends BaseMessage {
    public final int rank;
    public final String variableName;

    public SubscribeMessage(String variable) {
        this.rank = MPI.COMM_WORLD.Rank();
        this.variableName = variable;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SubscribeMessage.class.getSimpleName() + "[", "]")
                .add("rank=" + rank)
                .add("variableName='" + variableName + "'")
                .toString();
    }
}
