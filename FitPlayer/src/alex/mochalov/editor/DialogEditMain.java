package alex.mochalov.editor;

import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.database.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import android.media.projection.*;
import android.widget.MediaController.*;
import android.content.*;

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
	private Button buttonSelectMusicPath;
	
	//private TextView textViewSignal;
	/*
	private String[] spinnerArray;
	private HashMap<Integer, String> spinnerMap;
	private Spinner spinnerNextGroup;*/
	private TextView textViewPathToMp3;
	
	private CheckBox countBeforeTheEnd;
	private CheckBox playMusic;
	private CheckBox music_quieter;
	private CheckBox expand_text;
	private CheckBox speach_descr;
	
	private MediaPlayer mp;

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
		buttonSelectMusicPath = (Button)findViewById(R.id.buttonSelectMusicPath);
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
/*
					checkBoxNextGroupSignalOn = (CheckBox)findViewById(R.id.checkBoxNextGroupSignal); 
					spinnerNextGroup.getSelectedItem().toString();
					
					String id = spinnerMap.get(spinnerNextGroup.getSelectedItemPosition());
		        	RingtoneManager.getRingtone(mContext, Uri.parse(id)).play();
					
					Programm.setNextGroupSignal(checkBoxNextGroupSignalOn.isChecked(), 
								spinnerNextGroup.getSelectedItem().toString(),
								spinnerMap.get(spinnerNextGroup.getSelectedItemPosition()));
*/
					countBeforeTheEnd = (CheckBox)findViewById(R.id.countBeforeTheEnd); 
					Programm.setCountBeforeTheEnd(countBeforeTheEnd.isChecked());
					
		
					Programm.setExpand_text(expand_text.isChecked());
							
					//playMusic = (CheckBox)findViewById(R.id.playMusic); 
					Programm.setPlayMusic(playMusic.isChecked());
					
					Programm.setPathToMp3(textViewPathToMp3.getText().toString());
					

					Programm.setMusic_quieter(music_quieter.isChecked());
					
					Programm.setSpeach_descr(speach_descr.isChecked());
					
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
		
		Button btnLockUnlock = (Button)findViewById(R.id.dialogeditButtonLockUnlock);
		btnLockUnlock.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					Programm.setLock();

					if (callback != null)
						callback.callbackOk();
					dialog.dismiss();
				}
			});
			
		/*
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
			*/
		Button buttonTestMusic = (Button)findViewById(R.id.buttonTestMusic);
	    buttonTestMusic.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					testMusic( textViewPathToMp3.getText().toString() );
				}

				private void testMusic(String path)
				{
		
					File dir = new File(path+"/"); 
ArrayList<String> mp3 = new ArrayList<String>();
					File[] files = dir.listFiles();
					if (files != null )
						for (int i=0; i<files.length; i++)
							if (files[i].getName().endsWith("mp3"))
								mp3.add(files[i].getName());
							
					
					if (mp3.size() == 0)
						Toast.makeText(mContext, "No mp3 found", Toast.LENGTH_LONG).show();
					else {
						int index = (int) (Math.random() * mp3.size());
						
						if (mp != null)
							mp.stop();
						
						mp = MediaPlayer.create(mContext,
															Uri.parse(path+"/" + mp3.get(index)));
						mp.start();
						
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							public void run() {
								if (mp != null)
									mp.stop();
							}
						}, 5000);
					}
				}
			});
		
	
			
		//checkBoxNextGroupSignalOn = (CheckBox)findViewById(R.id.checkBoxNextGroupSignal); 
		//checkBoxNextGroupSignalOn.setChecked(Programm.isSoundNextGroupOn());

		countBeforeTheEnd = (CheckBox)findViewById(R.id.countBeforeTheEnd); 
		countBeforeTheEnd.setChecked(Programm.isCountBeforeTheEndOn());

		expand_text = (CheckBox)findViewById(R.id.expand_text); 
		expand_text.setChecked(Programm.isExpand_text());
		
		playMusic = (CheckBox)findViewById(R.id.playMusic); 
		playMusic.setChecked(Programm.isPlayMusicOn());
		
		textViewPathToMp3 = (TextView)findViewById(R.id.textViewPathToMp3);
		textViewPathToMp3.setText(Programm.getPathToMp3());
		
		music_quieter =  (CheckBox)findViewById(R.id.music_quieter); 
		music_quieter.setChecked(Programm.isMusic_quieter());
		
		speach_descr = (CheckBox)findViewById(R.id.speach_descr); 
		speach_descr .setChecked(Programm.isMusic_quieter());
		
		lockChanged();
	}

	private void lockChanged()
	{
		Button btnLockUnlock = (Button)findViewById(R.id.dialogeditButtonLockUnlock);
		
		if (Programm.isLocked()){
			btnLockUnlock.setText(mContext.getResources().getString(R.string.unlock));
			name.setEnabled(false);
			text.setEnabled(false);
			playMusic.setEnabled(false);
		
			countBeforeTheEnd.setEnabled(false);
			music_quieter.setEnabled(false);
			speach_descr.setEnabled(false);
			expand_text.setEnabled(false);
			
			buttonSelectMusicPath.setEnabled(false);
			btnOk.setEnabled(false);
			
		} else {
			btnLockUnlock.setText(mContext.getResources().getString(R.string.lock));
			name.setEnabled(true);
			text.setEnabled(true);
			playMusic.setEnabled(true);
			
			countBeforeTheEnd.setEnabled(true);
			music_quieter.setEnabled(true);
			speach_descr.setEnabled(true);
			expand_text.setEnabled(true);
			
			buttonSelectMusicPath.setEnabled(true);
			btnOk.setEnabled(true);
			
		}
	}

	
	protected void onDismiss(DialogInterface dialog){
		if (mp != null)
			mp.stop();
	}
}
