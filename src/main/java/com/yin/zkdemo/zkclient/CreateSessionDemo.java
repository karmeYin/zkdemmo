package com.yin.zkdemo.zkclient;

import org.I0Itec.zkclient.ZkClient;

public class CreateSessionDemo {
    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient("localhost:2181",5000);
        System.out.println("Zookeeper session established");
    }
}
