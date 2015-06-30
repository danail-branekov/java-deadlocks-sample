package taxi.deadlock_free_v2;

public class TaxiObserver implements Runnable {
	private final Dispatcher dispather;

	public TaxiObserver(Dispatcher dispatcher) {
		this.dispather = dispatcher;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			System.out.println("Observer is getting image");
			dispather.getImage();
		}
	}

}
