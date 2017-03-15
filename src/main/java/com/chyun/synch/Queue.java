package com.chyun.synch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 类的实现描述:
 * Created by Calix on 13/3/17.
 */
public class Queue extends SyncPrimitive {
    Queue(String address, int sessionTime) {
        super(address, sessionTime);
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
    }

    /**
     * Add element to the queue.
     *
     * @param i
     * @return
     */
    boolean produce(int i) throws KeeperException, InterruptedException {
        ByteBuffer b = ByteBuffer.allocate(4);
        byte[] value;
        // Add child with value i
        b.putInt(i);
        value = b.array();
        zk.create(root + "/element", value, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        List<String> list = zk.getChildren(root, true);
        if (list.size() == 0) {
            System.out.println("Going to wait");
            mutex.wait();
        } else {
            for (String s : list) {
                System.out.println("Node: " + s);
            }
        }
        return true;
    }

    int consume() throws KeeperException, InterruptedException {
        int retvalue = -1;
        Stat stat = null;
        // Get the first element available
        while (true) {
            synchronized (mutex) {
                List<String> list = zk.getChildren(root, true);
                if (list.size() == 0) {
                    System.out.println("Going to wait");
                    mutex.wait();
                } else {
                    for (String s : list) {
                        System.out.println("Node: " + s);
                    }

                    Integer min = new Integer(list.get(0).substring(7));
                    for (String s : list) {
                        Integer tempValue = new Integer(s.substring(7));
                        //System.out.println("Temporary value: " + tempValue);
                        if (tempValue < min) min = tempValue;
                    }
                    System.out.println("Temporary value: " + root + "/element000000000" + min);
                    byte[] b = zk.getData(root + "/element000000000" + min, false, stat);
                    zk.delete(root + "/element000000000" + min, 0);
                    ByteBuffer buffer = ByteBuffer.wrap(b);
                    retvalue = buffer.getInt();
                    return retvalue;
                }
            }
        }
    }
}
