package com.forsrc.lib.cache;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.forsrc.lib.cache.config.JbossCacheConfig;

public class JbossCacheTest {

    Cache<Serializable, Serializable> cache;

    @Before
    public void before() {
        JbossCacheConfig jbossCacheConfig = new JbossCacheConfig();
        cache = jbossCacheConfig.getCache();
    }

    @Test
    public void testJbossCache() {
        cache.put("/root/test", "test", "value");
        assertEquals(cache.get("/root/test", "test"), "value");

        Fqn<String> fqn = Fqn.fromString("/root/test");
        cache.put(fqn, "testFqn", "valueFqn");
        assertEquals(cache.get(fqn, "testFqn"), "valueFqn");

    }

    @After
    public void after() {
        cache.stop();
    }
}
