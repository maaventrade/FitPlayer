package alex.mochalov.calendar;

import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.text.*;
import java.util.*;

public class DialogEditCalendar extends Dialog implements android.view.View.OnClickListener
{

	private Context mContext;
	private Dialog dialog;

	private ImageButton ibOk;
	private ImageButton ibAdd;

	private Cell mCell;
	
	private ArrayList<Prog> programms;
	private ListView listViewProgramms;
	private AdapterProg adapter;
	
	MyCallback callback = null;
	interface MyCallback {
		void callbackOk(); 
		void callbackOkNew(Record newRecord); 
	} 
	
	public DialogEditCalendar(Context context, Cell cell) {
		super(context);
		mContext = context;
		dialog = this;
		mCell = cell;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dialog_edit_calendar);
		
		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
          //    WindowManager.LayoutParams.MATCH_PARENT);
		
		
		TextView title = (TextView)findViewById(R.id.tvTitle);
		title.setText(mCell.getDateStr());
		
		ListView listViewProgramms = (ListView)findViewById(R.id.listViewProgramms);
		listViewProgramms.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		programms = new ArrayList<Prog>();
		
		programms.add(new Prog("Òòöóêöóê"));
		//Utils.readFilesList(files);

		adapter = new AdapterProg(mContext, programms);

		/*
		adapter.listener = new AdapterFiles.OnButtonClickListener(){

			@Override
			public void onEdit(String text)
			{
				//Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
				if (listener != null && text.length() > 0)
					listener.onGoSelected(text);

			}

			@Override
			public void onAdd(String text)
			{

			}
		};
		*/

		listViewProgramms.setAdapter(adapter);

		listViewProgramms.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View p2, int index, long p4)
				{
 				}}
		);	

		
		
		
		ibOk = (ImageButton)findViewById(R.id.ibOk);
		ibOk.setOnClickListener(this);
		
		ibAdd = (ImageButton)findViewById(R.id.ibAdd);
		ibAdd.setOnClickListener(this);
		
//		btnCancel = (Button)findViewById(R.id.dialogeditButtonCancel);
//		btnCancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if (v == ibOk){
			if (callback != null)
				callback.callbackOk();
			
			dialog.dismiss();
		} else 
		if (v == ibAdd){
			
			Dialog dialog = new Dialog(mContext);
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Select Color Mode");

			ListView modeList = new ListView(mContext);
			String[] stringArray = new String[] { "Bright Mode", "Normal Mode" };
			ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(mContext, 
					android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
			modeList.setAdapter(modeAdapter);

			builder.setView(modeList);
			dialog = builder.create();
			dialog.show();
			
		} else {
			dialog.dismiss();
		}
		
	}	
	

	
	
}
