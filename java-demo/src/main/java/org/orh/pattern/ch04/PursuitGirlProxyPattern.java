package org.orh.pattern.ch04;

public class PursuitGirlProxyPattern {
    
    public static void main(String[] args) {
        SchoolGirl jiaojiao = new SchoolGirl();
        jiaojiao.setName("娇娇");
        
        Proxy daili = new Proxy(jiaojiao);
        daili.giveDolls();
        daili.giveFlowers();
        daili.giveChocolate();
    }

    // 代理接口
    static interface GiveGift {
        void giveDolls();

        void giveFlowers();

        void giveChocolate();
    }

    // 追求者类
    static class Pursuit implements GiveGift {
        private SchoolGirl mm;

        public Pursuit(SchoolGirl mm) {
            this.mm = mm;
        }

        public void giveDolls() {
            System.out.println(mm.getName() + " 送你洋娃娃");
        }

        public void giveFlowers() {
            System.out.println(mm.getName() + " 送你鲜花");
        }

        public void giveChocolate() {
            System.out.println(mm.getName() + " 送你巧克力");
        }
    }

    // 代理类
    static class Proxy implements GiveGift {
        Pursuit gg;

        public Proxy(SchoolGirl mm) {
            gg = new Pursuit(mm);
        }

        public void giveDolls() {
            gg.giveDolls();
        }

        public void giveFlowers() {
            gg.giveFlowers();
        }

        public void giveChocolate() {
            gg.giveChocolate();
        }
    }

    static class SchoolGirl {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
