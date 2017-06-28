package alex.mochalov.fitplayer;

import alex.mochalov.editor.*;
import alex.mochalov.programms.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.util.*;
import android.view.*;

public class MainActivity extends Activity implements OnInitListener{

	FragmentFiles fragmentFiles;
	FragmentPlayer fragmentPlayer;
	FragmentEditor fragmentEditor;
	
	String TAG_FRAGMENT_FILES = "TAG_FRAGMENT_FILES";
	
	String TAG_FRAGMENT_EDITOR = "TAG_FRAGMENT_EDITOR";
	
	Context mContext;

	int MY_DATA_CHECK_CODE = 0;
	boolean  langSupported;
	
	
	Record mainFolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        Log.d("e","1");

		if (savedInstanceState != null){
			fragmentFiles = (FragmentFiles)getFragmentManager().findFragmentByTag(TAG_FRAGMENT_FILES);
			fragmentFiles.setParams(this);

			fragmentPlayer = (FragmentPlayer)getFragmentManager().findFragmentByTag(FragmentPlayer.TAG_FRAGMENT_PLAYER);
			if (fragmentPlayer != null)
				fragmentPlayer.setParams(this);
			else
				fragmentPlayer = new FragmentPlayer(this);

			fragmentEditor = (FragmentEditor)getFragmentManager().findFragmentByTag(TAG_FRAGMENT_EDITOR);
			if (fragmentEditor != null)
				fragmentEditor.setParams(this);
			else
				fragmentEditor = new FragmentEditor(this);
			
			//Utils.setRandomize( savedInstanceState.getBoolean(RANDOMIZE));
			//Utils.setScale( savedInstanceState.getInt(HELPTEXTSCALE));
			//Dictionary.setDictionaryName(savedInstanceState.getString(DIC));
			//Dictionary.setLastWord(savedInstanceState.getString(DIC_LASTWORD));
			//Dictionary.setText(savedInstanceState.getString(DIC_TEXT));
			
		} else {
			fragmentFiles = new FragmentFiles(this);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.frgmCont, fragmentFiles, TAG_FRAGMENT_FILES);
			ft.commit();

			fragmentPlayer = new FragmentPlayer(this);
			fragmentEditor = new FragmentEditor(this);
			
			//Utils.setScale( prefs.getInt(HELPTEXTSCALE, 110));
		}		
	}

	
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		//MenuData.setText(savedInstanceState.getString(MTEXT));
		//Utils.setRandomize(savedInstanceState.getBoolean(RANDOMIZE));
		//Utils.setScale( savedInstanceState.getInt(HELPTEXTSCALE));
		
		//Log.d("aaa", "SET HELPTEXTSCALE "+savedInstanceState.getInt(HELPTEXTSCALE));
		
		//MenuData.putRandomizationOrder(savedInstanceState.getIntArray(RANDOMIZATION_ORDER));
		
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		//outState.putString(MTEXT, MenuData.getText());
		//outState.putBoolean(RANDOMIZE, Utils.getRandomize());
		//outState.putIntArray(RANDOMIZATION_ORDER, MenuData.getRandomizationOrder());
		
		//outState.putInt( HELPTEXTSCALE, Utils.getScale());
		
		//outState.putString( DIC, Dictionary.getDictionaryName());
		//outState.putString(DIC_LASTWORD, Dictionary.getLastWord());
		//outState.putString(DIC_TEXT, Dictionary.getText());
		//Log.d("aaa", "PUT "+Utils.getScale());
	}
	
	@Override
    public void onPause() {
		TtsUtils.destroy();
		
        super.onPause();
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
