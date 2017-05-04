package basic;

import java.util.HashSet;
import java.util.Set;

public class OverrideEquals {
    public static void main(String[] args) {
        User u1 = new User();
        u1.setId(1L);
        u1.setUserName("zhang");

        User u2 = new User();
        u2.setId(1L);
        u2.setUserName("Li");

        System.out.println("u1 hasCode:" + u1.hashCode());
        System.out.println("u2 hasCode:" + u2.hashCode());
        System.out.println("u1.equals(u2):" + u1.equals(u2)); // 名字不同，但符合我们定义的ID相同，即相等

        Set<User> set = new HashSet<User>();
        set.add(u1);
        System.out.println("add u1 after:" + set.size());
        set.add(u2);
        System.out.println("add u2 after:" + set.size()); // 重写了 hashCode 方法，hashSet 认为其对象也是重复的
        set.forEach(System.out::println); // 可以看出 hashSet 判断已存在时不会再添加
        u2.setId(2L);
        set.add(u2);
        System.out.println("add u2 after:" + set.size()); // 改变id即可完成添加
    }

    /**
     * 覆盖equals方法看起来似乎很简单，但是如果覆盖不当会导致错误，并且后果相当严重。《Effective Java》一书中提到“最容易避免这类问题的办法就是不覆盖equals方法”
     * <br>
     * 1. 设定你的“相等”规则，确保严谨 ====> 这里是 id值相等则认为是 equal <br>
     * 2. 重写 equals方法、hasCode 方法
     */
    static class User {
        /**
         * id 唯一
         */
        private Long id;

        private String userName;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof User) {
                User user = (User) obj;
                return this.id.equals(user.id);
            }
            return super.equals(obj);
        }

        /**
         * HashMap/HashSet存储此对象时，会根据hashcode查询，提高查询速度
         */
        @Override
        public int hashCode() {
            return this.id.hashCode();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public String toString() {
            return "User [id=" + id + ", userName=" + userName + "]";
        }
    }
}
