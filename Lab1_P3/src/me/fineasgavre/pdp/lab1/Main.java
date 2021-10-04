package me.fineasgavre.pdp.lab1;

import me.fineasgavre.pdp.lab1.Models.Node;
import me.fineasgavre.pdp.lab1.Tasks.ConsistencyCheckerTask;
import me.fineasgavre.pdp.lab1.Tasks.IncrementParentNodesTask;

import java.util.ArrayList;
import java.util.Timer;

public class Main {

    public static ArrayList<Node> primaryNodes = new ArrayList<>();

    public static void main(String[] args) {
        createTreeStructure();
        scheduleIncrementTasks(10);
        scheduleConsistencyCheckerTask();
    }

    private static void scheduleConsistencyCheckerTask() {
        var timer = new Timer();
        timer.schedule(new ConsistencyCheckerTask(), 5, 5 * 1000);
    }

    private static void scheduleIncrementTasks(int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            var timer = new Timer();
            timer.schedule(new IncrementParentNodesTask(), 0, 1000);
        }
    }

    private static void createTreeStructure() {
        // Primary Nodes
        var pnode1 = new Node(5);
        var pnode2 = new Node(7);
        var pnode3 = new Node(10);

        primaryNodes.add(pnode1);
        primaryNodes.add(pnode2);
        primaryNodes.add(pnode3);

        // 2nd level Secondary Nodes
        var s2node1 = new Node();
        pnode1.addChild(s2node1);
        pnode2.addChild(s2node1);
        var s2node2 = new Node();
        pnode1.addChild(s2node2);
        pnode3.addChild(s2node2);
        var s2node3 = new Node();
        pnode2.addChild(s2node3);
        pnode3.addChild(s2node3);

        // 3rd level Secondary Nodes
        var s3node1 = new Node();
        s2node2.addChild(s3node1);
        s2node3.addChild(s3node1);
        var s3node2 = new Node();
        s2node1.addChild(s3node2);
        s2node3.addChild(s3node2);
    }

}
