package org.orh.pattern.ch09;

import java.util.ArrayList;
import java.util.List;

public class ObserverPatterV1 {
    public static void main(String[] args) {
        // 前台-童子喆
        Secretary tongzize = new Secretary();

        // 看股的两位同事
        StockObserver tongshi1 = new StockObserver("魏关姹", tongzize);
        StockObserver tongshi2 = new StockObserver("易管查", tongzize);

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
        private List<StockObserver> observers = new ArrayList<StockObserver>();

        // 前台通过电话，所说的话或所做的事
        private String action;

        public void attach(StockObserver observer) {
            observers.add(observer);
        }

        public void notifyAllObservers() {
            for (StockObserver stockObserver : observers) {
                stockObserver.update();
            }
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
    static class StockObserver {
        private String name;
        private Secretary sub;

        public StockObserver(String name, Secretary sub) {
            this.name = name;
            this.sub = sub;
        }

        public void update() {
            System.out.printf("%s %s 关闭股票行情，继续工作！\n", sub.getAction(), this.name);
        }
    }
}
