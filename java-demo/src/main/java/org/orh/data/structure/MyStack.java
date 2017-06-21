package org.orh.data.structure;

import java.util.Arrays;
import java.util.EmptyStackException;

public class MyStack<E> {

    private Object[] elementData; // 存储入栈的元素
    private int size; // 栈中元素个数
    // private E top; // 栈顶元素指针

    public MyStack() {
        elementData = new Object[10];
    }

    public int size() {
        return size;
    }

    public void push(E element) {
        ensureCapacity(size + 1);
        elementData[size] = element;
        size++;
    }

    public E peek() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elementData(size - 1);
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        elementData[size] = null;
        E popE = elementData(size - 1);
        size--;
        return popE;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - elementData.length < 0) {
            grow();
        }
    }

    private void grow() {
        elementData = Arrays.copyOf(elementData, elementData.length);
    }

    @SuppressWarnings("unchecked")
    private E elementData(int index) {
        return (E) elementData[index];
    }

    public static void main(String[] args) {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        System.out.println(stack.peek());
        System.out.println(stack.pop());

        stack.push(2);
        System.out.println(stack.pop());

        stack.push(3);
        System.out.println(stack.pop());

        stack.push(4);
        stack.push(4);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }
}
