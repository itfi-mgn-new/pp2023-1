package lesson6;

import java.util.concurrent.ArrayBlockingQueue;

public class QueueTEst {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final ArrayBlockingQueue<String> q = new ArrayBlockingQueue<>(10);
		
		q.put("new string");
		System.err.println("get:"+q.take());
 	}
}
