import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatError {
    // 跟中 SimpleDateFormat 可以发现 内部 存储了全局变量： Calendar，也就是单个实例，多线程 都会访问操作 这个Calendar，造成混乱，最终转换错误或出现转换异常
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 特意把他定义为全局，因为这更符合真实的应用情况

    public static void main(String[] args) {

        String[] dateStrArr = new String[] {"2000-01-01", "2000-01-02", "2000-01-03", "2000-01-04", "2000-01-05", "2000-01-06",
                "2000-01-07", "2000-01-08", "2000-01-09", "2000-01-10"};

        for (int i = 0; i < dateStrArr.length; i++) {
            final int tmpi = i;
            new Thread(() -> Service.checkParse(dateStrArr[tmpi]), "thread-" + tmpi).start();
        }
    }

    static class Service {

        public static void checkParse(String dateStr) {
            try {
                Date dateRef = sdf.parse(dateStr); // 将需要 转换 的【字符串】转为【Date】

                String newDateString = sdf.format(dateRef); // 取出刚 转换的【Date】结果，格式化为【字符串】用于验证

                if (!newDateString.equals(dateStr)) { // 验证：如果前后不等，则是错误的
                    System.out.println("ThreadName=" + Thread.currentThread().getName() + " 【准换错误，不符合预期】  日期字符串：" + dateStr + " 转换成的日期为："
                            + newDateString);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
