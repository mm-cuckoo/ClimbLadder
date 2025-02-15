package com.cko.algorithm.linkedlist;

import com.cko.algorithm.ListNode;
import com.cko.algorithm.N;
import com.cko.algorithm.NU;

/**
 * https://leetcode.cn/problems/reverse-linked-list/description/?envType=study-plan-v2&envId=top-100-liked
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 */
public class 反转链表206 {
    public static void main(String[] args) {
        ListNode base = NU.create(new int[]{1, 2, 3, 4, 5});

        NU.printNodes(reverseList(base));
    }

    public static ListNode reverseList(ListNode head) {

        /**
         * 创建一个反转后resultHead虚拟头节点，让其一直保持在头部，每次取出一个node都会插入到resultHead.next 位置，
         */
        ListNode resultHead = new ListNode();
        ListNode curNode = head;
        while (curNode != null) {
            ListNode resultEndNode = resultHead.next;
            ListNode addNode = curNode;
            curNode = curNode.next;

            resultHead.next = addNode;
            addNode.next = resultEndNode;
        }

        return resultHead.next;

    }

    public static ListNode reverseList2(ListNode head) {

        /**
         * 排除一些特殊情况
         */
        if (head == null || head.next == null) {
            return head;
        }

        /**
         * 到这里，head 和 head.next 一定是有数据，
         */
        ListNode lastNode = head; // 取出第一个node
        ListNode ptrNode = head.next; // 指向第一个node 的下一个node
        head.next = null; // 清空第一个node next 指向
        /**
         * 判断当前指向位置的next是否有node， 如果有继续取需要反转的node， 如果没有表示当前已经到链表结尾
         * 这里要注意一下，因为while循环判断的是当前指向的node的next，所以当ptrNode.next为null时，
         * lastNode 指向了已经反转的链表中最开始位置，ptrNode 指向需要反转链表的最后一个node
         * eg:
         * lastNode = [4,3,2,1] 中 4位置
         * ptrNode = [5]
         * 所以最后一步需要把ptrNode 放到lastNode 头部
         *
         */
        while (ptrNode.next != null) {
            ListNode curNode = ptrNode;
            ptrNode = ptrNode.next;
            curNode.next = lastNode;
            lastNode = curNode;
        }
        // 最后一步需要把ptrNode 放到lastNode 头部
        ptrNode.next = lastNode;

        // 返回头部node
        return ptrNode;


    }
}
