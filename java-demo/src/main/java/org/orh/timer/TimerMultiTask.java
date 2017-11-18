package org.orh.timer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerMultiTask {

    public static Timer daemonTimer = new Timer();

    public static void main(String[] args) throws ParseException {
        MyTask myTask1 = new MyTask("myTask1");
        MyTask myTask2 = new MyTask("myTask2");

        String dateTimeStr1 = "2017-03-29 13:37:50"; // myTask1 在 50秒时开始执行，会耗时 5秒
        String dateTimeStr2 = "2017-03-29 13:37:53"; // myTask2 计划在53秒开始，但实际是在 55秒开始的，任务会被前一个任务阻塞

        Date dateRef1 = defaultFmt().parse(dateTimeStr1);
        Date dateRef2 = defaultFmt().parse(dateTimeStr2);

        System.out.printf("字符串时间：%s  当前时间：%s\n", defaultFmt().format(dateRef1), defaultFmt().format(new Date()));
        System.out.printf("字符串时间：%s  当前时间：%s\n", defaultFmt().format(dateRef2), defaultFmt().format(new Date()));

        daemonTimer.schedule(myTask1, dateRef1);
        daemonTimer.schedule(myTask2, dateRef2);
    }

    static class MyTask extends TimerTask {

        private String taskName;

        public MyTask(String taskName) {
            super();
            this.taskName = taskName;
        }

        @Override
        public void run() {
            System.out.println(this.taskName + " 开始运行了! 时间为：" + defaultFmt().format(new Date()));
            sleep(5 * 1000);
            System.out.println(this.taskName + " 运行结束了! 时间为：" + defaultFmt().format(new Date()));
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static SimpleDateFormat defaultFmt() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
