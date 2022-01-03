package me.fineasgavre.pdp.lab8;

import me.fineasgavre.pdp.lab8.Messages.BaseMessage;
import me.fineasgavre.pdp.lab8.Messages.LockAcknowledgementMessage;
import me.fineasgavre.pdp.lab8.Messages.LockRequestMessage;
import mpi.MPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockManager {
    private final Map<Integer, String> locks = new ConcurrentHashMap<>();

    public void processLockRequestMessage(LockRequestMessage message) {
        if (message.status) {
            var hasLocked = lockVariableForOwner(message.ownerRank, message.variableName);

            if (hasLocked) {
                sendMessageToAll(new LockAcknowledgementMessage(message.variableName, message.ownerRank));
            }
        } else {
            releaseLockOnVariable(message.ownerRank);
        }
    }

    private boolean lockVariableForOwner(int rankOwner, String variableName) {
        if (locks.containsValue(variableName)) {
            return false;
        }

        locks.put(rankOwner, variableName);
        return true;
    }

    public void releaseLockOnVariable(int rankOwner){
        locks.remove(rankOwner);
    }

    private void sendMessageToAll(LockAcknowledgementMessage message) {
        Utils.log("Lock Manager sends message: " + message + ".");

        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            MPI.COMM_WORLD.Isend(new Object[]{message}, 0, 1, MPI.OBJECT, i, 0);
        }
    }
}
