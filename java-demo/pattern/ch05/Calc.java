package pattern.ch05;

/**
 * <pre>
 * 1. 封装、继承、多态 （继承-重写-父指子） 2. 简单工厂模式 产生实例 修改为工厂方法
 * 
 * <pre>
 */
public class Calc {

    public static void main(String[] args) {
        IOperationFactory operFactory = new AddFactory();
        Operation oper = operFactory.createOperation();

        oper.numberA = 2;
        oper.numberB = 2;

        System.out.println(oper.getResult());

        operFactory = new SubFactory();
        oper = operFactory.createOperation();

        oper.numberA = 2;
        oper.numberB = 2;

        System.out.println(oper.getResult());

        // 客户端新增1：乘方工厂实例化
        operFactory = new PowerFactory();
        
        // 得到乘方计算类
        oper = operFactory.createOperation();

        oper.numberA = 2;
        oper.numberB = 3;

        System.out.println(oper.getResult());
    }

    static interface IOperationFactory {
        Operation createOperation();
    }

    static class AddFactory implements IOperationFactory {
        public Operation createOperation() {
            return new OperationAdd();
        }
    }
    static class SubFactory implements IOperationFactory {
        public Operation createOperation() {
            return new OperationSub();
        }
    }
    static class MulFactory implements IOperationFactory {
        public Operation createOperation() {
            return new OperationMul();
        }
    }
    static class DivFactory implements IOperationFactory {
        public Operation createOperation() {
            return new OperationDiv();
        }
    }
    
    // 新增2：生成乘方实例工厂类
    static class PowerFactory implements IOperationFactory {
        public Operation createOperation() {
            return new OperationPower();
        }
    }
    
    // 新增 1: 乘方计算类
    static class OperationPower extends Operation {
        public double getResult() {
            return Math.pow(this.getNumberA(), this.getNumberB());
        }
    }

    // 加法计算
    static class OperationAdd extends Operation {
        public double getResult() {
            return this.getNumberA() + this.getNumberB();
        }
    }

    // 减法计算
    static class OperationSub extends Operation {
        public double getResult() {
            return this.getNumberA() - this.getNumberB();
        }
    }

    // 乘法计算
    static class OperationMul extends Operation {
        public double getResult() {
            return this.getNumberA() * this.getNumberB();
        }
    }

    // 除法计算
    static class OperationDiv extends Operation {
        public double getResult() {
            if (this.getNumberB() == 0) {
                throw new RuntimeException("除数不能为0");
            }
            return this.getNumberA() / this.getNumberB();
        }
    }

    /**
     * 抽象计算类
     */
    static abstract class Operation {
        private double numberA;
        private double numberB;

        public double getNumberA() {
            return numberA;
        }

        public void setNumberA(double numberA) {
            this.numberA = numberA;
        }

        public double getNumberB() {
            return numberB;
        }

        public void setNumberB(double numberB) {
            this.numberB = numberB;
        }

        public abstract double getResult();
    }
}
