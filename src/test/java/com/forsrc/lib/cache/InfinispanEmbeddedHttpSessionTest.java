package com.forsrc.lib.cache;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.infinispan.spring.session.configuration.EnableInfinispanEmbeddedHttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableInfinispanEmbeddedHttpSession
public class InfinispanEmbeddedHttpSessionTest {

    @Bean
    public SpringEmbeddedCacheManagerFactoryBean springCache() {
       return new SpringEmbeddedCacheManagerFactoryBean();
    }

    @RestController
    static class SessionController {

       @Autowired
       SpringEmbeddedCacheManager cacheManager;

       @RequestMapping("/session")
       public Map<String, String> session(HttpServletRequest request) {
          Map<String, String> result = new HashMap<>();
          String sessionId = request.getSession(true).getId();
          result.put("created:", sessionId);
          // By default Infinispan integration for Spring Session will use 'sessions' cache.
          result.put("active:", cacheManager.getCache("sessions").getNativeCache().keySet().toString());
          return result;
       }
    }

    public static void main(String[] args) {
       SpringApplication.run(InfinispanEmbeddedHttpSessionTest.class, args);
       System.exit(0);
    }
}
