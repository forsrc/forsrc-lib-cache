package com.forsrc.lib.cache;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.TransactionMode;
import org.junit.Test;

public class InfinispanTxTest {

    @Test
    public void test() throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        // Define the default cache to be transactional
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.transaction().transactionMode(TransactionMode.TRANSACTIONAL);
        // Construct a local cache manager using the configuration we have defined
        DefaultCacheManager cacheManager = new DefaultCacheManager(builder.build());
        // Obtain the default cache
        Cache<String, String> cache = cacheManager.getCache();
        // Obtain the transaction manager
        TransactionManager transactionManager = cache.getAdvancedCache().getTransactionManager();
        // Perform some operations within a transaction and commit it
        transactionManager.begin();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        transactionManager.commit();
        // Display the current cache contents
        System.out.printf("key1 = %s\nkey2 = %s\n", cache.get("key1"), cache.get("key2"));
        // Perform some operations within a transaction and roll it back
        transactionManager.begin();
        cache.put("key1", "value3");
        cache.put("key2", "value4");
        transactionManager.rollback();
        // Display the current cache contents
        System.out.printf("key1 = %s\nkey2 = %s\n", cache.get("key1"), cache.get("key2"));
        // Stop the cache manager and release all resources
        cacheManager.stop();
    }
}
