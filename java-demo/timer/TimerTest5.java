import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest5 {

    public static Timer timer = new Timer();

    public static int runCount = 0; // timer内部是队列方式，所以这里没有线程安全的问题

    public static void main(String[] args) throws ParseException {
        MyTask myTaskA = new MyTask("task-A");

        String dateStr = "2017-03-29 16:45:35";
        Date dateRef = defaultFmt().parse(dateStr);

        System.out.println("字符串时间：" + dateStr + " 当前时间：" + defaultFmt().format(new Date()) + "\n");

         // timer.schedule(myTaskA, dateRef, 4000);
         // timer.scheduleAtFixedRate(myTaskA, dateRef, 4000); 

         // timer.schedule(myTaskA, dateRef, 2000); // 上次任务一旦结束，就开始新的任务
        
          timer.scheduleAtFixedRate(myTaskA, dateRef, 2000);
        
        // schedule: task, firstTime, period
        // 日程表：任务，什么时间开始，多久做一次
        //     正常的无异议情况：开始时间在将来，间隔周期的时间 比 单次 任务执行完的时间 长，即新的任务 执行时，前一次的任务一定执行完成了
         
        //     例外1： 如果开始时间，已经过去了？ 
        //     A: schedule: 立即执行，下一次的执行时间 = 上一次开始时间 + 间隔时间  ； scheduleAtFixRate: 立即执行，下一次的执行时间 = 上一次的结束时间
         
        //     例外2： 如果间隔时间短于单次任务的时间，前一次任务还没结束，新任务的执行点就到来了？
        //     A: schedule: 首次到点执行，下一次的执行时间 = 上一次结束的时间；scheduleAtFixRate: 首次到点执行，下一次的执行时间 = 上一次的结束时间
         
        //     例外3：以上两种例外同时出现？
        //     A: schedule: 立即执行，下一次的执行时间 = 上一次的结束时间；scheduleAtFixRate:立即执行， 下一次的执行时间 = 上一次的结束时间
    }

    static class MyTask extends TimerTask {
        private String taskName;
        
        public MyTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            System.out.println(this.taskName + " ------------begin 运行了! 时间为：" + defaultFmt().format(new Date()));
            sleep(3000);
            System.out.println(this.taskName + "  end 运行了! 时间为：" + defaultFmt().format(new Date()));

            runCount++;
            if (runCount == 5) { // 运行满5次，则清空 timer的任务列表，退出
                timer.cancel();
            }
        }
    }

    public static SimpleDateFormat defaultFmt() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
