package alex.mochalov.fitplayer;

import alex.mochalov.editor.FragmentEditor;
import alex.mochalov.programms.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.view.*;
import alex.mochalov.record.*;
import android.util.*;

public class MainActivity extends Activity implements OnInitListener{

	FragmentFiles fp;
	FragmentPlayer fragmentPlayer;
	FragmentEditor fragmentEditor;
	
	Context mContext;

	int MY_DATA_CHECK_CODE = 0;
	boolean  langSupported;
	
	FragmentTransaction ft;
	
	Record mainFolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        
		//fillData();
		
		fp = new FragmentFiles(this);
		fragmentPlayer = new FragmentPlayer(this);
		fragmentEditor = new FragmentEditor(this);
		
		fp.listener = new FragmentFiles.
			OnStartProgrammListener(){

			@Override
			public void onGoSelected(String text)
			{
				
				ft = getFragmentManager().beginTransaction();
				
				Bundle args = new Bundle();
			    args.putString("name", text);
			    fragmentPlayer.setArguments(args);
			    
				ft.replace(R.id.frgmCont, fragmentPlayer);
				ft.addToBackStack(null);
				ft.commit();
				
			}

			@Override
			public void onEditSelected(String text)
			{
				
				ft = getFragmentManager().beginTransaction();
				
				Bundle args = new Bundle();
			    args.putString("name", text);
			    fragmentEditor.setArguments(args);
				
				ft.replace(R.id.frgmCont, fragmentEditor);
				ft.addToBackStack(null);
				ft.commit();
				
			}
			

		};
		
		ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frgmCont, fp);
		ft.commit();
	}

	
	@Override
    public void onPause() {
		
        super.onPause();
		TtsUtils.destroy();
		
    }	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch (id){
		
		case R.id.action_settings:
			return true;
		
		default:	
			return super.onOptionsItemSelected(item);
		}
	}
	
	

	@Override
	public void onInit(int status) {
	      if (status == TextToSpeech.SUCCESS) 
		    TtsUtils.init(this);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				TtsUtils.newTts(this, this);
			} else {
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}

		} else
			super.onActivityResult(requestCode, resultCode, data);
	}

	
	
	
}
