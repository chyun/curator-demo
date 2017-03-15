package com.chyun.lock;

/**
 * 类的实现描述:
 * Created by Calix on 15/3/17.
 */
public class LockNode {
    private String id;

    public LockNode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
