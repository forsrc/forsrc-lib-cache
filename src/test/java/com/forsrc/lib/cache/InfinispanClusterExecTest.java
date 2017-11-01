package com.forsrc.lib.cache;

import java.util.Random;

import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.ClusterExecutor;
import org.infinispan.manager.DefaultCacheManager;
import org.junit.Test;

public class InfinispanClusterExecTest {

    @Test
    public void test() {
     // Setup up a clustered cache manager
        GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
        // Initialize the cache manager
        DefaultCacheManager cacheManager = new DefaultCacheManager(global.build());
        ClusterExecutor clusterExecutor = cacheManager.executor();
        clusterExecutor.submitConsumer(cm -> new Random().nextInt(), (address, intValue, exception) ->
                System.out.printf("%s\n", intValue));
        // Shuts down the cache manager and all associated resources
        cacheManager.stop();
    }
}
