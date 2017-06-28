package alex.mochalov.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageView;

public class BImageView extends ImageView{

	MyCallback callback = null;
	interface MyCallback {
		void callbackTouch(); 
	} 
	
	@SuppressLint("NewApi")
	public BImageView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
		
	}

	public BImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public BImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
        	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	    	if (callback != null)
        	    		callback.callbackTouch();
        	    }
                return false;
            }
       });
	}
	
}
