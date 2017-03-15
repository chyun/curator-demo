package com.chyun;

/**
 * 类的实现描述:
 * Created by Calix on 14/3/17.
 */
public class Singleton {
    //private int a = 1;
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    private Singleton() {
    }

    public static final Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
