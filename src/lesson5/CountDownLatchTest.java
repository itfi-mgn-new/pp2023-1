package lesson5;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
	public static volatile int	x = 0;

	public static void main(String[] args) {
		CountDownLatch	latch = new CountDownLatch(2);
		
		Thread	t = new Thread(()-> {
			try {
				latch.await();
	//		Thread.sleep(500);
				System.err.println(x);
			} catch (InterruptedException e) {
			}
		});
		t.start();
		x = 100;
		latch.countDown();
	}

}
