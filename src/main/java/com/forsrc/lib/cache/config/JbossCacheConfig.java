package com.forsrc.lib.cache.config;

import java.io.Serializable;

import org.jboss.cache.Cache;
import org.jboss.cache.CacheFactory;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.config.CacheLoaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JbossCacheConfig {

    @Value("spring.application.name")
    private String appName;

    @Bean(destroyMethod = "stop")
    public Cache<Serializable, Serializable> getCache() {
        CacheFactory<Serializable, Serializable> factory = new DefaultCacheFactory<>();
        Cache<Serializable, Serializable> cache = factory.createCache("config/jboss-cache.xml", true);
        org.jboss.cache.config.Configuration config = cache.getConfiguration();
        //config.setClusterName(appName);
        cache.create();
        cache.start();
        return cache;
    }
}
