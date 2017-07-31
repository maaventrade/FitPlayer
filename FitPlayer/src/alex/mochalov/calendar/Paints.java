package alex.mochalov.calendar;

import android.graphics.Paint;
import android.graphics.Rect;

public class Paints {
	static Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final static Rect textBounds = new Rect();
	static Paint paintSmallText = new Paint(Paint.ANTI_ALIAS_FLAG);

	public static Rect getTextBounds(String string, Paint paintText) {
		Paints.paintText.getTextBounds(string, 0, string.length(), textBounds);
		return textBounds;
	}
}
