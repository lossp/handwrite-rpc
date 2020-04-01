package nettyrpc.registry;

import nettyrpc.constant.Constant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;


/**
 * This class is for service register
 */
public class ServiceRegistry {
    private CountDownLatch latch = new CountDownLatch(1);

    private String address;
    private ZooKeeper zooKeeper;

    public ServiceRegistry(String address) {
        this.address = address;
        zooKeeper = connectRegister();
        System.out.println("Entering here");
    }
    public void register(String data) {
        if (data != null) {
            zooKeeper = connectRegister();
            if (zooKeeper != null) {
                addRootNode(zooKeeper);
                createNode(zooKeeper, data);
            }
        }
    }

    private ZooKeeper connectRegister() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(address, Constant.getZOOKEEPER_SESSION_TIMEOUT(), new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    // 主要用于处理连接事件
                    // The client is in the connected state - it is connected to a server in the ensemble (one of the servers specified in the host connection parameter during ZooKeeper client creation).
                    System.out.println("Received watch event: " + watchedEvent);
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        System.out.println("zookeeper status is " + Event.KeeperState.SyncConnected);
                        // 这里调用latchCountDown()，主要是等待函数内部处理完毕之后，将latch减到0，从而整个流程不会在latch.await()阻塞，否则会在latch.await()阻塞
                        latch.countDown();
                    }
                    if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
                        System.out.println("zookeeper now is disconnected");
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }

    private void addRootNode(ZooKeeper zooKeeper) {
        try {
            Stat status = zooKeeper.exists(Constant.getZookeeperRegistryPath(), false);
            if (status == null) {
                System.out.println("Adding the root node now...");
                zooKeeper.create(Constant.getZookeeperRegistryPath(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createNode(ZooKeeper zooKeeper, String info) {
        try {
            byte[] infoBytes = info.getBytes();
            String path = zooKeeper.create(Constant.getZookeeperDataPath(), infoBytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("Node is being created, just a moment please");
            System.out.println("the data = " + info);
            System.out.println("the path = " + path);
            System.out.println("the basic path = " + Constant.getZookeeperDataPath());
            System.out.println("caoo ".getBytes());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
