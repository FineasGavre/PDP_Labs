package me.fineasgavre.pdp.lab1.Tasks;

import me.fineasgavre.pdp.lab1.Checker.ConsistencyChecker;
import me.fineasgavre.pdp.lab1.Main;

import java.util.TimerTask;

public class ConsistencyCheckerTask extends TimerTask {

    @Override
    public void run() {
        var consistencyChecker = new ConsistencyChecker(Main.primaryNodes);

        System.out.println("Preparing to run consistency checker...");
        var result = consistencyChecker.runChecker();

        System.out.println("Checker returned result " + result + ".");
    }

}
