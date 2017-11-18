package org.orh.pattern.ch02;

/**
 * 超市收银 - 工厂模式实现 ：打折、满减； 简单工厂实现  main中需要认识到两个类：CashFactory、CashSuper
 */
public class SuperMarket {
    public static void main(String[] args) {
        double assetPurchaseMoney  = 512;
        System.out.println("购买了" + 512 + "RMB的物品");
        
        // 开业 8折
        CashSuper cs = CashFactory.createCashSuper("打8折");
        System.out.println("打8折：" + cs.acceptCash(assetPurchaseMoney));

        cs = CashFactory.createCashSuper("满300减100");
        System.out.println("满300减100：" + cs.acceptCash(assetPurchaseMoney));
    }
    
    static class CashFactory {
        public static CashSuper createCashSuper(String type) {
            CashSuper cs = null;
            switch (type) {
                case "正常收费":
                    cs = new CashNormal();
                    break;
                case "满300减100":
                    cs = new CashReturn(300, 100);
                    break;
                case "打8折":
                    cs = new CashRebate(0.8);
                    break;
                default:
                    throw new RuntimeException("未知收银策略：" + type);
            }
            return cs;
        }
    }

    static abstract class CashSuper {

        /**
         * 入参为原价，返回 当前价
         */
        public abstract double acceptCash(double money);
    }

    // 正常收费，原价返回
    static class CashNormal extends CashSuper {
        public double acceptCash(double money) {
            return money;
        }
    }

    // 打折
    static class CashRebate extends CashSuper {
        private double discountRate = 1; // 折扣率

        public CashRebate(double discountRate) { // 实例化时要求传入折扣率
            this.discountRate = discountRate;
        }

        public double acceptCash(double money) {
            return money * discountRate;
        }
    }

    // 满减，达到指定金额，减去一定金额
    static class CashReturn extends CashSuper {
        private double moneyTarget; // 目标金额
        private double moneyReturn; // 减去金额

        public CashReturn(double moneyTarget, double moneyReturn) {
            this.moneyTarget = moneyTarget;
            this.moneyReturn = moneyReturn;
        }

        public double acceptCash(double money) {
            return money >= moneyTarget ? (money - moneyReturn) : (money);
        }

    }
}
