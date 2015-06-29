package taxi.deadlock_free;

import java.awt.Point;

public class Image {
	private static final int DRAW_MARKER_DELAY = 200;

	public void drawMarker(Point location) {
		try {
			Thread.sleep(DRAW_MARKER_DELAY);
		} catch (InterruptedException e) {
		}
	}

}
