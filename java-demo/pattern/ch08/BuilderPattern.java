package pattern.ch08;

import java.util.ArrayList;
import java.util.List;

public class BuilderPattern {

    public static void main(String[] args) {
        Director director = new Director();
        Builder b1 = new ConcreteBuilder1();
        Builder b2 = new ConcreteBuilder2();

        director.construct(b1); //指挥者类包装建造过程，保证完整
        b1.builder().show();

        b2.builderPartA().builderPartB().builder().show(); // 链式调用，过程暴露，不保证完整但灵活建造
    }

    /**
     * Director-指挥者类
     */
    static class Director {
        public void construct(Builder builder) {
            builder.builderPartA().builderPartB();
        }
    }

    /**
     * 产品类- 有多个部件组成
     */
    static class Product {
        List<String> parts = new ArrayList<String>();

        public void addParts(String part) {
            parts.add(part);
        }

        public void show() {
            System.out.println("产品 创建：--------------------");
            for (String part : parts) {
                System.out.println(part);
            }
        }
    }

    /**
     * 抽象建造者类
     */
    static abstract class Builder {
        abstract Builder builderPartA();

        abstract Builder builderPartB();

        abstract Product builder();
    }

    static class ConcreteBuilder1 extends Builder {
        private Product product = new Product();

        @Override
        Builder builderPartA() {
            product.addParts("部件A");
            return this;
        }

        @Override
        Builder builderPartB() {
            product.addParts("部件B");
            return this;
        }

        @Override
        Product builder() {
            return product;
        }
    }

    static class ConcreteBuilder2 extends Builder {
        private Product product = new Product();

        @Override
        Builder builderPartA() {
            product.addParts("部件X");
            return this;
        }

        @Override
        Builder builderPartB() {
            product.addParts("部件Y");
            return this;
        }

        @Override
        Product builder() {
            return product;
        }

    }
}
