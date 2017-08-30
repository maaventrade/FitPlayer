package alex.mochalov.editor;

import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.programm.Programm;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.text.*;

public class DialogEdit extends Dialog implements android.view.View.OnClickListener
{

	private Context mContext;
	private Dialog dialog;

	private Record record;
	private boolean mIsGroup;
	
	private EditText name;
	private EditText text;
	private EditText duration;
	
	private Button btnOk;
	private ImageButton imgBtnOk;
	private Button btnCancel;
	private Button buttonSelect;
	
	private CheckBox itIsTheRest;
	private CheckBox cbWeight;
	
	private boolean newRecord = false;
	
	MyCallback callback = null;
	interface MyCallback {
		void callbackOk(); 
		void callbackOkNew(Record newRecord); 
	} 
	
	
	public DialogEdit(Context context, Record object, boolean isGroup) {
		super(context);
		mContext = context;
		dialog = this;
		
		if (object == null){
			record = new Record("New");
			newRecord = true;
		} else
			record = object;
			
		mIsGroup = isGroup;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dialog_edit);
		
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
              WindowManager.LayoutParams.MATCH_PARENT);
		
		name = (EditText)findViewById(R.id.editTextName);
		name.setText(record.getName());
		name.setSelectAllOnFocus(true);

		TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
		if (newRecord)
			tvTitle.setText(mContext.getResources().getString(R.string.title_add));
		else
			tvTitle.setText(mContext.getResources().getString(R.string.title_edit));
		
		TextView tvExercise = (TextView)findViewById(R.id.tvExercise);
		tvExercise.setText(record.getText());
							   
		
		text = (EditText)findViewById(R.id.editTextText);
		text.setText(record.getText());

		TextView textViewStartTime = (TextView)findViewById(R.id.textViewStartTime);
		textViewStartTime.setText(Utils.MStoString(Programm.getTimeBefore(record)));
		
		itIsTheRest = (CheckBox)findViewById(R.id.checkBoxItIsTheRest);
		itIsTheRest.setChecked(record.isRest());
		
		cbWeight = (CheckBox)findViewById(R.id.cbWeight);
		cbWeight.setChecked(record.isWeight());
		
		TextView textView3 = (TextView)findViewById(R.id.textView3);
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
						if (s.length() == 2 && start == 1){
							duration.setText(s+":");
							duration.setSelection(3);
							
						}
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
		btnOk.setOnClickListener(this);
		
		imgBtnOk = (ImageButton)findViewById(R.id.imgBtnOk);
		imgBtnOk.setOnClickListener(this);
		
		btnCancel = (Button)findViewById(R.id.dialogeditButtonCancel);
		btnCancel.setOnClickListener(this);
		
		buttonSelect = (Button)findViewById(R.id.buttonSelect);
		buttonSelect.setOnClickListener(this);
	
		name.requestFocus();
        name.selectAll();
/*
		Editable text = name.getText();
        if (text.length() > 0) {
            text.replace(0, 1, text.subSequence(0, 1), 0, 1);
            name.selectAll();
        }*/		
	}

	@Override
	public void onClick(View v) {
		if (v == btnOk  || v == imgBtnOk ){
			record.setName(name.getText().toString());
			//record.setText(text.getText());
			record.setRest(itIsTheRest.isChecked());
			record.setWeight(cbWeight.isChecked());
			
			if (! mIsGroup)
				record.setDuration(duration.getText());
			
			Programm.summDurations(record);
			
			if (callback != null)
				if (newRecord)
					callback.callbackOkNew(record);
				else callback.callbackOk();
			
			dialog.dismiss();
		} else if (v == buttonSelect) {
			DialogSelectRecord dialog = new DialogSelectRecord(mContext);
			dialog.callback = new DialogSelectRecord.MyCallback() {
				@Override
				public void selected(Exercise record) {
					name.setText(record.getName());
					text.setText(record.getText());
					itIsTheRest.setChecked(record.isRest());
				}
			}; 
			dialog.show();
			
		} else {
			dialog.dismiss();
		}
		
	}	
	

	
	
}
