package com.cko.algorithm.linkedlist;

import com.cko.algorithm.ListNode;
import com.cko.algorithm.N;
import com.cko.algorithm.NU;

/**
 * https://leetcode.cn/problems/linked-list-cycle-ii/description/?envType=study-plan-v2&envId=top-100-liked
 * 给定一个链表的头节点  head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 *
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。如果 pos 是 -1，则在该链表中没有环。注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。
 *
 * 不允许修改 链表。
 */
public class 环形链表II142 {
    public static void main(String[] args) {
        ListNode centerNode = new ListNode(3);
        ListNode endNode = new ListNode(6);
        endNode.next = centerNode;
        ListNode headNode = NU.create(new ListNode[]{
                        N.Ln(1),
                        N.Ln(2),
                        centerNode,
                        N.Ln(4),
                        N.Ln(5),
                        endNode
                }
        );

        ListNode node = detectCycle(headNode);
        NU.printNode(node);

    }

    public static ListNode detectCycle(ListNode head) {
        ListNode node = isCycle(head);
        if (node != null) {
            node = findNode(head, node);
        }

        return node;
    }


    /**
     * 判断是不是圆环
     * 假设一个链表环 1,2,3,4,5,6
     * 那么在当慢指针移动到6的时候两个指针会相交，因为快指针移动是慢指针的2倍，所以需要快指针执行完一圈后第二圈与慢指针相遇
     * 可以理解为相遇的一个条件是：慢指针移动到链表环的长度时就会相遇
     *
     *假设一个链表环 1,2,3,4,5,6 环节点为3
     *那么1，2 节对于慢指针便是多移动的，那么慢指针就会相比于整个链表为环的情况提前两个节点到达相遇点， 在4节点相遇
     *
     *查找环节点
     * 找到快慢指针相遇节点，重相遇节点的next 节点开始到环节点和从链表的起始位置到环节点距离相同
     * 从特殊场景推理：
     * 1 .链表头节点是环节点， 那么快慢指针一定在链表的最后一个节点相遇，那么最后一个节点的next 就是环节点
     * 2. 链表中某一位置为环节点，那么其实位置到环节点的距离就是快慢指针就会提前相遇的距离，所以，从相遇节点的next 节点到环节点的距离和头节点到环节点距离相等
     *
     * 所以，如果要找环节点，就可以通过快慢指针相遇节点和链表起始节点开始移动，直到相遇便是环节点
     */
    private static ListNode isCycle(ListNode head) {

        if (head == null) {
            return null;
        }

        ListNode firstNode = head.next;
        ListNode secondNode = head;

        while (firstNode != null) {
            if (firstNode == secondNode) {
                return firstNode;
            }

            if (firstNode.next == null) {
                return null;
            } else {
                firstNode = firstNode.next.next;
                secondNode = secondNode.next;
            }
        }

        return null;

    }

    /**
     * 查找相交节点
     * @param head : 原始链表头节点
     * @param head1 ： 使用快慢指针指向的相同节点
     * @return
     */
    private static ListNode findNode(ListNode head, ListNode head1) {
        ListNode node1 = head;
        ListNode node2 = head1.next;
        while (true) {
            if (node1 == node2) {
                return node1;
            }

            node1 = node1.next;
            node2 = node2.next;

        }
    }

}
