package com.chyun.synch;

import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
import org.apache.zookeeper.KeeperException;

/**
 * 类的实现描述:
 * Created by Calix on 13/3/17.
 */
public class QueueTest {
    public static void main(String[] args) {
        Queue q = new Queue("localhost", 2000);

        //System.out.println("Input: " + args[1]);
        int i;
        Integer max = new Integer(10);
        System.out.println("Producer");
        for (i = 0; i < max; i++) {
            try {
                q.produce(10 + i);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("Consumer");
//
//        for (i = 0; i < max; i++) {
//            try{
//                int r = q.consume();
//                System.out.println("Item: " + r);
//            } catch (KeeperException e){
//                i--;
//            } catch (InterruptedException e){
//
//            }
//        }

    }
}
