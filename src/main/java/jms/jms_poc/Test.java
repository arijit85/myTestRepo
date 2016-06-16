package jms.jms_poc;


public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Producer prod = new Producer();
		Consumer cons = new Consumer();
		Thread p = new Thread(prod);
		Thread c = new Thread(cons);
		p.start();
		c.start();

	}

}
