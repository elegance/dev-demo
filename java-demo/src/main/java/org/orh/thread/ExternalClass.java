package org.orh.thread;
public class ExternalClass {
    private String userName;
    private String password;

    // 内部类， 把类比喻成一个鸡蛋，内部类是蛋黄，外部类是蛋壳
    // 普通内部类(非static) 那相当于生鸡蛋，没有鸡蛋壳（外部类没有实例化），蛋黄也就不复存在 ----- 生鸡蛋： 壳之不存，黄之焉附
    // 没有 `public`标识，只有在同一个包的其他类可访问、实例化
    class InnerClass {
        private String age;
        private String address;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }

    // 内部静态类 , 相当于熟鸡蛋，没有鸡蛋壳，蛋黄也可以是完好的（可以实例化）， -----  熟鸡蛋：唇亡齿寒 ，照样可以嚼东西（没有蛋壳，蛋黄可以用来做卤蛋呀...）
    static class InnerStaticClass {
        private String age;
        private String address;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
