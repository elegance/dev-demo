package org.orh.data.structure;

/**
 * 模拟实现-链式存储的线性表
 */
public class MyLinkList<E> {
    protected int size; // 链表中存储的元素个数

    private Node<E> first; // 单链表，用first 记录 链表头

    static class Node<E> {
        E data; // 值域
        Node<E> next; // 指针域
    }

    public MyLinkList() {}

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        first = null;
        size = 0;
    }

    public E get(int index) {
        rangeCheck(index);
        return node(index).data;
    }

    public int indexOf(E element) {
        int cur = 0;

        if (element != null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (element.equals(x.data)) {
                    return cur;
                }
                cur++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.data == null) {
                    return cur;
                }
                cur++;
            }
        }
        return -1;
    }

    private Node<E> node(int index) {
        int cur = 0;
        Node<E> x = null;

        for (x = first; x != null; x = x.next) {
            if (cur == index) {
                break;
            }
            cur++;
        }
        return x;
    }

    /**
     * 在链表末尾添加一个新元素
     */
    public boolean add(E element) {
        return add(size, element);
    }

    public boolean add(int index, E element) {
        checkPositionIndex(index);

        Node<E> newNode = new Node<>();
        newNode.data = element;

        if (index == 0) {
            newNode.next = first;
            first = newNode;
        } else {
            Node<E> pre = node(index - 1); // 获取前一个元素
            newNode.next = pre.next; // 新手牵后手
            pre.next = newNode; // 前手牵新手
        }

        size++;
        return true;
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public E remove(int index) {
        checkPositionIndex(index);

        Node<E> removeE = null;

        if (index == 0) {
            removeE = first;
            first = first.next;
        } else {
            Node<E> pre = node(index - 1); // 获取前一个元素
            removeE = pre.next;
            pre.next = removeE.next;
        }
        size--;
        return removeE.data;
    }

    public int size() {
        return size;
    }

    // 范围检查
    private void rangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public static void main(String[] args) {
        MyLinkList<Integer> list = new MyLinkList<>();
        list.add(1111);
        printLinkList(list);

        list.add(2222);
        printLinkList(list);

        list.add(3333);
        printLinkList(list);

        list.add(1, 4444);
        printLinkList(list);

        list.add(5555);
        printLinkList(list);

        System.out.println(list.indexOf(2222));
        System.out.println(list.indexOf(null));

        list.remove(3);
        printLinkList(list);

        list.remove(0);
        printLinkList(list);

        list.remove(list.indexOf(5555));
        printLinkList(list);
        System.out.println(list.indexOf(null));

        list.add(null);
        System.out.println(list.indexOf(null));

        printLinkList(list);
        System.out.println(list.size());

    }

    private static void printLinkList(MyLinkList<Integer> list) {
        StringBuilder sbd = new StringBuilder("[");
        for (Node<Integer> n = list.first; n != null; n = n.next) {
            sbd.append(n.data);
            if (n.next != null) {
                sbd.append(", ");
            }
        }
        sbd.append("]");
        System.out.println(sbd.toString());
    }
}
