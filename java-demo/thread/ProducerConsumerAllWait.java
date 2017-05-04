public class ProducerConsumerAllWait {

    public static void main(String[] args) throws InterruptedException {
        String lock = new String("");
        Producer p = new Producer(lock);
        Consumer c = new Consumer(lock);

        Thread[] pThreads = new Thread[2];
        Thread[] cThreads = new Thread[2];

        for (int i = 0; i < 2; i++) {
            pThreads[i] = new Thread(() -> {
                while (true) {
                    p.setValue();
                }
            }, "生产者 " + (i + 1));

            cThreads[i] = new Thread(() -> {
                while (true) {
                    c.getValue();
                }
            }, "消费者 " + (i + 1));

            pThreads[i].start();
            cThreads[i].start();
        }

        Thread.sleep(5000);
        Thread[] threadArr = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
        Thread.currentThread().getThreadGroup().enumerate(threadArr);
        for (int i = 0; i < threadArr.length; i++) {
            System.out.println(threadArr[i].getName() + " " + threadArr[i].getState());
        }
        
        // 控制台输出：
        // 1.  生产者 生产者 1 RUNNABLE 了 ==> p1.start()，得到锁 ==> 无值 ，生产 ，notify(e:消费者)(通知过早)
        // 2.  生产者 生产者 1 WATING 了 ★ ==> while ==> 已经设值，wait，释放锁
        // 3.  生产者 生产者 2 WATING 了 ★ ==> p2 得到锁，已经设值，wait，释放锁
        // 4.  消费者 消费者 1 RUNNABLE 了 ==> c1 得到锁，有值，消费，notify (e:生产者)
        // 5.  消费者 消费者 1 WAITING 了☆ ==> while ==> 无值可用，wait，释放锁
        // 6.  生产者 生产者 1 RUNNABLE 了 ==> p1 得到锁，无值，生产值，notify (e:消费者)
        // 7.  生产者 生产者 1 WATING 了 ★ ==> while ==> 值未被消费，wait,释放锁
        // 8.  生产者 生产者 2 WATING 了 ★ ==> unexpected: p2 wait的线程被唤醒，值还未被消费，wait，释放锁
        // 9.  消费者 消费者 2 RUNNABLE 了 ==> c2 得到锁，有值，消费，notify (e:生产者)
        // 10. 消费者 消费者 2 WAITING 了☆ ==> while ==> 无值，wait，释放锁
        // 11. 消费者 消费者 1 WAITING 了☆ ==> unexpected: 无值，wait,释放锁

        // 所有都进入wait的原因：
        // 生产者 通知唤醒了另一个生产者， 发生在：第6行生产在的notify，结果 唤醒了第8行的生产者
        // 消费者 通知唤醒了 另一个消费者，发生在：第9行消费者的notify，结果 唤醒了第11行的消费者

        // 知识点有以下：
        // notify 唤醒一个 “wait 队列”中的一个线程，队列中poll出一个线程唤醒
        // notify 方法不会释放锁，等待同步代码块结束
        // 唤醒的线程不一定马上就能RUNNABLE，而是进入 “竞争 队列”,竞争取的锁才会进入同步块执行
        // 睡觉的线程不参与竞争，要区分两种阻塞：因为同步块的内部锁 竞争失败的阻塞、条件不满足进入 wait队列的阻塞-------------- wait队列、竞争队列
    }

    static class Producer {
        private String lock;

        public Producer(String lock) {
            this.lock = lock;
        }

        public void setValue() {
            try {
                String threadName = Thread.currentThread().getName();
                synchronized (lock) {
                    while (!ValueObject.value.equals("")) {
                        System.out.println("生产者 " + threadName + " WATING 了 ★");
                        lock.wait();
                    }
                    System.out.println("生产者 " + threadName + " RUNNABLE 了 ");
                    String value = System.currentTimeMillis() + "_" + System.nanoTime();
                    ValueObject.value = value;
                    lock.notify(); // 注意此处 发出的notify信号，不一定会被“消费者”所获取，也有可能被另外一个“生产者”所获取
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    static class ValueObject {
        static String value = "";
    }

    static class Consumer {
        private String lock;

        public Consumer(String lock) {
            this.lock = lock;
        }

        public void getValue() {
            try {
                String threadName = Thread.currentThread().getName();
                synchronized (lock) {
                    while (ValueObject.value.equals("")) {
                        System.out.println("消费者 " + threadName + " WAITING 了☆");
                        lock.wait();
                    }
                    System.out.println("消费者 " + threadName + " RUNNABLE 了");
                    ValueObject.value = "";
                    lock.notify();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
