package lesson2;

public class Exercises {
	static int x = 0;

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// 1. ЗАпустить два потока которые пооеердно увелилчвают 
		// значение поля x
		// 2. Запустить два потока которые считают сумму текущего
		// значения x 
		// 3. В конце каждый из потоков выдает накопленную сумму
		
		int sum = 0;
		for (int index = 0; index < 20000; index++) {
			sum += index;
		}
		System.err.println("REsult="+sum);
		Thread	t1 = new Thread(()->write());
		Thread	t2 = new Thread(()->write());
		Thread  t3 = new Thread(()->read());
		Thread  t4 = new Thread(()->read());
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		t1.join();
		t2.join();
		
		t3.interrupt();
		t4.interrupt();
		
		t3.join();
		t4.join();
		
		System.err.println("x="+x);
	}

	static void write() {
		for(int index = 0; index < 10000; index++) {
			inc();
		}
	}
	
	static void read() {
		int	localX = 0, newX, sum = 0;
		
		while (!Thread.interrupted()) {
			newX = get();
			for(int current = localX; current < newX; current++) {
				sum += current;
			}
			localX = newX;
		}
		System.err.println("Sum="+sum);
	}
	
	static synchronized void inc() {
		x++;
	}
	
	static synchronized int get() {
		return x;
	}
}
