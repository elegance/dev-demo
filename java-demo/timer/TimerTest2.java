import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest2 {
    public static Timer timer = new Timer();

    public static void main(String[] args) throws ParseException {
        MyTask myTask = new MyTask("myTask");
        String dateTimeStr = "2017-03-29 13:52:50";
        Date dateRef = defaultFmt().parse(dateTimeStr);
        System.out.printf("字符串时间：%s  当前时间：%s\n", defaultFmt().format(dateRef), defaultFmt().format(new Date()));

        timer.schedule(myTask, dateRef, 2000); // 从 dateRef 开始，每2000 millis 执行一次 myTask任务，myTask 任务执行需要 3000 millis，则后续每次开始的任务则会类推延时

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
            sleep(3 * 1000);
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
