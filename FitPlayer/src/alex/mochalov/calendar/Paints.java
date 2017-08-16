package alex.mochalov.calendar;

import alex.mochalov.fitplayer.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;

public class Paints {
	static Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final static Rect textBounds = new Rect();
	static Paint paintSmallText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	static Bitmap bmpCompleted = null;
	static Bitmap bmpCompleted1 = null;
	static Bitmap bmpGo = null;
	
	static Rect bmpCompletedSrc;

	public static Rect getTextBounds(String string, Paint paintText) {
		Paints.paintText.getTextBounds(string, 0, string.length(), textBounds);
		return textBounds;
	}

	public static void loadBitmaps(Activity mContext) {
		bmpCompleted = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.completed);
		bmpCompleted1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.completed1);
		bmpGo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.go44);
		
		bmpCompletedSrc = new Rect(0,0, bmpCompleted.getWidth(), bmpCompleted.getHeight());
	}
	
}
