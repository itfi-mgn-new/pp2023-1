package lesson10;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		final ExecutorService ste = Executors.newSingleThreadExecutor();
		
		Future<?> f = ste.submit(()->System.err.println("jshgdhjsdh"));

		System.err.println("Get 1:"+f.get());
		
		Callable<String> c = new Callable<String>() {
			@Override
			public String call() throws Exception {
				System.err.println("11111111");
				throw new RuntimeException("djfhjkdhfkjh"); 
				//return "ok";
			}
		};

		Future<?> f2 = ste.submit(c);
		System.err.println("Get 2:"+f2.get());
		
		ste.shutdown();
	}

}
