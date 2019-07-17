package cn.dyx.protal.frame.util;

/**
 * Description：Redis部署模式
 *
 * @author dyx
 * @date 2019/3/5 16:52
 */
public class RedisPattern {
    /**
     * 单节点
     */
    public final static String SINGLE = "single";

    /**
     * 哨兵
     */
    public final static String SENTINEL = "sentinel";
    /**
     * 主从
     */
    public final static String MASTER_SLAVE = "master-slave";

    /**
     * 集群
     */
    public final static String CLUSTER = "cluster";
}
