package org.orh.lock.phaser;

import java.util.concurrent.Phaser;

public class MyPhaser extends Phaser {

    /**
     * 1. 每一阶段执行完后，此方法被调用
     * 2. 此方法返回 true 意味着 Phase 被终止，因此可以巧妙的设置此方法的返回值来终止所有线程。
     */
    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case 0:
                return studentArrived();
            case 1:
                return finishFirstExercise();
            case 2:
                return finishSecondExercise();
            case 3:
                return finishExam();
            default:
                return true;
        }
    }

    private boolean studentArrived() {
        System.out.println("学生准备好了，学生人数：" + getRegisteredParties());
        return false;
    }

    private boolean finishFirstExercise() {
        System.out.println("第一题所有学生做完");
        return false;
    }

    private boolean finishSecondExercise() {
        System.out.println("第二题所有学生做完");
        return false;
    }

    private boolean finishExam() {
        System.out.println("第三题所有学生做完，完成考试");
        return true;
    }
}
