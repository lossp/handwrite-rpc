package nettyrpc.registry;

import nettyrpc.constant.Constant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * This class is for service register
 */
public class ServiceRegistry {
    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<>();
    private String address;
    private ZooKeeper zooKeeper;

    public ServiceRegistry(String address) {
        this.address = address;
        zooKeeper = connectRegister();
    }

    public ZooKeeper connectRegister() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(address, Constant.getZOOKEEPER_SESSION_TIMEOUT(), new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    // 主要用于处理连接事件
                    // The client is in the connected state - it is connected to a server in the ensemble (one of the servers specified in the host connection parameter during ZooKeeper client creation).
                    if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }

    public void stop() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


//    private void watchNode(ZooKeeper zooKeeper) {
//        try {
//            List<String> nodeList = zooKeeper.getChildren(Constant.getZookeeperRegistryPath(), new Watcher() {
//                @Override
//                public void process(WatchedEvent watchedEvent) {
//                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
//                        watchedEvent(zooKeeper);
//                    }
//                }
//            })
//        }
//    }
}
