package me.fineasgavre.pdp.lab8;

import me.fineasgavre.pdp.lab8.Messages.*;
import me.fineasgavre.pdp.lab8.Models.Variable;
import mpi.MPI;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DistributedSharedMemory {
    private final Lock variableOwnerLock = new ReentrantLock();

    private final Map<String, Set<Integer>> subscribers = new ConcurrentHashMap<>();
    private final Map<String, Variable> variables = new ConcurrentHashMap<>();
    private final Set<Map.Entry<String, Integer>> variableOwners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void sendSubscriptionNotice(String variableName) {
        sendMessageToAll(new SubscribeMessage(variableName));
    }

    public void receiveSubscriptionNotice(SubscribeMessage message) {
        Utils.log("I know that R" + message.rank + " subscribed to " + message.variableName + ".");
        subscribers.putIfAbsent(message.variableName, new HashSet<>());
        subscribers.get(message.variableName).add(message.rank);
    }

    public void sendClosureNotice() {
        sendMessageToAll(new CloseMessage(MPI.COMM_WORLD.Rank()));
    }

    public void receiveClosureNotice(CloseMessage message) {
        Utils.log("I know that [R" + message.rank + "] has closed.");

        subscribers.values().forEach(set -> set.remove(message.rank));
    }

    public void compareAndUpdate(String variableName, int comparison, int newValue) {
        Utils.log("I want to compare " + variableName + " to " + comparison + " and change to " + newValue + " if it matches.");

        var acquireReadAndUpdateThread = new Thread(() -> {
            var acquireThread = startAcquiringLockThread(variableName);
            acquireThread.start();

            try {
                acquireThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            var variable = variables.get(variableName);

            if (variable != null && variable.getValue() == comparison) {
                variable.setValue(newValue);
                variable.incrementVersion();

                sendMessageToSubscribers(subscribers.get(variableName), new UpdateMessage(variable));
            }

            sendLockRelease(variableName);
        });

        acquireReadAndUpdateThread.start();
    }

    public void sendUpdateNotice(String variableName, int value) {
        Utils.log("I want to tell everyone that " + variableName + " will now be " + value + ".");

        var updateThread = new Thread(() -> {
            var acquireThread = startAcquiringLockThread(variableName);
            acquireThread.start();

            try {
                acquireThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            var variable = variables.getOrDefault(variableName, new Variable(variableName, value));

            if (variables.containsKey(variableName)) {
                variable.setValue(value);
                variable.incrementVersion();
            }

            sendMessageToSubscribers(subscribers.get(variableName), new UpdateMessage(variable));
            sendLockRelease(variableName);
        });

        updateThread.start();
    }

    public void receiveUpdateNotice(UpdateMessage updateMessage) {
        Utils.log("I have received an update for variable " + updateMessage.variable + ".");

        if (variables.containsKey(updateMessage.variable.getName())) {
            if (variables.get(updateMessage.variable.getName()).getVersion() < updateMessage.variable.getVersion()) {
                variables.put(updateMessage.variable.getName(), updateMessage.variable);
            }
        } else {
            variables.put(updateMessage.variable.getName(), updateMessage.variable);
        }
    }

    public Thread startAcquiringLockThread(String variableName) {
        Utils.log("I want to get the lock for " + variableName + ".");

        return new Thread(() -> {
            var hasAcquiredLock = false;

            while (!hasAcquiredLock) {
                variableOwnerLock.lock();

                Utils.log("Owners: " + variableOwners);
                hasAcquiredLock = variableOwners.contains(Map.entry(variableName, MPI.COMM_WORLD.Rank()));
                Utils.log("I know that " + variableName + " is locked to me.");

                variableOwnerLock.unlock();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sendMessageToLockManager(new LockRequestMessage(variableName, true));
            }
        });
    }

    public void sendLockRelease(String variableName) {
        variableOwnerLock.lock();
        variableOwners.removeIf(entry -> entry.getKey().equals(variableName) && entry.getValue() == MPI.COMM_WORLD.Rank());
        variableOwnerLock.unlock();
        sendMessageToLockManager(new LockRequestMessage(variableName, false));
    }

    public void receiveLockNotice(LockAcknowledgementMessage lockMessage) {
        variableOwnerLock.lock();
        variableOwners.removeIf(entry -> entry.getKey().equals(lockMessage.variableName));
        variableOwners.add(Map.entry(lockMessage.variableName, lockMessage.ownerRank));
        variableOwnerLock.unlock();
        Utils.log("I now know that variable [" + lockMessage.variableName + "] is locked to [R" + lockMessage.ownerRank + "].");
    }

    private void sendMessageToLockManager(BaseMessage message) {
        Utils.log("I am sending this message: " + message + " just to the lock manager.");

        MPI.COMM_WORLD.Isend(new Object[]{message}, 0, 1, MPI.OBJECT, 0, 0);
    }

    private void sendMessageToSubscribers(Set<Integer> subscribers, BaseMessage message) {
        Utils.log("I am sending this message: " + message + " just to " + subscribers + ".");

        subscribers.forEach(i -> {
            MPI.COMM_WORLD.Isend(new Object[]{message}, 0, 1, MPI.OBJECT, i, 0);
        });
    }

    private void sendMessageToAll(BaseMessage message) {
        Utils.log("I am sending this message: " + message + ".");

        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            MPI.COMM_WORLD.Isend(new Object[]{message}, 0, 1, MPI.OBJECT, i, 0);
        }
    }
}
