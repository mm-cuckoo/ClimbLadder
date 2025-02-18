package com.cko.algorithm.linkedlist;

import com.cko.algorithm.ListNode;
import com.cko.algorithm.N;
import com.cko.algorithm.NU;

import java.util.HashSet;

/**
 * https://leetcode.cn/problems/linked-list-cycle/description/?envType=study-plan-v2&envId=top-100-liked
 * 给你一个链表的头节点 head ，判断链表中是否有环。
 *
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。注意：pos 不作为参数进行传递 。仅仅是为了标识链表的实际情况。
 *
 * 如果链表中存在环 ，则返回 true 。 否则，返回 false 。
 */
public class 环形链表141 {

    public static void main(String[] args) {

        ListNode centerNode = new ListNode(0);
        ListNode endNode = new ListNode(6);
        endNode.next = centerNode;
        ListNode headNode = NU.create(new ListNode[]{
                N.Ln(1),
                N.Ln(2),
                centerNode,
                N.Ln(3),
                N.Ln(4),
                N.Ln(5),
                endNode
                }
        );

        boolean result = hasCycle(headNode);
        System.out.println(result);

    }

    public static boolean hasCycle(ListNode head) {
        // 快慢指针
        // 在快慢指针中，关键点在于前面的指针的next是否为null， 如果不为null 可以一直遍历下去，直到出现两个指针指向同一节点退出
        // 如果前面指针的next 出现null， 表示一定不是环形链表，可以结束了
        if (head == null) {
            return false;
        }
        ListNode firstNode = head.next;
        ListNode secondNode = head;

        while (firstNode != null) {
            if (firstNode == secondNode) {
                return true;
            }
            if (firstNode.next == null) {
                return false;
            }
            firstNode = firstNode.next.next;
            secondNode = secondNode.next;

        }

        return false;
    }

    public static boolean hasCycle1(ListNode head) {
        // 用一个hashSet 保存已经遍历的节点， 每次取出一个都去hashSet 中查找是否已经存在
        // 如果存在，为true，执行到最后不纯在，则为false
        HashSet<ListNode>  rootNodes = new HashSet<>();
        while (head != null) {
            if (rootNodes.contains(head)) {
                return true;
            } else {
                rootNodes.add(head);
                head = head.next;
            }
        }
        return false;
    }
}
