package com.chyun.synch;

import org.apache.zookeeper.KeeperException;

import java.util.concurrent.TimeUnit;

/**
 * 类的实现描述:
 * Created by Calix on 13/3/17.
 */
class Compitator implements Runnable {
    Barrier barrier;
    int i;
    Compitator(Barrier barrier, int i) {
        this.barrier = barrier;
        this.i = i;
    }
    @Override
    public void run() {
        //System.out.println("Barrier " + i + " enter");
        try {
            barrier.enter();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Barrier " + i + " running");
    }
}
public class BarrierTest {
    public static void main(String[] args) {
        Barrier barrier = new Barrier("localhost", 200000);
        Thread[] threads = new Thread[6];
        for (int i = 0; i < 6; i++) {
            Thread t = new Thread(new Compitator(barrier, i));
            threads[i] = t;
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t.start();
        }
        for (int i = 0; i < 6; i++) {
            Thread t = threads[i];
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
