package org.orh.jdk8;

import org.junit.Test;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public class UseOptional {

    @Test
    public void whenOriginal() {
        //1. 原始做法，每次判断 user ！= null
        User user = null;
        if (user != null) {
            System.out.println("username: " + user.getUsername());
        }
    }

    @Test
    public void whenUseOptional1() {
        //2. 使用 user.isPresent 与 user == null 好像没什么改进，还有多余的消耗
        Optional<User> user1 = Optional.ofNullable(null);
        if (user1.isPresent()) {
            System.out.println("username: " + user1.get().getUsername());
        } else {
            System.out.println("username is null");
        }
    }

    /**
     * ifPresent： 传入 function 保证方法体 accept 的参数不 为 null
     * ifPresent(Consume<? super T> consumer) => consumer.accept(value)
     */
    @Test
    public void whenScientificUse() {
        // 科学用法：

        Optional<User> user2 = Optional.ofNullable(getUserById("1"));
        user2.ifPresent(u -> System.out.println(u.getUsername()));
    }

    /**
     * orElse: 使用 Else 来指定另外的值，相当于是设默认值
     */
    @Test
    public void whenUseOrElse() {
        //2. orElse
        User user3 = Optional
                .ofNullable(getUserById("2"))
                .orElse(new User("2", "unknowUser", 0));

        System.out.println(user3.getUsername());
    }

    /**
     * orElseGet: 相比orElse 设定个默认值，这里是传入 的 function 来设定默认值
     */
    @Test
    public void whenUseOrElseGet() {
        //3. orElseGet
        User user4 = Optional
                .ofNullable(getUserById("2"))
                .orElseGet(() -> new User("2", "unknow user", 0));
    }

    /**
     * orElseThrow： 为null时抛出运行时错误
     */
    @Test(expected = EntityNotFoundException.class)
    public void whenUseOrElseThrow() {
        //4. orElseThrow => 可配合自定义异常，spring mvc exception handler
        User user5 = Optional.ofNullable(getUserById("2"))
                .orElseThrow(() -> new EntityNotFoundException("id 为 " + 2 + " 的用户不存在"));
    }

    /**
     * 在 对象不为空时， 获取对象值，做map映射转换
     */
    @Test
    public void whenUseMap() {
        Optional<String> username = Optional.ofNullable(getUserById("1"))
                .map(user -> user.getUsername())
                .map(name -> name.toLowerCase())
                .map(name -> name.replace('-', ' '));

        System.out.println("Username is:" + username.orElse("Un know"));
    }

    /**
     * flatMap 可支持 Optional的出参，将其摊平
     */
    @Test
    public void whenUseFlatMap() {
        Optional<String> username = Optional.ofNullable(getUserById("1"))
                .flatMap(user -> Optional.of(user.getUsername()))
                .flatMap(name -> Optional.of(name.toLowerCase()));
        System.out.println("username is " + username.orElse("UnKnow"));
    }

    /**
     * 过滤输出
     */
    @Test
    public void whenUseFilter() {
        Optional<String> username = Optional.ofNullable(getUserById("1"))
                .filter(user -> user.getAge() > 18)
                .map(user -> user.getUsername());
        System.out.println("username is " + username.orElse("UnKnow"));
    }


    static User getUserById(String id) {
        if ("1".equals(id)) {
            return new User(id, "test-A", 18);
        }
        return null;
    }

    static class User {
        public User(String id, String username, int age) {
            this.id = id;
            this.username = username;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    ", age=" + age +
                    '}';
        }

        private String id;
        private String username;
        private int age;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
