package lesson2;

public class SyncTest {
	static int x = 0;
	static final Object	sync = new Object();
	
	public static void main(String[] args) throws InterruptedException {
		final Thread[]	t = new Thread[10];
		
		for (int index = 0; index < t.length; index++) {
			t[index] = new Thread(()->increment());
		}
		for(Thread item : t) {
			item.start();
		}
		for(Thread item : t) {
			item.join();
		}
		System.err.println("x="+x);
	}

	private static void increment() {
		for(int index = 0; index < 100000; index++) {
//			synchronized(sync) { // try { aload sync; monitorenter
//				x++;
//			}                    // } finally {aload sync; monitorexit;}
			//x = x + 1;
			inc();
		}
	}
	
	private static synchronized void inc() {
		// synchronized(SyncTest.class) {
		x++;
		// }
	}
	
	private synchronized void nonstatic() {
		// syncronized(this) {
		// TODO: ......
		// }
	}
}
