package org.orh.pattern.ch03;

public class DecoratorPattern {
    public static void main(String[] args) {
        ConcreteComponent component = new ConcreteComponent();
        ConcreteDecoratorA d1 = new ConcreteDecoratorA();
        ConcreteDecoratorB d2 = new ConcreteDecoratorB();
        
        d1.setComponent(component);
        d2.setComponent(d1);
        d2.operation();
    }

    static abstract class Component {
        public abstract void operation();
    }

    static class ConcreteComponent extends Component {

        @Override
        public void operation() {
            System.out.println("Concrete Component!");
        }
    }

    static class Decorator extends Component {
        protected Component component;

        public void setComponent(Component component) {
            this.component = component;
        }

        @Override
        public void operation() {
            if (component != null) {
                component.operation();
            }
        }
    }

    static class ConcreteDecoratorA extends Decorator {
        private String addedState; // 本类独有功能

        @Override
        public void operation() {
            super.operation(); // 执行原Component Operation , 再执行本类的功能，如addedState ，相当于对原
                               // Component进行了装饰
            addedState = "New State";
            System.out.println("具体装饰对象A的操作" + this.addedState);
        }
    }

    static class ConcreteDecoratorB extends Decorator {
        @Override
        public void operation() {
            super.operation();
            this.addedBehavior(); 
            System.out.println("具体装饰对象B的操作。");
        }

        public void addedBehavior() { // 本类独有方法
//            System.out.println("addedBehavior");
        }
    }
}
