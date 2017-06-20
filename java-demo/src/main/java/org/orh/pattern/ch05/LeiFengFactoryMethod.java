package org.orh.pattern.ch05;

public class LeiFengFactoryMethod {
    public static void main(String[] args) {
        ILeiFengFactory factory = new UndergraduateFactory();
        LeiFeng student =  factory.createLeiFeng();
        student.buyRice();
        student.sweep();
        student.wash();
    }

    static class VolunteerFactory implements ILeiFengFactory {
        public LeiFeng createLeiFeng() {
            return new Volunteer();
        }
    }

    static class UndergraduateFactory implements ILeiFengFactory {
        public LeiFeng createLeiFeng() {
            return new Undergraduate();
        }
    }

    static interface ILeiFengFactory {
        public LeiFeng createLeiFeng();
    }

    static abstract class LeiFeng {
        public void sweep() {
            System.out.println("扫地");
        }

        public void wash() {
            System.out.println("洗衣");
        }

        public void buyRice() {
            System.out.println("买米");
        }
    }
    static class Undergraduate extends LeiFeng {
    }
    static class Volunteer extends LeiFeng {
    }
}
