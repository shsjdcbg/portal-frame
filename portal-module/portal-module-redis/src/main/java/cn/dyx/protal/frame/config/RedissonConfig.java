package cn.dyx.protal.frame.config;

import cn.dyx.protal.frame.util.RedisPattern;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ClassUtils;

/**
 * Description：Redisson配置
 *
 * @author dyx
 * @date 2019/2/15 11:11
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class RedissonConfig {
    private Logger logger = LoggerFactory.getLogger(RedissonConfig.class);

    @Value("${redisson.deploy.pattern}")
    private String pattern;
    @Value("${redisson.single.address}")
    private String singleAddress;
    @Value("${redisson.master.address}")
    private String masterAddress;
    @Value("${redisson.master.name}")
    private String masterName;
    @Value("${redisson.sentinel.addresses}")
    private String sentinelAddresses;
    @Value("${redisson.slave.addresses}")
    private String slaveAddresses;
    @Value("${redisson.cluster.addresses}")
    private String clusterAddresses;
    private int connectionMinimumIdleSize = 10;
    private int idleConnectionTimeout = 10000;
    private int connectTimeout = 10000;
    private int timeout = 3000;
    private int retryAttempts = 3;
    private int retryInterval = 1500;
    @Value("${redisson.password:@null}")
    private String password = null;
    private int subscriptionsPerConnection = 5;
    private String clientName = null;
    private int subscriptionConnectionMinimumIdleSize = 1;
    private int subscriptionConnectionPoolSize = 50;
    private int connectionPoolSize = 64;
    private int database = 1;
    private int dnsMonitoringInterval = 5000;
    /**
     * 对Redis集群节点状态扫描的时间间隔。单位是毫秒。
     */
    private int scanInterval = 1000;
    private int thread;

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws Exception {
        logger.info(">>>>>>>>>>> Redisson config init.");
        Config config;
        switch (pattern) {
            case RedisPattern.SENTINEL:
                config = getSentinelConfig();
                break;
            case RedisPattern.MASTER_SLAVE:
                config = getMasterSlaveConfig();
                break;
            case RedisPattern.CLUSTER:
                config = getClusterConfig();
                break;
            default:
                config = getSingleConfig();
                break;
        }
        Codec codec = (Codec) ClassUtils.forName("org.redisson.codec.JsonJacksonCodec", ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setThreads(thread);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    private Config getSingleConfig() {
        Config config = new Config();
        config.useSingleServer().setAddress(singleAddress)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setConnectionPoolSize(connectionPoolSize)
                .setDatabase(database)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setClientName(clientName)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPassword(password);
        return config;
    }

    private Config getSentinelConfig() {
        Config config = new Config();
        String[] addresses = sentinelAddresses.split(",");
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers().setMasterName(masterName);
        for (String address : addresses) {
            sentinelServersConfig.addSentinelAddress(address);
        }
        sentinelServersConfig.setDatabase(database)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setClientName(clientName)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPassword(password);
        return config;
    }

    private Config getMasterSlaveConfig() {
        Config config = new Config();
        MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers()
                .setMasterAddress(masterAddress);
        String[] addresses = slaveAddresses.split(",");
        for (String address : addresses) {
            masterSlaveServersConfig.addSlaveAddress(address);
        }
        masterSlaveServersConfig.setDatabase(database)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setClientName(clientName)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPassword(password);
        return config;
    }

    private Config getClusterConfig() {
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        String[] addresses = clusterAddresses.split(",");
        for (String address : addresses) {
            clusterServersConfig.addNodeAddress(address);
        }
        return config;
    }
}