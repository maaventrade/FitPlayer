package alex.mochalov.fitplayer;

import alex.mochalov.record.*;
import android.content.*;
import android.media.*;
import android.net.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class Media
{

	private static MediaPlayer mediaPlayer;
	private static String path;
	private static ArrayList<String> mp3 = new ArrayList<String>();

	public static void restart(Context mContext, boolean restartMusic)
	{
		if (Programm.isPlayMusicOn() && mp3.size() > 0){
			if (restartMusic){
				if (mediaPlayer != null){
					mediaPlayer.stop();
					mediaPlayer = null;
				}
					
				int index = (int) (Math.random() * mp3.size());
				mediaPlayer = MediaPlayer.create(mContext,
												 Uri.parse(path+"/" + mp3.get(index)));
				restartMusic = false;
			}
			mediaPlayer.start();
		}
	}

	public static void start()
	{
		if (Programm.isPlayMusicOn() && mp3.size() > 0)
    		if (mediaPlayer != null)
    			mediaPlayer.start();
		
	}

	public static void stop()
	{
		if (mediaPlayer != null){
        	mediaPlayer.stop();
        	mediaPlayer = null;
        }
	}

	public static void pause(){
		if (mediaPlayer != null)
			mediaPlayer.pause();
		
	}
	
	
	public static void loadMediaFiles(Context mContext)
	{
		
		path = Programm.getPathToMp3();

		File dir = new File(path+"/"); 
		File[] files = dir.listFiles();

		Toast.makeText(mContext, path+"/", Toast.LENGTH_LONG).show();


		if (files != null )
			for (int i=0; i<files.length; i++)
				if (files[i].getName().endsWith("mp3"))
					mp3.add(files[i].getName());

		
		// TODO: Implement this method
	}
	
	public static MediaPlayer getMediaPlayer(){
		return mediaPlayer;
	}
}
