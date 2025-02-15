package com.cko.algorithm.linkedlist;

import com.cko.algorithm.ListNode;
import com.cko.algorithm.N;
import com.cko.algorithm.NU;

/**
 * https://leetcode.cn/problems/intersection-of-two-linked-lists/description/?envType=study-plan-v2&envId=top-100-liked
 * 给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null 。
 */
public class 相交链表160 {

    public static void main(String[] args) {

        ListNode centerNode = N.Ln(6);

        ListNode headA = NU.create(new ListNode[]{
                N.Ln(1),
                N.Ln(3),
                N.Ln(4),
                centerNode,
                N.Ln(5),
                N.Ln(10),
                N.Ln(12),
                N.Ln(13),
        });

        ListNode headB = NU.create(new ListNode[]{
                N.Ln(100),
                N.Ln(110),
                N.Ln(120),
                N.Ln(130),
//                centerNode,
        });

        NU.printNode(getIntersectionNode(headA,headB));

    }
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {

        if (headA == null || headB == null) {
            return null;
        }

        ListNode startA = headA;
        ListNode startB = headB;


        /**
         * 思路，同时移动 headA 和 headB 的节点并进行比较 如果 headA 移动到结尾，则跳到headB 上继续执行， headB 链也是如此
         * 如果有交点一定会出现同时移动到同一个节点上
         * 如果没有交点如何结束， 当两个链都移动完一遍后 （headA + headB）后一定都会同时处于null , 此时循环结束
         */
        while (startA != startB) {

            if (startA == null) {
                startA = headB;
            } else {
                startA = startA.next;
            }

            if (startB == null) {
                startB = headA;
            } else {
                startB = startB.next;
            }
        }

        return startA;
    }}
