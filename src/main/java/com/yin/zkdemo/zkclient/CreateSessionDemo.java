package com.yin.zkdemo.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class CreateSessionDemo {
    private static String path="/zkclient_demo";
    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient=new ZkClient("localhost:2181",5000);
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(s + ":parentpath changedchildren: " + list);
            }
        });
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(s + " has been changed,new value:" + o.toString());
            }

            public void handleDataDeleted(String s) throws Exception {
                System.out.println(s + " has been deleted");
            }
        });
        System.out.println("Zookeeper session established");
        zkClient.deleteRecursive(path);
        zkClient.createPersistent(path,"hahahahahahah");
        Thread.sleep(1000);
        System.out.println(zkClient.getChildren(path));
        Thread.sleep(1000);
        zkClient.createPersistent(path+"/haha");
        zkClient.writeData(path,"修改修改");
        Thread.sleep(1000);
        zkClient.delete(path+"/haha");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);


    }
}
