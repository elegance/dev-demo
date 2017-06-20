package org.orh.pattern.ch02;

/**
 * 超市收银 - 策略模式实现 ：打折、满减; 策略与简单工厂结合 main中只需要认识CashContext类； 耦合更加降低
 */
public class SuperMarketStrategy {
    public static void main(String[] args) {
        double assetPurchaseMoney = 512;
        System.out.println("购买了" + 512 + "RMB的物品");
        
        CashContext csuper = new CashContext("打8折");

        // 开业 8折
        System.out.println("打8折：" + csuper.getResult(assetPurchaseMoney));


        csuper = new CashContext("满300减100");
        System.out.println("满300减100：" + csuper.getResult(assetPurchaseMoney));
    }
    

    static class CashContext {
        public CashContext(String type) {
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
        }

        private CashSuper cs;

        public double getResult(double money) {
            return cs.acceptCash(money);
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
