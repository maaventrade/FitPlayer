package alex.mochalov.editor;

import java.text.DecimalFormat;
import java.util.ArrayList;

import alex.mochalov.fitplayer.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

public class DialogEdit extends Dialog
{ 
	private Context mContext;
	private Dialog dialog;
	MyCallback callback = null;
	interface MyCallback {
		void callbackACTION_DOWN(int bgID); 
	} 

	public DialogEdit(Context context) {
		super(context);
		mContext = context;
		dialog = this;
		//setTitle(context.getResources().getString(R.string.select_canvas));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
      //        WindowManager.LayoutParams.MATCH_PARENT);

		setContentView(R.layout.dialog_edit);
		
		EditText duration = (EditText)findViewById(R.id.editTextDuration);
		
		//DecimalFormat format = new DecimalFormat("##:##");
		//String formattedText = format.format(1);
		//duration.setText(formattedText);
		
/*
      final ArrayList<Integer> canvas = new ArrayList<Integer>();
      AdapterCanvas boxAdapter; 
      boxAdapter = new AdapterCanvas(context, canvas);

		canvas.add(new Integer(R.drawable.canvas0));
		canvas.add(new Integer(R.drawable.canvas1));
		canvas.add(new Integer(R.drawable.canvas2));
		
		ListView listView = (ListView) findViewById(R.id.listViewCanvas);
      listView.setAdapter(boxAdapter);
		listView.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
					callback.callbackACTION_DOWN(canvas.get(index));
					dialog.dismiss();
				}}
		);
		*/
		
	}	
}
