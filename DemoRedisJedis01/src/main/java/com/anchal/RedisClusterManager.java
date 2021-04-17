package com.anchal;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClusterManager
{
	private JedisCluster cluster;
    private JedisPoolConfig poolConfig = new JedisPoolConfig();
    private volatile static RedisClusterManager INSTANCE ;
	
    private RedisClusterManager(){
    	initialization();
    }
	
    public static RedisClusterManager getInstance(){
        if (null == INSTANCE){
            synchronized (RedisClusterManager .class){
                if (null == INSTANCE){
                    INSTANCE = new RedisClusterManager(); 
                }
            }
        }
        return INSTANCE;
    }
	
    private JedisCluster getCluster(){return cluster;}
	
	
    private void initialization(){
        poolConfig.setMaxTotal(50);
        poolConfig.setMaxIdle(50);
        poolConfig.setMaxWaitMillis(10000);
        
        String hostsString = "127.0.0.1:7000;127.0.0.1:7001;127.0.0.1:7002;127.0.0.1:7003;127.0.0.1:7004;127.0.0.1:7005";
		Set<String> hosts = new HashSet<>(Arrays.asList(hostsString.split(";")));
		Set<HostAndPort> clusterHosts = new HashSet<>();
		for (String host : hosts)
		{
			String[] hostAndPort = host.split(":");
			clusterHosts.add(new HostAndPort(hostAndPort[0],Integer.parseInt(hostAndPort[1])));
		}
		this.cluster = new JedisCluster(clusterHosts,poolConfig);

    }
	
	public boolean put(String key, Object value, int secondsToLive)
	{
			String str = getCluster().setex(key, secondsToLive, (String) value);
			return str.equalsIgnoreCase("OK");
	}
	
	public String get(final String key)  {
			
			return getCluster().get(key);
	}
	
	public boolean exists(final String key)  {
		
			return getCluster().exists(key);
	}

	public  boolean set(final String key, final Object value) 
	{
			String str = getCluster().set(key, (String) value);
			return str.equalsIgnoreCase("OK");
	}

	public void close() {
		getCluster().close();
	}
	
}

