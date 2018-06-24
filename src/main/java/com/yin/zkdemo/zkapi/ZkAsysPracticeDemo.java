package com.yin.zkdemo.zkapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZkAsysPracticeDemo implements Watcher {
    private static CountDownLatch connectedstatus = new CountDownLatch(1);
    private static String path="/zk_practice_asys";
    private static ZooKeeper zookeeper=null;
    public void process(WatchedEvent event) {
       // System.out.println("received watchevent:" + event);
        if(event.getType()== Event.EventType.None && null==event.getPath()){
            connectedstatus.countDown();
        }else if(Event.EventType.NodeChildrenChanged == event.getType()){
            try {
                 zookeeper.getChildren(event.getPath(), true,new Ichildren2CallBack(),"context");
                zookeeper.getData(event.getPath(), true,new IDataCallBack(),"context");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }else if(Event.EventType.NodeDataChanged == event.getType()){
            zookeeper.getData(event.getPath(), true,new IDataCallBack(),"context");
        }
    }

    public static void main(String[] args) {
        try{
             zookeeper=new ZooKeeper("localhost:2181",5000,new ZkAsysPracticeDemo());
            System.out.println(zookeeper.getState());
            connectedstatus.await();
            System.out.println("Zookeeper Session established");
//            zookeeper.delete(path+"/p1",-1);
//            zookeeper.delete(path+"/p2",-1);
//            zookeeper.delete(path,-1);
            zookeeper.create(path,"test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,new IStringCallBack(),"i am context");
            zookeeper.create(path+"/p1","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallBack(),"i am context");
          zookeeper.getChildren(path,true,new Ichildren2CallBack(),"context");
            zookeeper.getData(path, true,new IDataCallBack(),"context");
          zookeeper.create(path+"/p2","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallBack(),"i am context");
            zookeeper.setData(path,"修改测试".getBytes(),-1,new IStateCallBack(),null);
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
class Ichildren2CallBack implements AsyncCallback.Children2Callback{

    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("Children:"+children);
    }
}
class IDataCallBack implements AsyncCallback.DataCallback{

    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println("data:"+new String(data));
    }
}
class IStateCallBack implements AsyncCallback.StatCallback{

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if(0==rc){
            System.out.println("修改成功");
        }
    }
}
