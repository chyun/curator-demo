package com.chyun.synch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 类的实现描述:
 * Created by Calix on 13/3/17.
 */
public class Barrier extends SyncPrimitive {

    private int size;
    private String name;
    public Barrier(String address, int sessionTime, int size) {
        super(address, sessionTime);
        this.size = size;
    }

    public Barrier(String address, int sessionTime) {
        super(address, sessionTime);
        this.size = 5;
        if (zk != null) {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) {
                System.out
                        .println("Keeper exception when instantiating queue: "
                                + e.toString());
            } catch (InterruptedException e) {
                System.out.println("Interrupted exception");
            }
        }
        // My node name
        try {
            name = new String(InetAddress.getLocalHost().getCanonicalHostName().toString());
        } catch (UnknownHostException e) {
            System.out.println(e.toString());
        }
    }

    boolean enter() throws KeeperException, InterruptedException {
        zk.create(root + "/" + name, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        while (true) {
            synchronized (mutex) {
                List<String> list = zk.getChildren(root, true);
                System.out.println("list size is " + list.size());
                if (list.size() < size) {
                    System.out.println("Thread " + Thread.currentThread().getName() + " is going to wait");
                    mutex.wait();
                    System.out.println("Wake up");
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * Wait until all reach barrier
     *
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    boolean leave() throws KeeperException, InterruptedException {
        zk.delete(root + "/" + name, 0);
        while (true) {
            synchronized (mutex) {
                List<String> list = zk.getChildren(root, true);
                if (list.size() > 0) {
                    mutex.wait();
                } else {
                    return true;
                }
            }
        }
    }

    boolean clear() throws KeeperException, InterruptedException {
        //zk.delete(root, );
        return true;
    }
}
