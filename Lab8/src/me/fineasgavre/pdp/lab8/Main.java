package me.fineasgavre.pdp.lab8;

import mpi.MPI;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);

        int currentRank = MPI.COMM_WORLD.Rank();

        switch (currentRank) {
            case 0 -> parentProcess();
            case 1 -> childProcess1();
            case 2 -> childProcess2();
            default -> Utils.log("whoooops im not doing anything");
        }

        MPI.Finalize();
    }

    private static void parentProcess() throws InterruptedException {
        var dsm = new DistributedSharedMemory();
        var thread = new Thread(new Listener(dsm, true));
        thread.start();

        dsm.sendSubscriptionNotice("a");
        dsm.sendSubscriptionNotice("b");
        dsm.sendSubscriptionNotice("c");
        dsm.compareAndUpdate("a", 0, 111);
        dsm.compareAndUpdate("c", 2, 333);
        dsm.compareAndUpdate("b", 100, 999);
        dsm.sendClosureNotice();

        thread.join();
    }

    private static void childProcess1() throws InterruptedException {
        var dsm = new DistributedSharedMemory();
        var thread = new Thread(new Listener(dsm, false));
        thread.start();

        dsm.sendSubscriptionNotice("a");
        dsm.sendSubscriptionNotice("b");
        dsm.sendUpdateNotice("b", 5);
        dsm.sendClosureNotice();

        thread.join();
    }

    private static void childProcess2() throws InterruptedException {
        var dsm = new DistributedSharedMemory();
        var thread = new Thread(new Listener(dsm, false));
        thread.start();

        dsm.sendSubscriptionNotice("b");
        dsm.compareAndUpdate("b", 1, 100);
        dsm.sendClosureNotice();

        thread.join();
    }
}
