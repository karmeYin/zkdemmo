package com.yin.zkdemo.zkapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZookeeperConstructorUsageWithSeidAndPid implements Watcher {
    private static CountDownLatch connectedstatus = new CountDownLatch(1);
    public void process(WatchedEvent event) {
        System.out.println("received watchevent:" + event);
        if(event.getState()== Event.KeeperState.SyncConnected){
            connectedstatus.countDown();
        }
    }

    public static void main(String[] args) {
        try{
            ZooKeeper zookeeper=new ZooKeeper("localhost:2181",5000,new ZookeeperConstructorUsageWithSeidAndPid());
            System.out.println(zookeeper.getState());
            connectedstatus.await();
            System.out.println("Zookeeper Session established");
            Long sid=zookeeper.getSessionId();
            byte[] pid=zookeeper.getSessionPasswd();
              zookeeper=new ZooKeeper("localhost:2181",5000,new ZookeeperConstructorUsageWithSeidAndPid(),sid,"test".getBytes());
              //两个相同的连接会相互断开对方，然后自己连上，一直争抢，应该是这个连接有自带重连
              zookeeper=new ZooKeeper("localhost:2181",5000,new ZookeeperConstructorUsageWithSeidAndPid(),sid,pid);
            Thread.sleep(Integer.MAX_VALUE);

        }catch (Exception e){

        }
    }

}
