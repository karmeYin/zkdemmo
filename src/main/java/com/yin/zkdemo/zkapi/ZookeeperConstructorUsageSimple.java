package com.yin.zkdemo.zkapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZookeeperConstructorUsageSimple implements Watcher {
    private static CountDownLatch connectedstatus = new CountDownLatch(1);
    public void process(WatchedEvent event) {
        System.out.println("received watchevent:" + event);
        if(event.getState()== Event.KeeperState.SyncConnected){
            connectedstatus.countDown();
        }
    }

    public static void main(String[] args) {
        try{
            ZooKeeper zookeeper=new ZooKeeper("localhost:2181",5000,new ZookeeperConstructorUsageSimple());
            System.out.println(zookeeper.getState());
            connectedstatus.await();
            System.out.println("Zookeeper Session established");
        }catch (Exception e){

        }
    }
}
