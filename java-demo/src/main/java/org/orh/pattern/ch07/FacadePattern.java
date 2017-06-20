package org.orh.pattern.ch07;

public class FacadePattern {
    
    public static void main(String[] args) {
        FundFacade fund = new FundFacade();
        fund.buyFund();
        fund.sellFund();
    }

    static class FundFacade {
        public void buyFund() {
            Stock600361.buy();
            Stock600362.buy();
            Stock600363.buy();
        }

        public void sellFund() {
            Stock600361.sell();
            Stock600362.sell();
            Stock600363.sell();
        }
    }

    static class Stock600361 {
        public static void buy() {
            System.out.println("买600361");
        }

        public static void sell() {
            System.out.println("卖600361");
        }
    }
    static class Stock600362 {
        public static void buy() {
            System.out.println("买600362");
        }

        public static void sell() {
            System.out.println("卖600362");
        }
    }
    static class Stock600363 {
        public static void buy() {
            System.out.println("买600363");
        }

        public static void sell() {
            System.out.println("卖600363");
        }
    }
}
