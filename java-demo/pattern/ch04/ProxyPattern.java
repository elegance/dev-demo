package pattern.ch04;

public class ProxyPattern {
    
    public static void main(String[] args) {
        Proxy proxy = new Proxy();
        proxy.request();
    }

    static abstract class Subject {
        public abstract void request();
    }

    static class RealSubject extends Subject {

        public void request() {
            System.out.println("真实的请求");
        }
    }

    static class Proxy extends Subject {
        RealSubject realSubject;

        @Override
        public void request() {
            if (realSubject == null) {
                realSubject = new RealSubject();
            }
            realSubject.request();
        }
    }
}
