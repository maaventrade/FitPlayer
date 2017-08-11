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

	private EditText name;
	private Button btnOk;
	private ImageButton imgBtnOk;
	private Button btnCancel;
	private Button buttonSelect;

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
		title.setText(mCell.getDate());
		
		ListView listViewProgramms = (ListView)findViewById(R.id.listViewProgramms);
		listViewProgramms.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		programms = new ArrayList<Prog>();
		//Utils.readFilesList(files);

		adapter = new AdapterProg(mContext, programms);

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

		listViewFiles.setAdapter(adapter);

		listViewFiles.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View p2, int index, long p4)
				{
					//String selectedString1 = (String) adapter.getItemAtPosition(index);
					if (selectedStringIndex == index){
						FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

						FragmentPlayer fragmentPlayer = new FragmentPlayer(mContext);

						Bundle args = new Bundle();
						args.putString("name", files.get(selectedStringIndex));
						fragmentPlayer.setArguments(args);

						ft.replace(R.id.frgmCont, fragmentPlayer, FragmentPlayer.TAG_FRAGMENT_PLAYER);
						ft.addToBackStack(null);

						ft.commit();
					}
					else selectedStringIndex = index;
				}}
		);	

		
		/*
		btnOk = (Button)findViewById(R.id.dialogeditButtonOk);
		btnOk.setOnClickListener(this);
		
		imgBtnOk = (ImageButton)findViewById(R.id.imgBtnOk);
		imgBtnOk.setOnClickListener(this);
		
		btnCancel = (Button)findViewById(R.id.dialogeditButtonCancel);
		btnCancel.setOnClickListener(this);
		
		buttonSelect = (Button)findViewById(R.id.buttonSelect);
		buttonSelect.setOnClickListener(this);
		*/			
	}

	@Override
	public void onClick(View v) {
		if (v == btnOk  || v == imgBtnOk ){
			/*
			record.setName(name.getText());
			record.setText(text.getText());
			record.setRest(itIsTheRest.isChecked());
			record.setWeight(cbWeight.isChecked());
			
			if (! mIsGroup)
				record.setDuration(duration.getText());
			*/
			//Programm.summDurations(record);
			
			if (callback != null)
				callback.callbackOk();
			
			dialog.dismiss();
		} else if (v == buttonSelect) {
			/*
			DialogSelectRecord dialog = new DialogSelectRecord(mContext);
			dialog.callback = new DialogSelectRecord.MyCallback() {
				@Override
				public void selected(Record record) {
					name.setText(record.getName());
					text.setText(record.getText());
					itIsTheRest.setChecked(record.isRest());
				}
			}; 
			dialog.show();
			*/
			
		} else {
			dialog.dismiss();
		}
		
	}	
	

	
	
}
