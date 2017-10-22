package com.forsrc.lib.cache;

import java.io.Serializable;

public class JbossCache<K extends Serializable, V extends Serializable> {

    private static JbossCache INSTANCE;

    private JbossCache() {
        
    }

    public JbossCache<K, V> getInstance() {
        if (INSTANCE == null) {
            synchronized (JbossCache.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JbossCache<K, V> ();
                }
            }
        }
        return INSTANCE;
    }
}
