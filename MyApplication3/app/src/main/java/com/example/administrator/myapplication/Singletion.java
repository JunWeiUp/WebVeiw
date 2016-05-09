package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2016/4/29.
 */
public class Singletion {

    private static Singletion instance;

    private Singletion() {}

    public static synchronized Singletion getInstance() {
        if (instance == null) {
            instance = new Singletion();
        }
        return instance;
    }
}
