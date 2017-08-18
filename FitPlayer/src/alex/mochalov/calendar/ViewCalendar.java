package alex.mochalov.calendar;

import android.annotation.*;
import android.content.*;
import android.graphics.*;
import android.text.format.*;
import android.util.*;
import android.view.*;
import android.widget.ImageButton;

import java.text.*;
import java.util.*;

import alex.mochalov.record.*;


public class ViewCalendar extends View
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
	
	private Date today;
	
	private Cell selectedCell = null;
	
	private final Rect textBounds = new Rect();
	
	private ViewCalendar thisView;

	@SuppressLint("NewApi")
	public ViewCalendar(Context context, 
							AttributeSet attrs, 
							int defStyleAttr, 
							int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	public ViewCalendar(Context context, 
							AttributeSet attrs, 
							int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	public ViewCalendar(Context context, 
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
		thisView = this;
		
		paintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintBg.setColor(Color.YELLOW);
		
		Paints.paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		Paints.paintText.setColor(Color.BLACK);
		Paints.paintText.setStyle(Paint.Style.STROKE);
		Paints.paintText.setTextSize(40);
		
		Calendar cal = Calendar.getInstance();
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		//Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		today = cal.getTime();
	
		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					post(new Runnable() {
							public void run() {
								thisView.getHeight(); //height is ready
								cellWidth = getWidth()/7;
								cellHeight = getHeight()/(cellsCountVert+1);
					
								if (cellWidth > 0)
									fillCells();
								invalidate();
							}
						});
				}
			});
		
		setLayerType(View.LAYER_TYPE_SOFTWARE, Paints.paintShadow);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		cellWidth = getWidth()/7;
		cellHeight = getHeight()/(cellsCountVert+1);
		Log.d("y","OnMes "+cellWidth);
		if (cellWidth > 0)
			fillCells();
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()){
		case MotionEvent.ACTION_UP:
			
			for (int j = 0; j < cellsCountVert; j++)
				for (int i = 0; i<7; i++)
					if (cells[i][j].contains(x, y)){
						if (selectedCell == cells[i][j]){
							DialogEditCalendar dialog = new DialogEditCalendar(mContext, cells[i][j]);
							dialog.callback = new DialogEditCalendar.DialodEditCalendarCallback(){

								@Override
								public void callbackOk()
								{
									invalidate();
								}

								@Override
								public void callbackOkNew(Record newRecord)
								{
									// TODO: Implement this method
								}
							};
							dialog.show();
						} else {
							selectedCell = cells[i][j];
							invalidate();
						}
						break;
					}
			
			break;
		case MotionEvent.ACTION_DOWN:
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
		canvas.drawColor(Color.WHITE);
		
		int is = -1;
		int js = -1;
		
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < cellsCountVert; j++){
				if (cells[i][j] != null )
					cells[i][j].draw(canvas, month, false, today);
				if (cells[i][j] == selectedCell){
					is = i;
					js = j;
				}
		}
		
		if (selectedCell != null){
			//int x;
			//int y;
			canvas.save();
			RectF rect = selectedCell.getRect();
			
			
			if (is == 0 && js == 0)
				canvas.scale(1.3f, 1.3f, rect.left,  rect.top);
			else if (is == 0 && js == cellsCountVert-1)
				canvas.scale(1.3f, 1.3f, rect.left,  rect.bottom);
			else if (is == 6 && js == cellsCountVert-1)
				canvas.scale(1.3f, 1.3f, rect.right,  rect.bottom);
			else if (is == 6 && js == 0)
				canvas.scale(1.3f, 1.3f, rect.right,  rect.top);
			
			else if (js == cellsCountVert-1)
				canvas.scale(1.3f, 1.3f, rect.left + rect.width()/2,  rect.bottom);
			
			else if (is == 0)
				canvas.scale(1.3f, 1.3f, rect.left + rect.width()/2,  rect.top);
			
			else if (is == 6)
				canvas.scale(1.3f, 1.3f, rect.right,  rect.top+ rect.height()/2);
			
			else if (js == 0)
				canvas.scale(1.3f, 1.3f, rect.left + rect.width()/2,  rect.top);
			
			else
				canvas.scale(1.3f, 1.3f, rect.left + rect.width()/2,  rect.top + rect.height()/2);
			
			selectedCell.draw(canvas, month, true, today);
		    canvas.restore();			
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

	public void init(int m) {
		month = m - m / 12 * 12;
		m = m + (2000 * 12);
		year = m / 12;

		fillCells();
		invalidate();
	}
	//public void setFiles(String[] files) {
	//	mFiles = files;
	//}
	
	
}

/*
cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
cal.clear(Calendar.MINUTE);
cal.clear(Calendar.SECOND);
cal.clear(Calendar.MILLISECOND);

*/
