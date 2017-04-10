package pattern.ch03;

public class AvataDecoratorPattern {

    public static void main(String[] args) {
        Person  xc = new Person("小菜");
        System.out.println("第一种装扮：");
        
        SneakersDecorator qx = new SneakersDecorator();
        BigTrouserDecorator kk = new BigTrouserDecorator();
        TshirtsDecorator tx = new TshirtsDecorator();
        qx.decorate(xc);
        kk.decorate(qx);
        tx.decorate(kk);
        tx.show();
        
        System.out.println("\n第二种装扮：");
        LeatherShoesDecorator px = new LeatherShoesDecorator();
        TieDecorator ld = new TieDecorator();
        SuitDecorator xz = new SuitDecorator();
        
        px.decorate(xc);
        ld.decorate(px);
        xz.decorate(ld);
        xz.show();
    }
    
    static class Person {
        private String name;

        public Person() {}

        public Person(String name) {
            this.name = name;
        }

        public void show() {
            System.out.printf("装扮的%s", this.name);
        }
    }

    static class FineryDecorator extends Person {
        protected Person personComponent;

        public void decorate(Person personComponent) {
            this.personComponent = personComponent;
        }

        @Override
        public void show() {
            if (personComponent != null) {
                personComponent.show();
            }
        }
    }

    static class TshirtsDecorator extends FineryDecorator {
        @Override
        public void show() {
            System.out.print("大T恤 ");
            super.show();
        }
    }

    static class BigTrouserDecorator extends FineryDecorator {
        @Override
        public void show() {
            System.out.print("大垮裤 ");
            super.show();
        }
    }
    
    static class SneakersDecorator extends FineryDecorator {
        @Override
        public void show() {
            System.out.print("破球鞋 ");
            super.show();
        }
    }
    
    static class SuitDecorator extends FineryDecorator {
        @Override
        public void show() {
            System.out.print("西装 ");
            super.show();
        }
    }
    
    static class TieDecorator extends FineryDecorator {
        @Override
        public void show() {
            System.out.print("领带 ");
            super.show();
        }
    }
    
    static class LeatherShoesDecorator extends FineryDecorator {
        @Override
        public void show() {
            System.out.print("皮鞋 ");
            super.show();
        }
    }
}
