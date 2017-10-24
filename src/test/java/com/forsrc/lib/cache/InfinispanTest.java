package com.forsrc.lib.cache;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.forsrc.lib.cache.config.InfinispanConfig;

@SpringBootApplication
public class InfinispanTest {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(InfinispanTest.class, args);

        EmbeddedCacheManager cacheManager = ctx.getBean(EmbeddedCacheManager.class);
        Cache<Long, String> cache = cacheManager.getCache(InfinispanConfig.CACHE_NAME);
        cache.put(System.currentTimeMillis(), "Infinispan");

        System.out.println("Values from Cache: {}" + cache.entrySet());
        System.exit(0);
    }

}
