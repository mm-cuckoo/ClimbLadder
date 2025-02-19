package com.cko.algorithm.linkedlist;

import com.cko.algorithm.ListNode;
import com.cko.algorithm.NU;

/**
 * https://leetcode.cn/problems/merge-two-sorted-lists/description/?envType=study-plan-v2&envId=top-100-liked
 * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 * 输入：l1 = [1,2,4], l2 = [1,3,4]
 * 输出：[1,1,2,3,4,4]
 */
public class 合并两个有序链表21 {

    public static void main(String[] args) {
        ListNode listNode1 = NU.create(new int[]{1, 2, 3, 4, 5});
        ListNode listNode2 = NU.create(new int[]{1, 2, 3, 4, 5});

        NU.printNodes(mergeTwoLists(listNode1,listNode2));
    }

    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // 先处理特殊场景
        if (list1 == null) {
            return list2;
        }

        if (list2 == null) {
            return list1;
        }

        ListNode firstNode = null;// 返回的头节点
        ListNode curNode = null;//移动的尾节点

        // 如果两个节点都不为null， 进行比较并插入到新的链表中
        while (list1 != null && list2 != null) {
            ListNode node;
            // 取出小的节点到node 中
            if (list1.value <= list2.value) {
                node = list1;
                list1 = list1.next;
            } else {
                node = list2;
                list2 = list2.next;
            }
            node.next = null; // 清空next

            if (firstNode == null) {
                firstNode = node;
                curNode = node;
            } else {
                curNode.next = node;
                curNode = node;
            }
        }

        // 到这里如果有链表中还有节点，说明另外一个链表已经没有节点了，只需要把剩余的节点拼接到新的链表上即可
        if (list1 != null) {
            curNode.next = list1;
        }

        if (list2 != null) {
            curNode.next = list2;
        }

        return firstNode;

    }
}
