package org.orh.thread;
/**
 * 一生产者与一消费者：操作值
 */
public class ProducerConsumerTest {

    public static void main(String[] args) {
        String lock = new String("");
        Producer producer = new Producer(lock);
        Consumer consumer = new Consumer(lock);

        Thread pThread = new Thread(() -> {
            while (true) {
                producer.setValue();
            }
        }, "p-thread");

        Thread cThread = new Thread(() -> {
            while (true) {
                consumer.getValue();
            }
        }, "c-thread");
        pThread.start();
        cThread.start();
    }

    static class Producer {
        private String lock;

        public Producer(String lock) {
            this.lock = lock;
        }

        public void setValue() {
            try {
                synchronized (lock) {
                    if (!ValueObject.value.equals("")) {
                        lock.wait(); // 等待：有值未被消费，当值被消费时， 消费者应该 notify 我
                    }
                    String value = System.currentTimeMillis() + "_" + System.nanoTime();
                    System.out.println("set 的值是：" + value);
                    ValueObject.value = value;
                    lock.notify(); // 通知：设置了新的值，通知消费者消费
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class ValueObject {
        public static String value = "";
    }

    static class Consumer {

        private String lock;

        public Consumer(String lock) {
            this.lock = lock;
        }

        public void getValue() {
            try {
                synchronized (lock) {
                    if (ValueObject.value.equals("")) {
                        lock.wait(); // 等待：没有值可消费了，等生产者设置了新的值应该notify 我
                    }
                    System.out.println("get 的值是：" + ValueObject.value);
                    ValueObject.value = "";
                    lock.notify(); // 通知：消费完值了，通知生产者生产
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
