package nettyrpc.registry;

import nettyrpc.constant.Constant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ServiceDiscovery {
    private CountDownLatch latch = new CountDownLatch(1);
    private String address;
    private ZooKeeper zooKeeper;
    private volatile List<String> dataList = new ArrayList<>();

    public ServiceDiscovery(String address) {
        this.address = address;
        zooKeeper = connectRegister();
        if (zooKeeper != null) {
            watchNode(zooKeeper);
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

    private void watchNode(ZooKeeper zooKeeper) {
        System.out.println("watch node method is entered");
        try {
            List<String> nodeList = zooKeeper.getChildren(Constant.getZookeeperRegistryPath(), new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    // 子节点变更时候的监听
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                        System.out.println("Node children has changed, run the watchedEvent once again");
                        watchNode(zooKeeper);
                    }
                }
            });
            List<String> dataList = new ArrayList<>();
            // 获取每个节点下的信息
            for (String node:nodeList) {
                byte[] bytes = zooKeeper.getData(Constant.getZookeeperRegistryPath() + "/" + node, false, null);
                bytes = (bytes == null ? new byte[0]: bytes);
                dataList.add(new String(bytes));
            }
            this.dataList = dataList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
