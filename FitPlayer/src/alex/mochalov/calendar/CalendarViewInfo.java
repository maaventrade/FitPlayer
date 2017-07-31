package alex.mochalov.calendar;

import android.annotation.*;
import android.content.*;
import android.graphics.*;
import android.text.format.*;
import android.util.*;
import android.view.*;
import java.text.*;
import java.util.*;


public class CalendarViewInfo extends View
{

	private Context mContext;
	private Paint paintBg;

	private float cellWidth;
	private float cellHeight;
	
	private final int cellsCountVert = 6;
	private final int cellsCountHor = 7;

	private Cell cells[][] = new Cell[cellsCountHor][cellsCountVert];
	
	//private int curentMonth = 0;
	
	private int month = 0;
	private int year = 0;
	
	private final Rect textBounds = new Rect();
	
	

	@SuppressLint("NewApi")
	public CalendarViewInfo(Context context, 
							AttributeSet attrs, 
							int defStyleAttr, 
							int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	public CalendarViewInfo(Context context, 
							AttributeSet attrs, 
							int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	public CalendarViewInfo(Context context, 
							AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public void nextMonth()
	{
		shiftMonth(1);
	}

	public void prevMonth()
	{
		shiftMonth(-1);
	}
	
	private void init(Context context){
		mContext = context;
		paintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintBg.setColor(Color.YELLOW);
		
		Paints.paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		Paints.paintText.setColor(Color.BLACK);
		Paints.paintText.setStyle(Paint.Style.STROKE);
		Paints.paintText.setTextSize(40);
		
		Calendar cal = Calendar.getInstance();
		
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);

		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		cellWidth = getWidth()/7;
		cellHeight = getHeight()/(cellsCountVert+1);
		
		if (cellWidth > 0)
			fillCells();
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			if (x < 500)
				shiftMonth(-1);
			else shiftMonth(1);
			
		}
			
		
		
		return true;
	}
	
	
	public void shiftMonth(int delta) {
		if (month + delta < 0){
			month = 11;
			year = year - 1; 
		} else if (month + delta > 11){
			month = 0;
			year = year + 1; 
		} else
			month = month + delta;
			
		fillCells();
		invalidate();
		
	}

	private void fillCells()
	{
		cells = new Cell[7][6];

		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);
		
		int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		int first = cal.get(Calendar.DAY_OF_WEEK)-1;
		if (first == 0)
			first = 7;
		cal = null;
		
		cal = Calendar.getInstance();
		cal.set(year, month, 1);
		cal.add(Calendar.MONTH, -1);
		int prevMonth = cal.get(Calendar.MONTH);
		int maxPrev = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int dayTmp;
		int monthTmp;
		int yearTmp;
		
		if (first != 1){
			dayTmp =  maxPrev - (first-2);
			monthTmp = prevMonth; 
			yearTmp = cal.get(Calendar.YEAR);
		}
		else {
			dayTmp = 1;
			prevMonth = -1;
			monthTmp = month; 
			yearTmp = year; 
		}


		for (int j = 0; j < cellsCountVert; j++)
			for (int i = 0; i<7; i++)
			{
				cells[i][j] = new Cell(dayTmp, monthTmp , yearTmp, cellWidth*i,
									   cellHeight + cellHeight*j, cellWidth, cellHeight);
				dayTmp++;
				if (prevMonth >= 0){
					if (dayTmp > maxPrev){
						dayTmp = 1;
						prevMonth = -1;
						monthTmp = month; 
						yearTmp = year; 
					}
				} else if (dayTmp > max){
					cal = Calendar.getInstance();
					cal.set(year, month, 1);
					cal.add(Calendar.MONTH, 1);
					monthTmp = cal.get(Calendar.MONTH); 
					yearTmp = cal.get(Calendar.YEAR);
					
					dayTmp = 1;
				}
			}
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.YELLOW);
		
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < cellsCountVert; j++){
				if (cells[i][j] != null )
					cells[i][j].draw(canvas, month);
		}
				
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
		String text = dateFormat.format(cal.getTime());
	
		Paints.paintText.setColor(Color.BLACK);
		
		Paints.paintText.getTextBounds("X", 0, 1, textBounds);
		canvas.drawText(text, 10 , 10+textBounds.height(), Paints.paintText);
		int textHeight = textBounds.height();
		  
		String[] namesOfDays = DateFormatSymbols.getInstance().getShortWeekdays();
		String sunday = namesOfDays[1];
		for (int i = 0; i<7; i++)
			namesOfDays[i] = namesOfDays[i+1];
		namesOfDays[7] = sunday;
		
		for (int i = 0; i<7; i++){
			if (i == 5)
				Paints.paintText.setColor(Color.RED);
			
			Paints.paintText.getTextBounds(namesOfDays[i+1], 0, namesOfDays[i+1].length(), textBounds);
			canvas.drawText( namesOfDays[i+1], cellWidth*i + cellWidth/2 - textBounds.exactCenterX(), cellHeight - textHeight, Paints.paintText);
		}	
				
		//drawBackground(canvas);
		//drawWeekNumbersAndDates(canvas);
		//drawWeekSeparators(canvas);
		//drawSelectedDateVerticalBars(canvas);
	}
	
	
}

/*
cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
cal.clear(Calendar.MINUTE);
cal.clear(Calendar.SECOND);
cal.clear(Calendar.MILLISECOND);

*/
