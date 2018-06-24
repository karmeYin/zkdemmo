package com.yin.zkdemo.zkapi;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class ZkAsysPracticeDemo implements Watcher {
    private static CountDownLatch connectedstatus = new CountDownLatch(1);
    private static String path="/zk_practice_asys";
    public void process(WatchedEvent event) {
        System.out.println("received watchevent:" + event);
        if(event.getState()== Event.KeeperState.SyncConnected){
            connectedstatus.countDown();
        }
    }

    public static void main(String[] args) {
        try{
            ZooKeeper zookeeper=new ZooKeeper("localhost:2181",5000,new ZkAsysPracticeDemo());
            System.out.println(zookeeper.getState());
            connectedstatus.await();
            System.out.println("Zookeeper Session established");
          zookeeper.create(path+"p1","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallBack(),"i am context");

          zookeeper.create("/zk_test_create_node_asys","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallBack(),"i am context");
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
class IStringCallBack implements AsyncCallback.StringCallback{

    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("create path result:rc:" + rc + " path:" + path + " name:" + name+" context:");
    }
}
