package alex.mochalov.editor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import alex.mochalov.fitplayer.R;
import alex.mochalov.fitplayer.Utils;
import alex.mochalov.record.Programm;
import alex.mochalov.record.Record;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DialogEditMain extends Dialog
{

	private Activity mContext;
	private Dialog dialog;

	private Record record;
	private boolean mIsGroup;
	
	private EditText name;
	private EditText text;
	
	private Button btnOk;
	private Button btnCancel;
	
	//private TextView textViewSignal;
	
	private String[] spinnerArray;
	private HashMap<Integer, String> spinnerMap;
	private Spinner spinnerNextGroup;
	private TextView textViewPathToMp3;
	
	int MY_DATA_GET_SIGNALS = 5;
	
	MyCallback callback = null;
	interface MyCallback {
		void callbackOk(); 
	} 
	
	
	public DialogEditMain(Activity context, Record object, boolean isGroup) {
		super(context);
		mContext = context;
		dialog = this;
		
		record = object;
		mIsGroup = isGroup;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_edit_main);
		
		this.setTitle(mContext.getResources().getString(R.string.edit_main));
		
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
              WindowManager.LayoutParams.MATCH_PARENT);
	
		name = (EditText)findViewById(R.id.editTextName);
		name.setText(record.getName());
		name.requestFocus();

		text = (EditText)findViewById(R.id.editTextText);
		text.setText(record.getText());

		textViewPathToMp3 = (TextView)findViewById(R.id.textViewPathToMp3);
		Button buttonSelectMusicPath = (Button)findViewById(R.id.buttonSelectMusicPath);
		buttonSelectMusicPath.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View p1)
			{
				DialogSelectPath dialog = new DialogSelectPath(mContext, textViewPathToMp3);
				dialog.show();
			}
		});
		
		btnOk = (Button)findViewById(R.id.dialogeditButtonOk);
		btnOk.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
					record.setName(name.getText());
					record.setText(text.getText());

					CheckBox checkBoxNextGroupSignal = (CheckBox)findViewById(R.id.checkBoxNextGroupSignal); 
					spinnerNextGroup.getSelectedItem().toString();
					
					String id = spinnerMap.get(spinnerNextGroup.getSelectedItemPosition());
		        	RingtoneManager.getRingtone(mContext, Uri.parse(id)).play();
					
					Programm.setNextGroupSignal(checkBoxNextGroupSignal.isChecked(), 
								spinnerNextGroup.getSelectedItem().toString(),
								spinnerMap.get(spinnerNextGroup.getSelectedItemPosition()));

					CheckBox countBeforeTheEnd = (CheckBox)findViewById(R.id.countBeforeTheEnd); 
					Programm.setCountBeforeTheEnd(countBeforeTheEnd.isChecked());
							
					CheckBox playMusic = (CheckBox)findViewById(R.id.playMusic); 
					Programm.setPlayMusic(playMusic.isChecked());
					
					
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
		
		spinnerNextGroup = (Spinner)findViewById(R.id.spinnerNextGroup);
		
		RingtoneManager manager = new RingtoneManager(mContext);
	    manager.setType(RingtoneManager.TYPE_NOTIFICATION); //TYPE_NOTIFICATION //TYPE_ALARM
	    Cursor cursor = manager.getCursor();

	    spinnerArray = new String[cursor.getCount()];
	    spinnerMap = new HashMap<Integer, String>();
	    
	    //Map<String, String> list = new HashMap<>();
	    int i = 0;
	    while (cursor.moveToNext()) {

	    	int currentPosition = cursor.getPosition();
	        String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);

	        spinnerMap.put(i, manager.getRingtoneUri(currentPosition).toString());
	        spinnerArray[i] = notificationTitle;

	        i++;
	    }
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, spinnerArray);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinnerNextGroup.setAdapter(adapter);
	    
	    Button buttonTestSound = (Button)findViewById(R.id.buttonTestSound);
	    buttonTestSound.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{

					String id = spinnerMap.get(spinnerNextGroup.getSelectedItemPosition());
		        	RingtoneManager.getRingtone(mContext, Uri.parse(id)).play();
					
				}
			});
	    /*
		Button buttonTestMusic = (Button)findViewById(R.id.buttonTestMusic);
		buttonTestMusic.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
			        File dir = new File(PATH+"/"); 
			        File[] files = dir.listFiles();
			        if (files != null )
			            for (int i = 0; i < files.length; i++)
			            	if (files[i].getName().endsWith(".mp3")){
								MediaPlayer mediaPlayer = MediaPlayer.create(mContext, 
										Uri.parse(PATH+ "/"+files[i].getName()));
								mediaPlayer.start();
			        	}	
				}
			});
	    */
	}

}
