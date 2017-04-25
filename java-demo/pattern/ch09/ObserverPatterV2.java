package pattern.ch09;

import java.util.ArrayList;
import java.util.List;

public class ObserverPatterV2 {
    public static void main(String[] args) {
        // 前台-童子喆
        Secretary tongzize = new Secretary();

        // 看股的两位同事
        Observer tongshi1 = new StockObserver("魏关姹", tongzize);
        Observer tongshi2 = new NBAObserver("易管查", tongzize);

        // 前台记录下两位同事
        tongzize.attach(tongshi1);
        tongzize.attach(tongshi2);

        // 发现boss回来
        tongzize.setAction("Boss回来了！");

        // 通知记录的两位同事
        tongzize.notifyAllObservers();
    }

    static class Secretary {
        // 同事列表
        private List<Observer> observers = new ArrayList<Observer>();

        // 前台通过电话，所说的话或所做的事
        private String action;

        // 增加
        public void attach(Observer observer) { // 针对抽象编程，减少与具体类的耦合
            observers.add(observer);
        }

        // 减少
        public void detach(Observer observer) {
            observers.remove(observer);
        }

        public void notifyAllObservers() {
            for (Observer observer : observers) {
                observer.update();
            }
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    /**
     * 抽象的观察者
     */
    static abstract class Observer {
        protected String name;
        protected Secretary sub;

        public Observer(String name, Secretary sub) {
            this.name = name;
            this.sub = sub;
        }

        public abstract void update();
    }

    /**
     * 看股票的同事
     */
    static class StockObserver extends Observer {

        public StockObserver(String name, Secretary sub) {
            super(name, sub);
        }

        public void update() {
            System.out.printf("%s %s 关闭股票行情，继续工作！\n", sub.getAction(), this.name);
        }
    }

    static class NBAObserver extends Observer {

        public NBAObserver(String name, Secretary sub) {
            super(name, sub);
        }

        @Override
        public void update() {
            System.out.printf("%s %s 关闭NBA直播，继续工作！\n", sub.getAction(), this.name);
        }
    }
}
