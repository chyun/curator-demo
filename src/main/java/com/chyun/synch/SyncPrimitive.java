package com.chyun.synch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 类的实现描述:
 * Created by Calix on 13/3/17.
 */
public class SyncPrimitive implements Watcher {
    static ZooKeeper zk = null;
    static Integer mutex;
    String root;
    SyncPrimitive(String address, int sessionTime) {
        this.root = "/sync" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt();
        System.out.println("Root is " + root);
        if (zk == null) {
            try {
                System.out.println("Starting ZK:");
                zk = new ZooKeeper(address, sessionTime, this);
                mutex = new Integer(-1);
                System.out.println("Finished starting ZK: " + zk);
            } catch (IOException e) {
                System.out.println(e.toString());
                zk = null;
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("event:" + event);
        try {
            List<String> list = zk.getChildren(root, true);
            if (null != list && list.size() >= 5) {
                synchronized (mutex) {
                    System.out.println("barrier is ok");
                    mutex.notifyAll();
                }
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
