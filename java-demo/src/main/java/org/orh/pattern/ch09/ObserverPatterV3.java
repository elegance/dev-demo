package org.orh.pattern.ch09;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决V2 中 在具体的观察者中依赖具体的前台类
 */
public class ObserverPatterV3 {
    public static void main(String[] args) {
        Boss huhansan = new Boss();

        // 看股的两位同事
        Observer tongshi1 = new StockObserver("魏关姹", huhansan);
        Observer tongshi2 = new NBAObserver("易管查", huhansan);


        // 前台记录下两位同事
        huhansan.attach(tongshi1);
        huhansan.attach(tongshi2);
        
        huhansan.detach(tongshi1); // 魏关姹其实没有被老板通知到

        // 发现boss回来
        huhansan.setAction("我胡汉三回来了！");

        // 通知记录的同事
        huhansan.notifyAllObservers();
    }

    static interface Subject {
        void attach(Observer observer);

        void detach(Observer observer);

        void notifyAllObservers();

        String getAction();
    }

    static class Boss implements Subject {
        private List<Observer> observers = new ArrayList<Observer>();
        private String action;

        @Override
        public void attach(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void detach(Observer observer) {
            observers.remove(observer);
        }

        @Override
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

    static class Secretary implements Subject {
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
        protected Subject sub;

        public Observer(String name, Subject sub) {
            this.name = name;
            this.sub = sub;
        }

        public abstract void update();
    }

    /**
     * 看股票的同事
     */
    static class StockObserver extends Observer {

        public StockObserver(String name, Subject sub) {
            super(name, sub);
        }

        public void update() {
            System.out.printf("%s %s 关闭股票行情，继续工作！\n", sub.getAction(), this.name);
        }
    }

    static class NBAObserver extends Observer {

        public NBAObserver(String name, Subject sub) {
            super(name, sub);
        }

        @Override
        public void update() {
            System.out.printf("%s %s 关闭NBA直播，继续工作！\n", sub.getAction(), this.name);
        }
    }
}
