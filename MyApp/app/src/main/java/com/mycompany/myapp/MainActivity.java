package com.mycompany.myapp;

import android.app.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(
									 Color.BLUE));
    }
}
