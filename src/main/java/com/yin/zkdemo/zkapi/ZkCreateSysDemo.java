package com.yin.zkdemo.zkapi;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZkCreateSysDemo implements Watcher {
    private static CountDownLatch connectedstatus = new CountDownLatch(1);
    private static String path="/zk_practice_sys";
    private static ZooKeeper zookeeper=null;
    public void process(WatchedEvent event) {
        System.out.println("received watchevent:" + event);
        if(event.getState()== Event.KeeperState.SyncConnected){
            if(event.getType()== Event.EventType.None && null==event.getPath()){
                connectedstatus.countDown();
            }else if(Event.EventType.NodeChildrenChanged == event.getType()){
                try {
                    System.out.println("regetChild:" + zookeeper.getChildren(event.getPath(), true));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }


        }
    }

    public static void main(String[] args) {
        try{
             zookeeper=new ZooKeeper("localhost:2181",5000,new ZkCreateSysDemo());
            System.out.println(zookeeper.getState());
            connectedstatus.await();
            System.out.println("Zookeeper Session established");
            zookeeper.delete(path,-1);
            zookeeper.create(path,"test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            String path1=zookeeper.create(path+"/p1","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            List<String> childrenList = zookeeper.getChildren(path,true);
            System.out.println(childrenList);
            String path2=zookeeper.create(path+"/p2","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(path2);
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
