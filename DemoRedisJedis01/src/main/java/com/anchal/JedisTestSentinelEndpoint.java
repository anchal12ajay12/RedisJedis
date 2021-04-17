package com.anchal;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;

public class JedisTestSentinelEndpoint {
    private static final String MASTER_NAME = "mymaster";
  
    private static final Set<String> sentinels;
    static {
        sentinels = new HashSet<String>();
        sentinels.add("127.0.0.1:26379");
        sentinels.add("127.0.0.1:26380");
        sentinels.add("127.0.0.1:26381");
        
    }
    
    

    public JedisTestSentinelEndpoint() {
    }

    void Check() throws InterruptedException {
        
        JedisSentinelPool pool = new JedisSentinelPool(MASTER_NAME, sentinels);
        Jedis jedis = null;
       
            try {
            	System.out.println("Fetching connection from pool");
                jedis = pool.getResource();
               
                Socket socket = jedis.getClient().getSocket();
                System.out.println("Connected to " + socket.getRemoteSocketAddress());
              
            } catch (JedisException e) {
            	System.out.println("Connection error of some sort!");
            	System.out.println(e.getMessage());
                Thread.sleep(2 * 1000);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
     
    }
}