package com.nowcoder.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTests {
    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue(10); // 队列容量为10
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

/**
 * 生产者线程 因为是一个线程，所以要实现Runnable接口
 */
class Producer implements Runnable {

    // 把阻塞队列传进来
    private BlockingQueue<Integer> queue;

    // 有参构造器
    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for(int i = 0; i< 100; i++) {
                Thread.sleep(20);
                queue.put(i);
                System.out.println(Thread.currentThread().getName() + "生产：" + queue.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 消费者线程
 */
class Consumer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(new Random().nextInt(1000));
                queue.take();
                System.out.println(Thread.currentThread().getName() + "消费：" + queue.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
