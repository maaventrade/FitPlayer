package alex.mochalov.calendar;

import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.*;
import android.view.*;
import android.text.*;

import java.util.*;

public class DialogEditCalendar extends Dialog implements android.view.View.OnClickListener
{

	private Context mContext;
	private Dialog dialog;
	private Dialog dialog1;

	private ImageButton ibOk;
	private ImageButton ibAdd;
	private Button btnOk;
	private Button btnCancel;

	private Cell mCell;
	
	private ArrayList<Prog> programms;
	private ListView listViewProgramms;
	private AdapterProg adapter;
	private ArrayList mFiles = new ArrayList();
	
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
		
		//mFiles = files;
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

		programms = CalendarData.get(mCell.getDate());
		
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
		
		btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if (v == btnOk){
			if (callback != null)
				callback.callbackOk();
			
			CalendarData.replace(mCell.getDate(), programms);
				
				
			dialog.dismiss();
		} else 
		if (v == btnCancel){
			
			dialog.dismiss();
		} else 
		
		if (v == ibAdd){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(mContext.getResources().getString(R.string.select_programm));

			ListView modeList = new ListView(mContext);
			
			modeList.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					programms.add(new Prog(mFiles.get(position).toString()));
					adapter.notifyDataSetChanged();
	                dialog1.cancel();
				}});
			
			
			Utils.readFilesList(mFiles);

			ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(mContext, 
					android.R.layout.simple_list_item_1, android.R.id.text1, mFiles);
			modeList.setAdapter(modeAdapter);

			builder.setView(modeList);
			
			builder.setNegativeButton("Cancel",
			        new DialogInterface.OnClickListener()
			        {
			            public void onClick(DialogInterface dialog, int id)
			            {
			                dialog1.cancel();
			            }
			        });
			
			dialog1 = builder.create();
			dialog1.show();
			
		} else {
			dialog.dismiss();
		}
		
	}	
	

	
	
}
