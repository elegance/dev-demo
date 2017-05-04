import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest4 {

    public static Timer timer = new Timer();

    public static void main(String[] args) throws ParseException {
        MyTask myTask = new MyTask();

        System.out.println("当前时间：" + defaultFmt().format(new Date()));
        timer.schedule(myTask, 3000, 3000); // 当前时间往后推3秒开始执行， 每3秒 执行一次myTask 
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
