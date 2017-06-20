package org.orh.pattern.ch03;

public class Avata {
    public static void main(String[] args) {
        Person xc = new Person("小菜");

        System.out.println("第一种装扮");
        Finery dtx = new TShirts();
        Finery kk = new BigTrouser();
        Finery pqx = new Sneakers();
        dtx.show();
        kk.show();
        pqx.show();
        xc.show();

        System.out.println("\n第二种装扮");
        Finery xz = new Suit();
        Finery ld = new Tie();
        Finery px = new LeatherShoes();
        xz.show();
        ld.show();
        px.show();
        xc.show();
    }

    static abstract class Finery {
        public abstract void show();
    }
    static class TShirts extends Finery {
        public void show() {
            System.out.print("大T恤 ");
        }
    }

    static class BigTrouser extends Finery {
        public void show() {
            System.out.print("大垮裤 ");
        }
    }

    static class Sneakers extends Finery {
        public void show() {
            System.out.print("破球鞋 ");
        }
    }

    static class Suit extends Finery {
        public void show() {
            System.out.print("西装 ");
        }
    }

    static class Tie extends Finery {
        public void show() {
            System.out.print("领带 ");
        }
    }

    static class LeatherShoes extends Finery {
        public void show() {
            System.out.print("皮鞋 ");
        }
    }

    static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public void show() {
            System.out.print("装扮的" + this.name);
        }
    }

}
