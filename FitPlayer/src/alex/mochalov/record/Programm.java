package alex.mochalov.record;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import alex.mochalov.fitplayer.R;
import alex.mochalov.fitplayer.Utils;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Programm {
	
	private static Record main = new Record("New programm");
	private static boolean soundNextGroupOn; 
	private static String soundNextName; 
	private static String soundNextGroupUri; 
	private static boolean countBeforeTheEnd; 
	private static boolean playMusic; 
	private static String pathToMp3; 

	private static ArrayList<Record> listDataHeader = new ArrayList<Record>();
	private static HashMap<Record, List<Record>> listDataChild = new HashMap<Record, List<Record>>();

	private static Record currentRecord = null;

	public static String getPathToMp3()
	{
		return pathToMp3;
	}

	public static boolean isPlayMusicOn()
	{
		
		return playMusic;
	}

	public static boolean isCountBeforeTheEndOn()
	{
	
		return countBeforeTheEnd;
	}

	public static boolean isSoundNextGroupOn()
	{
		return soundNextGroupOn;
	}
	
	/*
	public static Record getNextRecord()
	{
		// TODO: Implement this method
		return null;
	}

	public static Record getFirstRecord()
	{

		return null;
	}

	public static Record getCurrentRecord()
	{
		return currentRecord;
	}	
	*/
	public static ArrayList<Record> getGroups() {
		return listDataHeader;
	}

	public static HashMap<Record, List<Record>> getChilds() {
		return listDataChild;
	}

	public static ArrayList<Record> getList(){
		
		ArrayList<Record> list = new ArrayList<Record>();
		
		for (Record r : listDataHeader){
			boolean child = false;
			List<Record> l = listDataChild.get(r);
			if (l.size() == 0)
				list.add(r);
			else {
				for(Record p: l)
					list.add(p);
			}
		}
		
		return list;
	}
	
	public static boolean loadXML(Context mContext, String fileName) {

		listDataHeader.clear();
		listDataChild.clear();
		
		Record currentGroup = null; 
		Record record = null; 

		try {
			
			String name = Utils.APP_FOLDER + "/" + fileName;
			
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
					Log.d("", "END "+parser.getName());
					if(parser.getName().equals("children")){
						currentGroup = null;
						Log.d("", "currentGroup = null ");
					}
				}
				else if(eventType == XmlPullParser.START_TAG) {
					Log.d("", "START "+parser.getName());
					
					if(parser.getName() == null);
					else if(parser.getName().equals("main")){
						
						int duration = 0;
						if (parser.getAttributeValue(null, "duration") != null)
						duration = Integer.parseInt(parser.getAttributeValue(null, "duration"));
						
						main = new Record(parser.getAttributeValue(null, "name"),
							parser.getAttributeValue(null, "text"),
							duration);
							
						soundNextName = parser.getAttributeValue(null, "soundNextName");
						soundNextGroupUri = parser.getAttributeValue(null, "soundNextGroupUri");
						soundNextGroupOn = Boolean.parseBoolean( parser.getAttributeValue(null, "soundNextGroupOn"));
						countBeforeTheEnd = Boolean.parseBoolean( parser.getAttributeValue(null, "countBeforeTheEnd"));
						pathToMp3 = parser.getAttributeValue(null, "pathToMp3");
						playMusic = Boolean.parseBoolean( parser.getAttributeValue(null, "playMusic"));
						
						
					}
						else if(parser.getName().equals("children")){
						currentGroup = record;
						listDataChild.put(currentGroup, new ArrayList<Record>());
						Log.d("", "currentGroup = record "+record.getName());
					}
					else if(parser.getName().equals("record")){
						
						int duration = 0;
						if (parser.getAttributeValue(null, "duration") != null)
						duration = Integer.parseInt(parser.getAttributeValue(null, "duration"));
						
						record = new Record(parser.getAttributeValue(null, "name"),
							parser.getAttributeValue(null, "text"),
							duration);
						
						if (currentGroup != null){
							listDataChild.get(currentGroup).add(record);
						} else {
							listDataHeader.add(record);
						}
						
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
			return false;
		}
		
		return true;
	}

	public static Record getGroup(int index) {
		return listDataHeader.get(index);
	}

	public static Record getItem(int groupPosition, int childPosition) {
		Record group = listDataHeader.get(groupPosition);
		return listDataChild.get(group).get(childPosition);
	}

	public static void deleteRecord(Record selectedRecord) {
		listDataHeader.remove(selectedRecord);
		
		if (listDataChild.get(selectedRecord) != null)
			listDataChild.remove(selectedRecord);
		else {
			for (Entry<Record, List<Record>> entry : listDataChild.entrySet()) 
	            for (Record r : entry.getValue())
	            	if (r == selectedRecord){
	            		entry.getValue().remove(r);
	            		break;
	            	}
			
		}
	}

	public static Record addCHildRecord(Record selectedRecord) {
		Record record = new Record("New record");
		
		if (listDataChild.get(selectedRecord) == null){
			ArrayList<Record> newArray = new ArrayList<Record>();
			newArray.add(record);
			listDataChild.put(selectedRecord, newArray);
		} else {
			listDataChild.get(selectedRecord).add(record);
		}
		
		return null;
	}

	public static boolean save(Context mContext, String fileName) {
		try {

			File file = new File(Utils.APP_FOLDER);
			if(!file.exists()){                          
				file.mkdirs();                  
			}
			file = new File(Utils.APP_FOLDER, fileName);
			
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"\n");
			writer.write("<body>"+"\n");

			writer.write("<main name=\""
					 +main.getName()+"\""
					 +" text=\""+main.getText()+"\""
					 +" duration=\""+main.getDuration()+"\""
					 +" soundNextGroupOn=\""+soundNextGroupOn+"\""
					 +" soundNextName=\""+soundNextName+"\""
					 +" soundNextGroupUri=\""+soundNextGroupUri+"\""
					 +" countBeforeTheEnd=\""+countBeforeTheEnd+"\""
					 +" pathToMp3=\""+pathToMp3+"\""
					 +" playMusic=\""+playMusic+"\""
					 +">"+"\n");
			writer.write("</main>"+"\n");
			
			for (Record r: listDataHeader){
				writer.write("<record name=\""
						 +r.getName()+"\""
						 +" text=\""+r.getText()+"\""
						 +" duration=\""+r.getDuration()+"\""
						 +">"+"\n");
				
				if (listDataChild.get(r) != null){
					writer.write("<children>"+"\n");
					for (Record l: listDataChild.get(r)){
						writer.write("<record name=\""
								 +l.getName()+"\""
								 +" text=\""+l.getText()+"\""
								 +" duration=\""+l.getDuration()+"\""
								 +">"+"\n");
						writer.write("</record>"+"\n");
					}
					writer.write("</children>"+"\n");
				}
				writer.write("</record>"+"\n");
			}
			
			writer.write("</body>"+"\n");

			writer.close();
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.file_saved)+" "+fileName, Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			//Utils.setInformation(context.getResources().getString(R.string.error_save_file)+" "+e);
			Toast.makeText(mContext, mContext.getResources().getString(R.string.error_saving_file) +" "+e , Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public static Record getMainRecord() {
		return main;
	}

	public static void summDurations(Record record) {

		for (Entry<Record, List<Record>> entry : listDataChild.entrySet()) {
			if (entry.getValue().contains(record)){
	            Record group = entry.getKey();
	            long duration = 0;
	            
	            for (Record r : entry.getValue() )
	            	duration = duration + r.getDuration();
	            group.setDuration(duration);
	            break;
	        }
	    }
        //
        // Calculate total duration
        //
        long duration = 0;
		for (Entry<Record, List<Record>> entry : listDataChild.entrySet()) 
            for (Record r : entry.getValue() )
            	duration = duration + r.getDuration();
		main.setDuration(duration);		
	}

	public static Record addRecord(Record currentRecord) {
		Record record = new Record("New record");
		
		if (listDataHeader.indexOf(currentRecord) >= 0){
			if (currentRecord == null){
				listDataHeader.add(record);
			} else {
				listDataHeader.add(listDataHeader.indexOf(currentRecord)+1, record);
			}
		} else {
			for (Entry<Record, List<Record>> entry : listDataChild.entrySet()) 
				if (entry.getValue().contains(currentRecord))
					entry.getValue().add(entry.getValue().indexOf(currentRecord)+1, record);
											
		}
		
		
		return record;
	}

	public static Record copyRecord(Record currentRecord) {
		
		Record record = new Record("New record");
		record.mName = currentRecord.mName;
		record.setText(currentRecord.getText());
		record.setDuration(currentRecord.getDuration());
		
		if (listDataHeader.indexOf(currentRecord) > 0){
			listDataHeader.add(record);
		} else {
			for (Entry<Record, List<Record>> entry : listDataChild.entrySet()) 
				if (entry.getValue().contains(currentRecord))
					entry.getValue().add(record);
											
		}
		
		
		return record;
	}

	public static Record getGroup(Record record) {
		
		for (Entry<Record, List<Record>> entry : listDataChild.entrySet()) 
			if (entry.getValue().contains(record))
				return entry.getKey();
		
		return record;
	}

	public static void setNextGroupSignal(boolean checked, String string1,
			String string2) {

		soundNextGroupOn = checked; 
		soundNextName = string1; 
		soundNextGroupUri = string2; 
		
	}

	public static void setCountBeforeTheEnd(boolean checked) {
		countBeforeTheEnd = checked; 
	}

	public static void setPlayMusic(boolean checked) {
		playMusic = checked; 
	}

	public static void setPathToMp3(String param) {
		pathToMp3 = param; 
	}

	public static long getTimeBefore(Record record) {
		long time = 0;
		
		for (Entry<Record, List<Record>> entry : listDataChild.entrySet()) 
            for (Record r : entry.getValue())
            	if (r == record) break;
            	else time = time + r.getDuration();
		
		return time;
	}
	
		
}	
