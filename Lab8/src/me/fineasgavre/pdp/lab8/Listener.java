package me.fineasgavre.pdp.lab8;

import me.fineasgavre.pdp.lab8.Messages.*;
import mpi.MPI;

public class Listener implements Runnable {
    private final DistributedSharedMemory distributedSharedMemory;
    private final LockManager lockManager;

    public Listener(DistributedSharedMemory distributedSharedMemory, boolean managesLocks) {
        this.distributedSharedMemory = distributedSharedMemory;
        this.lockManager = managesLocks ? new LockManager() : null;
    }

    @Override
    public void run() {
        while (true) {
            Utils.log("I am waiting for a message...");

            var messagesBuffer = new Object[100];
            MPI.COMM_WORLD.Recv(messagesBuffer, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, MPI.ANY_TAG);
            var receivedMessage = (BaseMessage) messagesBuffer[0];

            if (receivedMessage instanceof SubscribeMessage) {
                distributedSharedMemory.receiveSubscriptionNotice((SubscribeMessage) receivedMessage);
            } else if (receivedMessage instanceof UpdateMessage) {
                distributedSharedMemory.receiveUpdateNotice((UpdateMessage) receivedMessage);
            } else if (receivedMessage instanceof LockRequestMessage && lockManager != null) {
                lockManager.processLockRequestMessage((LockRequestMessage) receivedMessage);
            } else if (receivedMessage instanceof LockAcknowledgementMessage) {
                distributedSharedMemory.receiveLockNotice((LockAcknowledgementMessage) receivedMessage);
            } else if (receivedMessage instanceof CloseMessage) {
                distributedSharedMemory.receiveClosureNotice((CloseMessage) receivedMessage);

                if (lockManager != null) {
                    lockManager.releaseLockOnVariable(((CloseMessage) receivedMessage).rank);
                } else {
                    break;
                }
            }
        }

        Utils.log("Final DSM: " + distributedSharedMemory);
    }
}
