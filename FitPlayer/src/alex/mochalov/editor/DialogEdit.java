package alex.mochalov.editor;

import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.text.*;

public class DialogEdit extends Dialog
{

	private Context mContext;
	private Dialog dialog;
	private Record mObject;
	
	AdapterEditor mAdapter;
	
	private EditText name;
	private Button btnOk;
	private Button btnCancel;
	
	MyCallback callback = null;
	interface MyCallback {
		void callbackACTION_DOWN(int bgID); 
	} 

	public DialogEdit(Context context, Record object, AdapterEditor adapter) {
		super(context);
		mContext = context;
		dialog = this;
		mObject = object;
		
		mAdapter = adapter;
		
		Log.d(""," mObject "+mObject);
		//setTitle(context.getResources().getString(R.string.select_canvas));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
      //        WindowManager.LayoutParams.MATCH_PARENT);

		setContentView(R.layout.dialog_edit);
	
		name = (EditText)findViewById(R.id.editTextName);
		name.setText(mObject.getName());

		final TextView textView3 = (TextView)findViewById(R.id.textView3);
		
		
		EditText text = (EditText)findViewById(R.id.editTextText);
		text.setText(mObject.getText());
		
		final EditText duration1 = (EditText)findViewById(R.id.editTextDuration1);
		
		
		duration1.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{
					//textView3.setText("*"+s+"* "+start+" "+after+" "+count);
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					String z = s.toString();
					
					int i = z.indexOf(":");
					if (s.length() == 2){
						duration1.setText(s+":");
						duration1.setSelection(3);
						
					}

					
					//duration1.setText(z);
					//if (z.length()
					/*
					if (!Character.isDigit(z.charAt(0)))
						z = "0"+z.substring(1);
					if (!Character.isDigit(z.charAt(1)))
						z = z.substring(0,1)+"0"+z.substring(2);
					
					if (z.charAt(2) != ':' )
						z = z.substring(0,2)+":"+z.substring(3);
						
					if (!Character.isDigit(z.charAt(3)))
						z = z.substring(0,3)+"0"+z.substring(4);
					if (!Character.isDigit(z.charAt(4)))
						z = z.su bstring(0,4)+"0";
						*/
					textView3.setText("*"+s+"* "+start+" "+before+" "+count);
				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					// TODO: Implement this method
				}
			});
		
		
		//duration1.setText("00:00");
		
		btnOk = (Button)findViewById(R.id.dialogeditButtonOk);
		btnOk.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{mObject.setName(name.getText());
					mAdapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			});
		
		btnCancel = (Button)findViewById(R.id.dialogeditButtonCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
					dialog.dismiss();
				}
			});
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
