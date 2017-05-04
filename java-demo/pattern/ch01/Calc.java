package pattern.ch01;

/**
 * <pre>
 * 1. 封装、继承、多态 （继承-重写-父指子）
 * 2. 简单工厂模式 产生实例
 * <pre>
 */
public class Calc {
    
    public static void main(String[] args) {
        // 多态：同样的请求不同的响应
        // 父类 (operation)引用指向子类
        
        Operation oper = OperationFactory.getOperate("+");
        oper.numberA = 2;
        oper.numberB = 2;

        System.out.println(oper.getResult());

        oper = OperationFactory.getOperate("-");
        oper.numberA = 2;
        oper.numberB = 2;
        System.out.println(oper.getResult());

        oper = OperationFactory.getOperate("*");
        oper.numberA = 2;
        oper.numberB = 2;
        System.out.println(oper.getResult());
        
        oper = OperationFactory.getOperate("/");
        oper.numberA = 2;
        oper.numberB = 2;
        System.out.println(oper.getResult());

        oper = OperationFactory.getOperate("^");
        oper.numberA = 2;
        oper.numberB = 3;
        System.out.println(oper.getResult());
    }
    
    /**
     * 工厂模式来 实例化对象
     */
    static class OperationFactory {
        public static Operation getOperate(String operate) {
            Operation oper = null;
            switch (operate) {
                case "+":
                    oper = new OperationAdd();
                    break;
                case "-":
                    oper = new OperationSub();
                    break;
                case "*":
                    oper = new OperationMul();
                    break;
                case "/":
                    oper = new OperationDiv();
                    break;
                // 新增2: 分支判断
                case "^":
                    oper = new OperationPower();
                    break;
                default:
                    throw new RuntimeException("未知操作符：" + operate);
            }
            return oper;
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
