package pattern.ch04;

public class PursuitGirl {
    
    public static void main(String[] args) {
        SchoolGirl jiaojiao = new SchoolGirl();
        jiaojiao.setName("娇娇");
        
        Pursuit zhuojiayi = new Pursuit(jiaojiao); // 卓贾易 与娇娇并不认识，此处有问题
        zhuojiayi.giveDolls();
        zhuojiayi.giveFlowers();
        zhuojiayi.giveChocolate();
    }

    // 追求者类
    static class Pursuit {
        SchoolGirl mm;

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
