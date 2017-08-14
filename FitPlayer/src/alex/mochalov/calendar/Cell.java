package alex.mochalov.calendar;

import android.graphics.*;
import android.text.format.*;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.*;

public class Cell
{
	private RectF mRect;
	
	private int mDay;
	private int mMonth;
	private int mYear;
	
	private String mText = "";

	public Cell(int day, int month, int year, float i, float j, float cellWidth, float cellHeight) {
		mRect = new RectF(i, j, i+cellWidth, j+cellHeight);
		
		mDay = day;
		mMonth = month;
		mYear = year;
		
	}

	
	public boolean contains(float x, float y)
	{
		return mRect.contains(x, y);
	}

	public void draw(Canvas canvas, int month, boolean selected)
	{

		
		if (selected){
			Paints.paintText.setColor(Color.BLUE);
			Paints.paintText.setStyle(Paint.Style.FILL_AND_STROKE);

			canvas.drawRect(mRect, Paints.paintText);
			
			Paints.paintText.setColor(Color.YELLOW);
			Paints.paintText.setStyle(Paint.Style.STROKE);
			Paints.paintText.setTextSize(40);
		} else {
			Paints.paintText.setColor(Color.BLACK);
			Paints.paintText.setStyle(Paint.Style.STROKE);
			Paints.paintText.setTextSize(40);

			canvas.drawRect(mRect, Paints.paintText);
		}
		
		
		Paints.paintSmallText.setColor(Color.BLACK);
		Paints.paintSmallText.setStyle(Paint.Style.STROKE);
		Paints.paintSmallText.setTextSize(24);
		
		canvas.drawRect(mRect, Paints.paintText);
		
		if (mMonth != month) 
			Paints.paintText.setColor(Color.GRAY);
		
		Rect bounds = Paints.getTextBounds("X", Paints.paintText);
		float y = 10 + mRect.top + bounds.height();
		//float restX = 
		
		canvas.drawText(""+mDay, 
					10 + mRect.left, y, Paints.paintText);
					
		y = y + bounds.height();
					
		draw3(canvas, mText, mRect,10 + mRect.left, y, bounds.height(), Paints.paintSmallText);
	
		
	}

	private void draw3(Canvas canvas, String mText, RectF mRect, float x, float y, float d, Paint paintSmallText)
	{
		float a[] = new float[mText.length()];
		Paints.paintSmallText. getTextWidths(mText,
				0, mText.length(), a);

		int k = 0;
		while (y < mRect.bottom){
			float j = x;
			while (k < a.length-1){
				canvas.drawText(mText.substring(k, k+1), 
								j, y, Paints.paintSmallText);
				j = j + a[k];
				k++;
				
				if (j >= x + mRect.width() || (k < a.length-1) && (j + a[k] * 2) >= x + mRect.width() )
					break;
				
			}
			y = y + d;
		}
		
	}


	public RectF getRect()
	{
		return mRect;
	}

	public String getDate() {
		return ""+mDay;
	}
	public String getDateStr() {
		Calendar cal = Calendar.getInstance();
		cal.set(mYear, mMonth, mDay);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
		String text = dateFormat.format(cal.getTime());
		
		return text;
	}


}
