package alex.mochalov.main;

import alex.mochalov.record.*;
import android.app.Activity;
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


	public static void newMediaPlayer(final Activity mContext) {
		mediaPlayer = new MediaPlayer();
		
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		    public void onCompletion(MediaPlayer mp) {
				if(mp.isPlaying()) 
					mp.stop();
				mp.reset();
					
				int index = (int) (Math.random() * mp3.size());
				
				try {
					mp.setDataSource(mContext, Uri.parse(path+"/" + mp3.get(index)));
					mp.prepareAsync();
					
				} catch (IllegalArgumentException | SecurityException
						| IllegalStateException | IOException e) {
					
					e.printStackTrace();
				}
				
		    }
		});		
		
		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});
		
	}
	
	public static void restart(final Context mContext, boolean restartMusic)
	{
		if (Programm.isPlayMusicOn() && mp3.size() > 0){
			if (restartMusic){
				if (mediaPlayer != null){
					if(mediaPlayer.isPlaying()) 
						mediaPlayer.stop();
				}
					
				int index = (int) (Math.random() * mp3.size());
				try {
					mediaPlayer.setDataSource(mContext, Uri.parse(path+"/" + mp3.get(index)));
				} catch (IllegalArgumentException | SecurityException
						| IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				mediaPlayer.prepareAsync();
			}
			if(! mediaPlayer.isPlaying()) 
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
			if(mediaPlayer.isPlaying()) 
				mediaPlayer.stop();
			mediaPlayer.release();
        }
	}

	public static void pause(){
		if (mediaPlayer != null)
			if(mediaPlayer.isPlaying()) 
				mediaPlayer.pause();
	}
	
	public static void loadMediaFiles(Context mContext)
	{
		path = Programm.getPathToMp3();
		mp3.clear();

		File dir = new File(path+"/"); 
		File[] files = dir.listFiles();

		if (files != null )
			for (int i=0; i<files.length; i++)
				if (files[i].getName().endsWith("mp3"))
					mp3.add(files[i].getName());
	}
	
	public static MediaPlayer getMediaPlayer()
	{
		return mediaPlayer;
	}

}
