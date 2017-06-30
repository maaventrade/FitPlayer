package alex.mochalov.fitplayer;

import android.annotation.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.util.*;
import android.widget.*;
import java.util.*;

public class TtsUtils implements OnUtteranceCompletedListener
{
	private static TextToSpeech tts;
	private Locale locale;
	private static boolean  langSupported;
	private static Context mContext;
	
	private static AudioManager am = null;
	private static int volume_level;
/*
	am.setStreamVolume(
	AudioManager.STREAM_MUSIC,
	volume_level,
	0);*/
	
	public static void speak(String text)
	{

		//Toast.makeText(mContext,text,Toast.LENGTH_LONG).show();

		volume_level= am.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		am.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            1,// Set minimal volum
            0);
			
		String[] textArray = text.split(":");
		//Log.d("d", "text "+text+" "+textArray.length);
		for (String s: textArray)
		{
			//Log.d("d", "s "+s);

			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			{
				ttsGreater21(s);
			}
			else
			{
				ttsUnder20(s);
			}

			//tts.playSilence(750, TextToSpeech.QUEUE_ADD, null);
		}

	}

	public static void newTts(Context context, OnInitListener listener)
	{
		tts = new TextToSpeech(context, listener);
		
	}

	public static void destroy()
	{
		if (tts != null) tts.shutdown();
	}

	public static void init(Context context)
	{
		mContext = context;
		
		am = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
		
		langSupported = false;
		loadLangueges();
		Locale locale[] = Locale.getAvailableLocales();
		for (int i=0; i < locale.length; i++)
		{
			//Log.d("", ""+locale[i].getISO3Language());
			//Toast.makeText(this, locale[i].getLanguage().toUpperCase()+"  "+language, Toast.LENGTH_LONG).show();
			if (locale[i].getISO3Language().equals(Utils.getLanguage()))
			{
				if (tts.isLanguageAvailable(locale[i]) == tts.LANG_NOT_SUPPORTED)
				{
					Toast.makeText(context, "язык не поддерживается" + " (" + Utils.getLanguage() + ") ", Toast.LENGTH_LONG).show();
					langSupported = false;
				}
				else
				{
					langSupported = true;
					tts.setLanguage(locale[i]);
				}	
				return; 
			}}
		Toast.makeText(context, "язык не найден" + " (" + Utils.getLanguage() + ") ", Toast.LENGTH_LONG).show();

	}

	@SuppressWarnings("deprecation")
	private static void ttsUnder20(String text)
	{
		Log.i("atts", "start *0");
		
		HashMap<String, String> map = new HashMap<>();
		map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private static void ttsGreater21(String text)
	{
		
		Log.i("atts", "start +"+mContext.hashCode() + "");
		String utteranceId = mContext.hashCode() + "";
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
	}

	public void onInit(int status) {
		if(status == TextToSpeech.SUCCESS) {
			tts.setOnUtteranceCompletedListener(this);
		}
	}
	
	public void onUtteranceCompleted(String utteranceId) {
		Log.i("atts", utteranceId); //utteranceId == "SOME MESSAGE"
	
	}
	
	private static void loadLangueges()
	{
    	Locale locale[] = Locale.getAvailableLocales();
        //Spinner spinnerLanguages = ((Spinner)findViewById(R.id.spinnerLanguages));
    	/*
		 ArrayList<String> languages = new ArrayList<String>();
		 for (int i=0; i< locale.length; i++)
		 if (tts.isLanguageAvailable(locale[i]) != tts.LANG_NOT_SUPPORTED)
		 if (! languages.contains(locale[i].getISO3Language()))
		 languages.add(locale[i].getISO3Language()); 
		 adapter = new ArrayAdapter(this,
		 android.R.layout.simple_spinner_item, languages);
		 spinner.setAdapter(adapter);
		 */
    }
}
