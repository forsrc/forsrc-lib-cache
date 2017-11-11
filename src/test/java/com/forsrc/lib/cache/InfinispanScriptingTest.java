package com.forsrc.lib.cache;

import java.util.HashMap;
import java.util.Map;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfiguration;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;
import org.junit.Test;

public class InfinispanScriptingTest {

    @Test
    public void test() {
        
        GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
        org.infinispan.configuration.cache.ConfigurationBuilder configurationBuilder = new org.infinispan.configuration.cache.ConfigurationBuilder();
        configurationBuilder.clustering().cacheMode(CacheMode.DIST_SYNC);
        // Initialize the cache manager
        DefaultCacheManager defaultCacheManager = new DefaultCacheManager(global.build(), configurationBuilder.build());

        HotRodServerConfiguration hotrodConfig = new HotRodServerConfigurationBuilder()
                .host("127.0.0.1")
                .port(ConfigurationProperties.DEFAULT_HOTROD_PORT)
                .topologyStateTransfer(false)
                .build();
        HotRodServer server = new HotRodServer();
        server.start(hotrodConfig, defaultCacheManager);
        defaultCacheManager.defineConfiguration("___script_cache", new org.infinispan.configuration.cache.ConfigurationBuilder().build());

        // Create a configuration for a locally-running server
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host("127.0.0.1").port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        // Connect to the server
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        // Retrieve the cache containing the scripts
        RemoteCache<String, String> scriptCache = cacheManager.getCache("___script_cache");
        // Create a simple script which multiplies to numbers
        scriptCache.put("simple.js", "multiplicand * multiplier");
        // Obtain the remote cache
        RemoteCache<String, Integer> cache = cacheManager.getCache();
        // Create the parameters for script execution
        Map<String, Object> params = new HashMap<>();
        params.put("multiplicand", 10);
        params.put("multiplier", 20);
        // Run the script on the server, passing in the parameters
        Object result = cache.execute("simple.js", params);
        // Print the result
        System.out.printf("Result = %s\n", result);
        // Stop the cache manager and release resources
        cacheManager.stop();
        defaultCacheManager.stop();
        server.stop();
    }
}
