package com.cko.algorithm;

public class NU {

    public static ListNode create(ListNode[] nodes) {
        ListNode startNode = N.Ln();
        ListNode lastNode = startNode;
        for (ListNode node : nodes) {
            lastNode.next = node;
            lastNode = lastNode.next;
        }
        return startNode.next;
    }


    public static ListNode create(int[] values) {
        ListNode startNode = N.Ln();
        ListNode lastNode = startNode;
        for (int value : values) {
            lastNode.next = N.Ln(value);
            lastNode = lastNode.next;
        }
        return startNode.next;
    }
    public static void printNode(ListNode node) {
        if (node == null) {
            System.out.println("[null]");
            return;
        }
        System.out.print("[" + node.value + "]");
    }
    public static void printNodes(ListNode node) {
        if (node == null) {
            System.out.println("[null]");
            return;
        }
        System.out.print("[");

        while (node != null) {
            System.out.print(node.value);
            if (node.next != null) {
                System.out.print(",");
            }
            node = node.next;
        }
        System.out.print("]");
    }
}
