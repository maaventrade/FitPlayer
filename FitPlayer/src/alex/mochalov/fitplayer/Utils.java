package alex.mochalov.fitplayer;

import alex.mochalov.record.Record;
import android.content.*;
import android.os.*;
import android.util.*;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class Utils {

	static final String PROGRAMM_FOLDER = "fitPlayer";
	static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
	public final static String APP_FOLDER = EXTERNAL_STORAGE_DIRECTORY+"/xolosoft/"+PROGRAMM_FOLDER;
	static String REC_FOLDER = APP_FOLDER+"/rec";
	
	private static String language = "rus";
	
	public static String action = "";
	
	public static String getRecFolder(){
		return REC_FOLDER;
	}

	public static String firstLetterToUpperCase(String text) {
		if (text == null || text.length() == 0) return "";
		else return text.substring(0,1).toUpperCase() + text.substring(1);
	}
	
	public static String getLanguage(){
		return language;
	}
	
	
	public static void loadHTML(String word, String translation, WebView webView) {
		if (! word.equals(""))
			translation = "<b>" + Utils.firstLetterToUpperCase(word) + "</b>"
				+ translation;

		translation = translation.replace("\n", "<br>");
		translation = translation.replace("<abr>", "<font color = #00aa00>");
		translation = translation.replace("</abr>", "</font>");

		translation = translation.replace("<ex>", "<font color = #aa7777>");
		translation = translation.replace("</ex>", "</font>");

		int start = translation.indexOf("<kref>");
		int end = translation.indexOf("</kref>");
		while (start >= 0 && end >= 0) {
			String text = translation.substring(start + 6, end);

			translation = translation.substring(0, start) + "<a href =\""
					+ "http://" + text + "\">" + text + "</a>"
					+ translation.substring(end + 7);

			start = translation.indexOf("<kref>");
			end = translation.indexOf("</kref>");
		}

		webView.loadData(translation, "text/html; charset=utf-8", "UTF-8");
	}

	public static void readFilesList(ArrayList<String> programms) {
		
		File file = new File(APP_FOLDER);
		if(!file.exists()){                          
			file.mkdirs();                  
		}

		File[] files = file.listFiles();
		
		for (int i = 0; i < files.length; i++)
		{
			String name = files[i].getName();
			if (name.toLowerCase().endsWith(".xml"))
				programms.add(name);
		}
		
	}

	public static Record loadXML(Context mContext, String name) {
		Record mainFolder = null; 
		Record currentFolder = null; 
		Record record = null; 
		//strings.clear();
		/*
		 * try { progressDialog.setMax(Utils.countLines(name)); } catch
		 * (IOException e) { progressDialog.setMax(0); }
		 * progressDialog.setProgress(0);
		 */
		try {
			
			name = APP_FOLDER + "/" + name;
			
			Log.d("a","start "+name);
			BufferedReader reader;
			BufferedReader rd = new BufferedReader(new InputStreamReader(new FileInputStream(name)));
			
			String line = rd.readLine();
			
			rd.close();
			
			if (line.toLowerCase().contains("windows-1251"))
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(name), "windows-1251")); //Cp1252
			else if (line.toLowerCase().contains("utf-8"))
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(name), "UTF-8")); 
			else if (line.toLowerCase().contains("utf-16"))
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(name), "utf-16"));
			else
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(name)));

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);         
			XmlPullParser parser = factory.newPullParser();
			
			parser.setInput(reader);
			
			int eventType = parser.getEventType();         
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if(eventType == XmlPullParser.START_DOCUMENT) {} 
				else if(eventType == XmlPullParser.END_TAG) {
					if(parser.getName() == "children")
						currentFolder = null;
				}
				else if(eventType == XmlPullParser.START_TAG) {
					//Log.d("", "START "+parser.getName());
					
					if(parser.getName() == null);
					else if(parser.getName().equals("children")){
						currentFolder = record;
						//Log.d("","currentFolder "+currentFolder);
					}
					else if(parser.getName().equals("record")){
						
						int duration = 0;
						if (parser.getAttributeValue(null, "duration") != null)
						duration = Integer.parseInt(parser.getAttributeValue(null, "duration"));
						
						record = new Record(parser.getAttributeValue(null, "name"),
							parser.getAttributeValue(null, "text"),
							duration);
						
						if (mainFolder == null){
							mainFolder = record;
							//Log.d("","mainFolder "+mainFolder);
						}
						
						if (currentFolder != null){
							//Log.d("","currentFolder add "+currentFolder);
							//currentFolder.addRecord(record);
						}
						//Log.d("", "name "+parser.getAttributeValue(null, "name"));
						//Log.d("", "text "+parser.getAttributeValue(null, "text"));
						//Log.d("", "duration "+parser.getAttributeValue(null, "duration"));
						//mainFolder.addRecord(parser.getAttributeValue(null, "name"),
						//		parser.getAttributeValue(null, "text"),
						//		Integer.parseInt(parser.getAttributeValue(null, "duration")));
					}
				} 
				
				try {
					eventType = parser.next();
				}
				catch (XmlPullParserException  e) {
				}
			}
			
					
		} catch (Throwable t) {
			Toast.makeText(mContext, mContext.getResources().getString(R.string.error_load_xml)+". "+t.toString(), Toast.LENGTH_LONG).show();
		}
		return mainFolder;
	}

	public static String MStoString(long l) {
		int d1 = (int)(l/60000);
		int d2 = (int)l/1000 - (int)(l/60000) * 60;
		
		String s1 = "";
		String s2 = "";
		
		if (d1 <= 0)
			s1 = "00";
		else if (d1 < 10)
			s1 = "0"+d1;
		else s1 = ""+d1;
		
		if (d2 <= 0)
			s2 = "00";
		else if (d2 < 10)
			s2 = "0"+d2;
		else s2 = ""+d2;
		
		return s1+":"+s2;
	}

	
}


/*
Log.d("", "START "+parser.getName());
if (parser.getName().toLowerCase().equals("dn_stop")) aDnStop = true;
						if (parser.getName().toLowerCase().equals("body")){
							body = true;
							if (parser.getAttributeValue(null, "name") != null)
								if (parser.getAttributeValue(null, "name").equals("notes"))
									body = false;
						}
						if (parser.getName().toLowerCase().equals("a")) a = true;
						} */
