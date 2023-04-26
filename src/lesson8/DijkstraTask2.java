package lesson8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class DijkstraTask2 {
	static final Garson	g = new Garson();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread	t1 = new Thread(()->action(0,1));
		Thread	t2 = new Thread(()->action(1,2));
		Thread	t3 = new Thread(()->action(2,3));
		Thread	t4 = new Thread(()->action(3,4));
		Thread	t5 = new Thread(()->action(4,0));

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
	}

	static void action(final int left, final int right) {
		for(;;) {
			try(GroupLocker locker = g.lockGroup(left, right)) {
				eating();
			} catch (InterruptedException e) {
				break;
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


class Garson {
	private final BlockingQueue<Request> queue = new ArrayBlockingQueue<>(10);
	private final Thread t = new Thread(()->dispatch());
	private final boolean[] locks = new boolean[5];
	private List<Request> awaited = new ArrayList<>();
	
	public Garson() {
		t.setDaemon(true);
		t.start();
	}
	
	public GroupLocker lockGroup(final int left, final int right) throws InterruptedException {
		final Request	r = new Request(true, left, right);
		
		queue.put(r);
		r.latch.await();
		
		return new GroupLocker() {
			@Override
			public void close() throws InterruptedException {
				queue.put(new Request(false, left, right));
			}
		};
	}
	
	private void dispatch() {
		for(;;) {
			try{
				final Request	r = queue.take();
				
				if (r.get) {
					if (!locks[r.left] && !locks[r.right]) {
						locks[r.left] = true;
						locks[r.right] = true;
						r.latch.countDown();
					}
					else {
						awaited.add(r);
					}
				}
				else {
					locks[r.left] = false;
					locks[r.right] = false;
//					for (Request t : awaited) {
//					}
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}

class Request {
	final boolean get;
	final int left;
	final int right;
	final CountDownLatch latch = new CountDownLatch(1);
	
	public Request(boolean get, int left, int right) {
		this.get = get;
		this.left = left;
		this.right = right;
	}
}


interface GroupLocker extends AutoCloseable {
	@Override
	public void close() throws InterruptedException;
}
