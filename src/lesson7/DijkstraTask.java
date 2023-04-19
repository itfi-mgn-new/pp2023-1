package lesson7;

public class DijkstraTask {
	static final Object	stick1 = new Object();
	static final Object	stick2 = new Object();
	static final Object	stick3 = new Object();
	static final Object	stick4 = new Object();
	static final Object	stick5 = new Object();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread	t1 = new Thread(()->action(stick1,stick2));
		Thread	t2 = new Thread(()->action(stick2,stick3));
		Thread	t3 = new Thread(()->action(stick3,stick4));
		Thread	t4 = new Thread(()->action(stick4,stick5));
		Thread	t5 = new Thread(()->action(stick1,stick5));

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
	}

	static void action(final Object left, final Object right) {
		for(;;) {
			synchronized(left) {
				synchronized(right) {
					eating();
				}
			}
			thinking();
		}
	}
	
	
	static void thinking() {
		System.err.println("thinking: "+Thread.currentThread().getName());
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//		}
	}
	
	static void eating() {
		System.err.println("Eating: "+Thread.currentThread().getName());
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//		}
	}
	
}
