package alex.mochalov.editor;

import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.programm.Programm;
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

public class DialogEditMain extends Dialog implements android.view.View.OnClickListener
{

	private Activity mContext;
	private Dialog dialog;

	private Record record;
	private boolean mTune;
	
	//private EditText name;
	private EditText text;
	
	private Button btnOk;
	private ImageButton imgBtnOk;
	private Button btnCancel;
	private Button btnSelectMusicPath;
	private Button btnLockUnlock;
	
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

	public MyCallback callback = null;
	public interface MyCallback {
		void callbackOk(); 
	} 
	
	
	public DialogEditMain(Activity context, Record object, boolean tune) {
		super(context);
		mContext = context;
		dialog = this;
		
		record = object;
		mTune = tune;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dialog_edit_main);
		
		//this.setTitle(mContext.getResources().getString(R.string.edit_main));
		
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
              WindowManager.LayoutParams.MATCH_PARENT);
	
		//name = (EditText)findViewById(R.id.editTextName);
		//name.setText(record.getName());
		//name.requestFocus();

		TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvTitle.setText(mContext.getResources().getString(R.string.edit_main));
		
		text = (EditText)findViewById(R.id.editTextText);
		text.setText(record.getText());

		textViewPathToMp3 = (TextView)findViewById(R.id.textViewPathToMp3);
		btnSelectMusicPath = (Button)findViewById(R.id.btnSelectMusicPath);
		btnSelectMusicPath.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View p1)
			{
				DialogSelectPath dialog = new DialogSelectPath(mContext, textViewPathToMp3.getText().toString());
				dialog.callback = new DialogSelectPath.SelectFileCallback() {
					@Override
					public void callbackOk(String path) {
						textViewPathToMp3.setText(path);
					}
				}; 
				dialog.show(); 
			}
		});
		
		imgBtnOk = (ImageButton)findViewById(R.id.imgBtnOk);
		imgBtnOk.setOnClickListener(this);
		
		btnOk = (Button)findViewById(R.id.dialogeditButtonOk);
		btnOk.setOnClickListener(this);
					
		btnCancel = (Button)findViewById(R.id.dialogeditButtonCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					dialog.dismiss();
				}
			});
		
		btnLockUnlock = (Button)findViewById(R.id.dialogeditButtonLockUnlock);
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
		speach_descr .setChecked(Programm.isSpeach_descr());
		
		if (mTune){
			btnLockUnlock.setVisibility(View.INVISIBLE);
			
			LinearLayout layoutName = (LinearLayout)findViewById(R.id.layoutName);
			layoutName.setVisibility(View.INVISIBLE);
			
			LinearLayout layoutText = (LinearLayout)findViewById(R.id.layoutText);
			layoutText.setVisibility(View.INVISIBLE);
			
		} else {
			lockChanged();
		}
	}

	private void lockChanged()
	{
		
		if (Programm.isLocked()){
			btnLockUnlock.setText(mContext.getResources().getString(R.string.unlock));
			//name.setEnabled(false);
			text.setEnabled(false);
			playMusic.setEnabled(false);
		
			countBeforeTheEnd.setEnabled(false);
			music_quieter.setEnabled(false);
			speach_descr.setEnabled(false);
			expand_text.setEnabled(false);
			
			btnSelectMusicPath.setEnabled(false);
			btnOk.setEnabled(false);
			
		} else {
			btnLockUnlock.setText(mContext.getResources().getString(R.string.lock));
			//name.setEnabled(true);
			text.setEnabled(true);
			playMusic.setEnabled(true);
			
			countBeforeTheEnd.setEnabled(true);
			music_quieter.setEnabled(true);
			speach_descr.setEnabled(true);
			expand_text.setEnabled(true);
			
			btnSelectMusicPath.setEnabled(true);
			btnOk.setEnabled(true);
			
		}
	}

	
	protected void onDismiss(DialogInterface dialog){
		if (mp != null)
			mp.stop();
	}

	@Override
	public void onClick(View v) {
		if (v == btnOk || v == imgBtnOk ){
			//record.setName(name.getText());
			//record.setText(text.getText());

			countBeforeTheEnd = (CheckBox)findViewById(R.id.countBeforeTheEnd); 
			Programm.setCountBeforeTheEnd(countBeforeTheEnd.isChecked());

			Programm.setExpand_text(expand_text.isChecked());
					
			Programm.setPlayMusic(playMusic.isChecked());
			
			Programm.setPathToMp3(textViewPathToMp3.getText().toString());

			Programm.setMusic_quieter(music_quieter.isChecked());
			
			Programm.setSpeach_descr(speach_descr.isChecked());
			
			if (callback != null)
				callback.callbackOk();
			
			dialog.dismiss();
		}
		
	}
}
