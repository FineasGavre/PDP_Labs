package me.fineasgavre.pdp.lab8;

import mpi.MPI;

public class Utils {
    public static void log(Object message) {
        System.out.println("[R-" + MPI.COMM_WORLD.Rank() + "] " + message.toString());
    }
}
