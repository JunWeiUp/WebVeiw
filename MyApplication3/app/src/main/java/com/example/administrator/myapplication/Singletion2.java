package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2016/4/29.
 */
public class Singletion2 {

    private static Singletion2 instance;

    private Singletion2() {
    }

    public static  Singletion2 getInstance() {
        if (instance == null) {
            instance = new Singletion2();
        }
        return instance;
    }
}
