package org.orh.lock.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 这样的一个题目：
 * 5个学生一起参加考试，一共有三道题，要求所有学生到齐才能开始考试，全部同学都做完第一题，学生才能继续做第二题，全部学生做完了第二题，才能做第三题，所有学生都做完的第三题，考试才结束
 *
 */
public class StudentTask implements Runnable {

    public static void main(String[] args) {
        MyPhaser phaser = new MyPhaser();
        StudentTask[] studentTasks = new StudentTask[5];
        for (int i = 0; i < studentTasks.length; i++) {
            studentTasks[i] = new StudentTask(phaser);
            phaser.register(); // 注册一次表示phaser维护的线程个数
        }

        Thread[] threads = new Thread[studentTasks.length];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(studentTasks[i], "student-" + (i + 1));
            threads[i].start();
        }

    }

    private Phaser phaser;

    public StudentTask(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        String studentName = Thread.currentThread().getName();
        System.out.println(studentName + " 到达考场");
        phaser.arriveAndAwaitAdvance();

        System.out.println(studentName + " 做第一题时间...");
        doExercise1();
        System.out.println(studentName + " 第一题完成.");
        phaser.arriveAndAwaitAdvance();

        System.out.println(studentName + " 做第二题时间...");
        doExercise2();
        System.out.println(studentName + " 第二题完成.");
        phaser.arriveAndAwaitAdvance();

        System.out.println(studentName + " 做第三题时间...");
        doExercise3();
        System.out.println(studentName + " 第三题完成.");
        phaser.arriveAndAwaitAdvance();

    }

    private void doExercise1() {
        long duration = (long) (Math.random() * 10);
        sleepXSeconds(duration);
    }

    private void doExercise2() {
        long duration = (long) (Math.random() * 10);
        sleepXSeconds(duration);
    }

    private void doExercise3() {
        long duration = (long) (Math.random() * 10);
        sleepXSeconds(duration);
    }

    private void sleepXSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
