package lesson14;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

// https://ignite.apache.org/docs/latest/key-value-api/basic-cache-operations

/*
--add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED 
--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED 
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED 
--add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED 
--add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED 
--add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED 
--add-opens=java.base/java.io=ALL-UNNAMED 
--add-opens=java.base/java.nio=ALL-UNNAMED 
--add-opens=java.base/java.util=ALL-UNNAMED 
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED 
--add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED 
--add-opens=java.base/java.lang=ALL-UNNAMED
 */


public class Test {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		 // Preparing IgniteConfiguration using Java APIs
        IgniteConfiguration cfg = new IgniteConfiguration();

        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);

        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        // Starting the node
        Ignite ignite = Ignition.start(cfg);

        // Create an IgniteCache and put some values in it.
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");
        IgniteCache<Integer, Long> cacheResult = ignite.getOrCreateCache("myResult");
        cache.put(1, "Hello");
        cache.put(2, "World!");

        System.err.println(">> Created the cache and add the values.");

        // Executing custom Java compute task on server nodes.
        ignite.compute(ignite.cluster().forServers()).broadcast(new RemoteTask());

        System.err.println(">> Compute task is executed, check for output on the server nodes.");
        
        cacheResult.forEach((e)->{
        	System.err.println(e.getKey()+"="+e.getValue());
        });
        

        // Disconnect from the cluster.
        ignite.close();	
	}

	
	private static class RemoteTask implements IgniteRunnable {
		private static final long serialVersionUID = 9065576604736699834L;
		
		@IgniteInstanceResource
        Ignite ignite;

        @Override public void run() {
            System.err.println(">> Executing the compute task");

            System.err.println(
                "   Node ID: " + ignite.cluster().localNode().id() + "\n" +
                "   OS: " + System.getProperty("os.name") +
                "   JRE: " + System.getProperty("java.runtime.name"));

            IgniteCache<Integer, String> cache = ignite.cache("myCache");

            UUID id = ignite.cluster().localNode().id(); 
            
            IgniteCache<UUID, Long> result = ignite.cache("myResult");
            
//            Lock lock = result.lock(id);
//            try{
//            	lock.lock();

                result.put(id, System.currentTimeMillis());
//            }
//            finally {
//                lock.unlock();
//            }            
            
            System.err.println(">> " + cache.get(1) + " " + cache.get(2));
        }
    }	
}
