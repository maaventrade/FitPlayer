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

	private Record record;
	private boolean mIsGroup;
	
	private EditText name;
	private EditText text;
	private EditText duration;
	
	private Button btnOk;
	private Button btnCancel;
	
	MyCallback callback = null;
	interface MyCallback {
		void callbackOk(); 
	} 
	
	
	public DialogEdit(Context context, Record object, boolean isGroup) {
		super(context);
		mContext = context;
		dialog = this;
		
		record = object;
		mIsGroup = isGroup;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
      //        WindowManager.LayoutParams.MATCH_PARENT);

		setContentView(R.layout.dialog_edit);
	
		name = (EditText)findViewById(R.id.editTextName);
		name.setText(record.getName());
		name.requestFocus();

		text = (EditText)findViewById(R.id.editTextText);
		text.setText(record.getText());

		final TextView textView3 = (TextView)findViewById(R.id.textView3);
		duration = (EditText)findViewById(R.id.editTextDuration1);
		if (mIsGroup){
			duration.setVisibility(View.INVISIBLE);
			textView3.setVisibility(View.INVISIBLE);
		} else {
			duration.setVisibility(View.VISIBLE);
			textView3.setVisibility(View.VISIBLE);
			
			duration = (EditText)findViewById(R.id.editTextDuration1);
			duration.addTextChangedListener(new TextWatcher(){

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
							duration.setText(s+":");
							duration.setSelection(3);
							
						}
						textView3.setText("*"+s+"* "+start+" "+before+" "+count);
					}

					@Override
					public void afterTextChanged(Editable p1)
					{
						// TODO: Implement this method
					}
				});
			long l = record.getDuration();
			if (l == 0){
				duration.setText("");
			} else {
				duration.setText(Utils.MStoString(l));
			}
		}
		
		
		btnOk = (Button)findViewById(R.id.dialogeditButtonOk);
		btnOk.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
					record.setName(name.getText());
					record.setText(text.getText());
					if (! mIsGroup)
						record.setDuration(duration.getText());
					
					Programm.summDurations(record);
					
					if (callback != null)
						callback.callbackOk();
					
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