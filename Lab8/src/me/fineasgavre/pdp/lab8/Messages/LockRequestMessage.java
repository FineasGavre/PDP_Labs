package me.fineasgavre.pdp.lab8.Messages;

import mpi.MPI;

import java.util.StringJoiner;

public class LockRequestMessage extends BaseMessage {
    public final String variableName;
    public final int ownerRank = MPI.COMM_WORLD.Rank();
    public final boolean status;

    public LockRequestMessage(String variableName, boolean status) {
        this.variableName = variableName;
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LockRequestMessage.class.getSimpleName() + "[", "]")
                .add("variableName='" + variableName + "'")
                .add("ownerRank=" + ownerRank)
                .add("status=" + status)
                .toString();
    }
}
