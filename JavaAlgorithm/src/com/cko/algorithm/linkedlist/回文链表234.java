package com.cko.algorithm.linkedlist;

import com.cko.algorithm.ListNode;
import com.cko.algorithm.NU;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/palindrome-linked-list/description/?envType=study-plan-v2&envId=top-100-liked
 * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
 * 输入：head = [1,2,2,1]
 * 输出：true
 *
 * 输入：head = [1,2]
 * 输出：false
 *
 * 输入：head = [1]
 * 输出：true
 */
public class 回文链表234 {

    public static void main(String[] args) {

        ListNode head = NU.create(new int[]{1,1,2,1});

        boolean result = isPalindrome(head);

        System.out.println(result);

    }

    public static boolean isPalindrome(ListNode head) {

        /**
         * 处理特殊情况
         */
        if (head == null) {
            return false;
        }

        /**
         * 单个node是回文
         */
        if (head.next == null) {
            return true;
        }

        /**
         * 使用list保存所有node
         */
        List<ListNode> listNode = new ArrayList<>();
        ListNode tmpNode = head;
        while (tmpNode != null) {
            listNode.add(tmpNode);
            tmpNode = tmpNode.next;
        }


        /**
         * 依次从list中的两头各取node 进行比较，如果出现两个部相等则认为不是回文，
         * 如果都遍历完没有遇到不相同的两个node 则认为是回文
         */
        int leftIndex = 0;
        int rightIndex = listNode.size() - 1;
        for (int i = 0; i < listNode.size() / 2; i++) {
            ListNode leftNode = listNode.get(leftIndex);
            ListNode rightNode = listNode.get(rightIndex);

            if (leftNode.value != rightNode.value) {
                return false;
            }

            leftIndex++;
            rightIndex--;

        }


        return true;

    }
}
