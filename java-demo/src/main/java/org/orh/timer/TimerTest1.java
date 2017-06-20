package org.orh.timer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest1 {
    // public static Timer timer = new Timer(); // Timer 内部的TimerThread 继承了 Thread类，new 实例化时 这个线程就
    // start了
    public static Timer daemonTimer = new Timer(true); // 构造函数 传入true，表示内部线程为
                                                       // 守护线程，当系统没有非守护线程时，timerTask执行完，程序就会退出

    public static void main(String[] args) throws ParseException {
        MyTask myTask = new MyTask();
        String dateTimeStr = "2017-03-29 11:28:50";
        Date dateRef = defaultFmt().parse(dateTimeStr);
        System.out.printf("字符串时间：%s  当前时间：%s\n", defaultFmt().format(dateRef), defaultFmt().format(new Date()));

        // timer.schedule(myTask, dateRef); // 任务执行完后，timer内部线程，所以进程还未销毁，控制台按钮呈红色状态
        daemonTimer.schedule(myTask, dateRef); // 守护线程的timer运行完后程序就结束了
    }

    static class MyTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("myTask 运行了! 时间为：" + defaultFmt().format(new Date()));
        }
    }

    public static SimpleDateFormat defaultFmt() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
