package group;

public class GetParentGroup {
    
    public static void main(String[] args) {
        // main
        System.out.println("线程：" + Thread.currentThread().getName() + " 所在线程组名为：" + Thread.currentThread().getThreadGroup().getName());

        // system
        System.out.println("线程：" + Thread.currentThread().getName() + " 所在线程组的父线程组名为：" + Thread.currentThread().getThreadGroup().getParent().getName());

        // null
        System.out.println("线程：" + Thread.currentThread().getName() + " 所在线程组的父线程组的父线程组：" + Thread.currentThread().getThreadGroup().getParent().getParent());
    }
}
