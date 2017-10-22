package com.forsrc.lib.cache;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.assertj.core.util.Arrays;
import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
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

        System.out.println("----------------");
        cache.getMembers().forEach(address -> {
            System.out.println("--> " + address.toString());
        });
        cache.put("/root/test", "test", "value");
        assertEquals(cache.get("/root/test", "test"), "value");

        Fqn<String> fqn = Fqn.fromString("/root/test");
        cache.put(fqn, "testFqn", "valueFqn");
        assertEquals(cache.get(fqn, "testFqn"), "valueFqn");

        JbossCacheMap<String, String> map = new JbossCacheMap<>("/root/testMap");
        map.put("test", "OK");
        assertEquals(map.get("test"), "OK");
        System.out.println("----------------");
        map.getCache().getMembers().forEach(address -> {
            System.out.println("--> " + address.toString());
        });

        JbossCacheMap<String, String> mapTest = new JbossCacheMap<>("/root/testMap");
        System.out.println("----------------");
        mapTest.getCache().getMembers().forEach(address -> {
            System.out.println("--> " + address.toString());
        });

        Node<String, String> node = mapTest.getCache().getRoot();
        System.out.println("----------------");
        System.out.println(node.dataSize());
    }

    @After
    public void after() {
        cache.stop();
    }
}
