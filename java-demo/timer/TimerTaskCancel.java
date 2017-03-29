import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskCancel {
    public static Timer timer = new Timer();

    public static void main(String[] args) throws ParseException {
        MyTaskA taskA = new MyTaskA();
        MyTaskB taskB = new MyTaskB();

        String dateTimeStr = "2017-03-29 14:13:50";
        Date dateRef = defaultFmt().parse(dateTimeStr);

        System.out.printf("字符串时间：%s  当前时间：%s\n", defaultFmt().format(dateRef), defaultFmt().format(new Date()));

        timer.scheduleAtFixedRate(taskA, dateRef, 4000);
        timer.scheduleAtFixedRate(taskB, dateRef, 4000);

    }

    static class MyTaskA extends TimerTask {
        public void run() {
            System.out.println("A 运行了! 时间为：" + defaultFmt().format(new Date()));
            this.cancel(); // 运行一次后就 cancel 取消掉
        }
    }

    static class MyTaskB extends TimerTask {
        public void run() {
            System.out.println("B 运行了! 时间为：" + defaultFmt().format(new Date()));
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
