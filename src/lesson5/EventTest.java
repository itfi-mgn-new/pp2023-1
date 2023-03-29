package lesson5;

import java.util.concurrent.Semaphore;

public class EventTest {
	public static volatile int	x = 0;
	
	public static void main(String[] args) throws InterruptedException {
		final Semaphore	sema = new Semaphore(0);
		
		Thread	t = new Thread(()-> {
			try {
				sema.acquire();
	//		Thread.sleep(500);
				System.err.println(x);
			} catch (InterruptedException e) {
			}
		});
		t.start();
		x = 100;
		sema.release();
	}
}
