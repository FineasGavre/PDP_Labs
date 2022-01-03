package me.fineasgavre.pdp.lab8;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DistributedSharedMemory {
    private static final Lock lock = new ReentrantLock();

    private final Map<String, Set<Integer>> subscribers = new HashMap<>();
    private final Map<String, Integer> variables = new HashMap<>();

    public DistributedSharedMemory() {

    }
}
