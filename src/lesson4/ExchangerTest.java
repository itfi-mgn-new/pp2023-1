package lesson4;

import java.util.concurrent.Exchanger;

public class ExchangerTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final Exchanger<String>	ex = new Exchanger<>();
		
		Thread	t = new Thread(()->{
			try {
				System.out.println("Recv: "+ex.exchange("from"));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		t.start();
		
		System.err.println("Received: "+ex.exchange("to"));
	}
}
