package org.orh.data.structure;

import java.util.Arrays;

/**
 * 模拟实现-顺序存储的线性表
 * 仅供学习使用，未经完整测试，不可用于生产
 */
public class MyArrayList<E> {
    private static final int DEFAULT_CAPACITY = 10;

    Object elementData[]; // 存储元素的数据

    private int size; // 实际存储的元素个数

    public MyArrayList() {
        this(DEFAULT_CAPACITY);// 默认开辟10个长度空间给数组使用
    }

    // 顺序存储避免不了-先申请空间-面临的问题是 初始多了浪费、初始少了不够用
    public MyArrayList(int initCapacity) {
        if (initCapacity > 0) {
            this.elementData = new Object[initCapacity];
        } else {
            throw new IllegalArgumentException("Ileegal Capacity:" + initCapacity);
        }
    }

    public int size() {
        return size;
    }

    // 数据结构定义的方法

    // 是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 根据记录数字，清空数组已经存储的元素
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0; // 更新记录的个数为0
    }

    // 根据索引获取
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }

    // 根据元素得到索引
    public int indexOf(E e) {
        if (e == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (elementData[i].equals(e)) {
                    return i;
                }
            }
        }
        return -1;
    }

    // 线性表-末尾添加元素
    public boolean add(E e) {
        return add(size, e);
    }

    private void grow() {
        // grow 增长多少呢：增大本身的一半
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // 取elementData 长度的一半
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    /**
     * 指定位置添加元素
     * 
     * @param index 可以基于现有List元素末尾添加，但是指定的位置不可大于现有最大元素的位置
     * @param element
     * @return
     */
    public boolean add(int index, E element) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 如果线性表长度大于等于数组长度，则抛出异常或动态增加容量
        if (elementData.length - size <= 1) { // 剩余容量小于1 ，则增长之
            grow();
        }
        // 根据数据结构描述：指定位置后的元素都完后挪一位
        int cur = size;
        while (cur > index) {
            elementData[cur] = elementData[--cur]; // 元素位置往后挪动一个
        }
        elementData[index] = element;
        size++;

        return true;
    }

    /**
     * 根据索引移除指定位置的元素: **指定位置往后的元素都会往后前挪动一位**
     * 
     * @param index
     * @return
     */
    public E remove(int index) {
        rangeCheck(index);

        E removeE = elementData(index);
        for (int i = index; i < size;) {
            elementData[i] = elementData[++i];
        }
        System.out.println("remove:" + removeE);
        elementData[--size] = null; // 之前的最后一项置为 null
        return removeE;
    }

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    // 范围检查
    private void rangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public static void main(String[] args) {
        MyArrayList<Integer> list = new MyArrayList<Integer>(3);
        System.out.println("constructor(3):" + Arrays.toString(list.elementData));
        list.add(1111);
        System.out.println("add 1111:" + Arrays.toString(list.elementData));

        list.add(2222);
        System.out.println("add 2222:" + Arrays.toString(list.elementData));

        list.add(3333);
        System.out.println("add 3333:" + Arrays.toString(list.elementData));

        list.add(1, 4444);
        System.out.println("add(1, 4444):" + Arrays.toString(list.elementData));

        list.add(5555);
        System.out.println("add 5555:" + Arrays.toString(list.elementData));

        System.out.println("indexOf(2222): " + list.indexOf(2222)); // 2
        System.out.println("indexOf(null): " + list.indexOf(null)); // -1
        
        list.remove(3);
        System.out.println("remove 3:" + Arrays.toString(list.elementData));
        list.remove(0);
        System.out.println("remove 0:" + Arrays.toString(list.elementData));
        list.remove(list.indexOf(5555));
        System.out.println("remove (list.indexOf(5555)):" + Arrays.toString(list.elementData));
        System.out.println("indexOf(null): " + list.indexOf(null)); // -1
        list.add(null);
        System.out.println("indexOf(null): " + list.indexOf(null)); // -1
        System.out.println(list.size());
    }
}
