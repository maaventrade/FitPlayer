package alex.mochalov.fitplayer;

import alex.mochalov.editor.FragmentEditor;
import alex.mochalov.programms.*;
import alex.mochalov.record.Folder;
import android.app.*;
import android.content.*;
import android.os.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.view.*;

public class MainActivity extends Activity implements OnInitListener{

	FragmentFiles fp;
	FragmentPlayer fragmentPlayer;
	FragmentEditor fragmentEditor;
	
	Context mContext;

	int MY_DATA_CHECK_CODE = 0;
	boolean  langSupported;
	
	FragmentTransaction ft;
	
	Folder mainFolder;
	
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
			    
				ft.add(R.id.frgmCont, fragmentPlayer);
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
				
				ft.add(R.id.frgmCont, fragmentEditor);
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
		case R.id.action_pause:
			//fr.pause();
			return true;
		case R.id.action_settings:
			return true;
		case R.id.action_start:
			//fr.start();
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

	
	private void fillData() {
		mainFolder = new Folder("Main folder");
		mainFolder.addRecord("Record 1", 
							 "Синоптики из центра погоды «Фобос» сообщили, что пока москвичам не стоит ждать потепления.",
							 1000);
		mainFolder.addRecord("Record 2", 
							 "В столице 22 июня ожидается прохладная погода до +17 градусов. Специалисты прогнозируют кратковременные дожди, погода будет облачная с прояснениями. В последующие дни температура воздуха немного поднимется, однако в столичном регионе все еще будет высокая вероятность осадков.",
							 2000);
		mainFolder.addRecord("Record 3", 
							 "В четверг характер погоды в Центральном регионе изменится мало. Местами пройдут кратковременные дожди, которые в сочетании с прохладными воздушными массами будут сдерживать прогрев воздуха, – сказал представитель «Фобоса».Впрочем, согласно предварительному прогнозу, в понедельник, 26 июня, будет уже тепло – до +28 градусов.",
							 3000);

		mainFolder.addRecord("Record 4", 
							 "For the first generation of tablets running Android 3.0, the proper way to declare tablet layouts was to put them in a directory with the xlarge configuration qualifier (for example, res/layout-xlarge/). In ",
							 4000);
		mainFolder.addRecord("Record 5", 
							 "For the first generation of tablets running Android 3.0, the proper way to declare tablet layouts was to put them in a directory with the xlarge configuration qualifier (for example, res/layout-xlarge/). In ",
							 5000);
		mainFolder.addRecord("Record 6", 
							 "For the first generation of tablets running Android 3.0, the proper way to declare tablet layouts was to put them in a directory with the xlarge configuration qualifier (for example, res/layout-xlarge/). In ",
							 6000);
		mainFolder.addRecord("Record 7", 
							 "For the first generation of tablets running Android 3.0, the proper way to declare tablet layouts was to put them in a directory with the xlarge configuration qualifier (for example, res/layout-xlarge/). In ",
							 7000);
		mainFolder.addRecord("Record 8", 
							 "For the first generation of tablets running Android 3.0, the proper way to declare tablet layouts was to put them in a directory with the xlarge configuration qualifier (for example, res/layout-xlarge/). In ",
							 8000);
		
		Utils.saveProgramm(this, "New programm.xml", mainFolder);
	}
	
}
