package pattern.ch04.ext;

/**
 * jdk 静态代理，硬编码，只为特定类的特定方法 织入 加强
 */
public class JdkStaticProxy {
    
    public static void main(String[] args) {
        UserServiceImpl userImpl = new UserServiceImpl();
        UserServiceProxy proxy = new UserServiceProxy(userImpl);
        proxy.addUser();
        System.out.println("--------------分割线------------------");
        proxy.editUser();
    }

    // 1. 业务接口
    static interface UserService {
        
        // 增加一个用户
        public void addUser();
        
        // 编辑一个用户
        public void editUser();
    }
    
    // 2. 业务实现类
    static class UserServiceImpl implements UserService {
        public void addUser() {
            System.out.println("增加一个用户。。。");
        }

        public void editUser() {
            System.out.println("编辑一个用户。。。");
        }
    }
    
    // 3. 代理类
    static class UserServiceProxy implements UserService {
        private UserServiceImpl userImpl;
        
        public UserServiceProxy(UserServiceImpl userImpl) {
            this.userImpl = userImpl;
        }

        public void addUser() {
            System.out.println("代理类方法，进行了增强。。");
            System.out.println("事务开始");

            userImpl.addUser(); //调用委托类的方法

            System.out.println("处理结束");
        }

        @Override
        public void editUser() {
            System.out.println("代理类方法，进行了增强。。");
            System.out.println("事务开始");

            userImpl.editUser(); //调用委托类的方法

            System.out.println("事务结束");
        }
        
    }
}
