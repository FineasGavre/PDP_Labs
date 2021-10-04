package me.fineasgavre.pdp.lab1.Tasks;

import me.fineasgavre.pdp.lab1.Main;

import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class IncrementParentNodesTask extends TimerTask {

    @Override
    public void run() {
        Main.primaryNodes.forEach(primaryNode -> {
            var value = ThreadLocalRandom.current().nextInt(1, 20);
            var sign = ThreadLocalRandom.current().nextBoolean();
            primaryNode.incrementValue(value * (sign ? 1 : -1));
        });

        System.out.println("Incremented primary nodes");
    }

}
