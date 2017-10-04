package alex.mochalov.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import alex.mochalov.fitplayer.R;
import alex.mochalov.main.Utils;
import alex.mochalov.programm.Programm;
import alex.mochalov.record.Record;
import android.widget.Toast;

public class Files {

	private static ArrayList<String> listDataHeader = new ArrayList<String>();
	private static HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();


	public static int getIndex(String newRecord) {
		int index = -1;

		for (String r : listDataHeader) {
			index++;
			if (r == newRecord)
				return index;

			List<String> l = listDataChild.get(r);
			if (l != null) {
				for (String p : l) {
					index++;
					if (r == p)
						return index;
				}
			}

		}

		return index;
	}

	public static void clear() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
	}

	public static ArrayList<String> getGroups() {
		return listDataHeader;
	}

	public static HashMap<String, List<String>> getChilds() {
		return listDataChild;
	}

	public static ArrayList<String> getList() {

		ArrayList<String> list = new ArrayList<String>();

		for (String r : listDataHeader) {
			boolean child = false;
			List<String> l = listDataChild.get(r);
			if (l != null) {
				if (l.size() == 0)
					list.add(r);
				else {
					for (String p : l)
						list.add(p);
				}
			} else {
				list.add(r);
			}
		}

		return list;
	}


	public static String getGroup(int index) {
		return listDataHeader.get(index);
	}

	public static String getItem(int groupPosition, int childPosition) {
		String group = listDataHeader.get(groupPosition);
		return listDataChild.get(group).get(childPosition);
	}

	public static void deleteRecord(String selectedRecord) {
		String parent = null;

		listDataHeader.remove(selectedRecord);

		if (listDataChild.get(selectedRecord) != null)
			listDataChild.remove(selectedRecord);
		else {
			for (Entry<String, List<String>> entry : listDataChild.entrySet())
				for (String r : entry.getValue())
					if (r == selectedRecord) {
						parent = entry.getKey();
						entry.getValue().remove(r);
						break;
					}

		}
	}

	public static String addCHildRecord(String selectedRecord) {
		String String = new String("New String");

		if (listDataChild.get(selectedRecord) == null) {
			ArrayList<String> newArray = new ArrayList<String>();
			newArray.add(String);
			listDataChild.put(selectedRecord, newArray);
		} else {
			listDataChild.get(selectedRecord).add(String);
		}

		return null;
	}

	

	//public static String getMainRecord() {
	//	return main;
//	}

	
	public static String addRecord(String newRecord, String currentRecord) {
		if (newRecord == null)
			newRecord = new String("new String");

		if (currentRecord == null)
			listDataHeader.add(newRecord);
		else if (listDataHeader.indexOf(currentRecord) >= 0) {
			if (currentRecord == null) {
				listDataHeader.add(newRecord);
			} else {
				listDataHeader.add(listDataHeader.indexOf(currentRecord) + 1,
						newRecord);
			}
		} else {
			for (Entry<String, List<String>> entry : listDataChild.entrySet())
				if (entry.getValue().contains(currentRecord))
					entry.getValue().add(
							entry.getValue().indexOf(currentRecord) + 1,
							newRecord);

		}

		return newRecord;
	}

	public static String copyRecord(String currentRecord) {

		String String = new String(currentRecord);

		if (listDataHeader.indexOf(currentRecord) > 0) {
			listDataHeader.add(String);
		} else {
			for (Entry<String, List<String>> entry : listDataChild.entrySet())
				if (entry.getValue().contains(currentRecord))
					entry.getValue().add(String);

		}

		return String;
	}

	public static String getGroup(String String) {

		for (Entry<String, List<String>> entry : listDataChild.entrySet())
			if (entry.getValue().contains(String))
				return entry.getKey();

		return String;
	}



	public static String pasteRecord(String copyRecord, String selectedRecord) {

		String String = new String(copyRecord);

		if (listDataHeader.indexOf(selectedRecord) >= 0) {
			listDataHeader.add(listDataHeader.indexOf(selectedRecord) + 1,
					String);
		} else {
			for (Entry<String, List<String>> entry : listDataChild.entrySet())
				if (entry.getValue().contains(selectedRecord))
					entry.getValue().add(
							entry.getValue().indexOf(selectedRecord) + 1,
							String);

		}

		return String;
	}

	private static Date date0 = new Date(0);

	public static Object getDateOfTheLastFale()
	{
		return date0;
	}

	
	public static void readFilesList() {
		listDataHeader.clear();
		listDataChild.clear();

		Record currentGroup = null;
		Record record = null;
		
		
		File file = new File(Utils.APP_FOLDER);
		if(!file.exists()){                          
			file.mkdirs();                  
		}

		File[] files = file.listFiles();
		
		for (int i = 0; i < files.length; i++)
		{
			String name = files[i].getName();
			if (name.toLowerCase().endsWith(".xml")
				&& Programm.isProgramm(files[i])
				){
				Date date  =  new Date(files[i].lastModified());
				if (date.compareTo(date0) > 0)
					date0 = date;
				///////////////programms.add(name);
			}
		}
		
        //////////////sort(programms);
		/////////////////////return ;
		
		
/*
		try {

			String name = Utils.APP_FOLDER + "/" + fileName;

			BufferedReader reader;
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					new FileInputStream(name)));

			String line = rd.readLine();

			rd.close();

			if (line.toLowerCase().contains("windows-1251"))
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(name), "windows-1251")); // Cp1252
			else if (line.toLowerCase().contains("utf-8"))
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(name), "UTF-8"));
			else if (line.toLowerCase().contains("utf-16"))
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(name), "utf-16"));
			else
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(name)));

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(reader);

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.END_TAG) {
					if (parser.getName().equals("children")) {
						currentGroup = null;
					}
				} else if (eventType == XmlPullParser.START_TAG) {

					if (parser.getName() == null)
						;
					else if (parser.getName().equals("main")) {

						int duration = 0;
						if (parser.getAttributeValue(null, "duration") != null)
							duration = Integer.parseInt(parser
									.getAttributeValue(null, "duration"));

						main = new Record(
								parser.getAttributeValue(null, "name"),
								parser.getAttributeValue(null, "text"),
								Boolean.parseBoolean(parser.getAttributeValue(
										null, "rest")), duration,
								Boolean.parseBoolean(parser.getAttributeValue(
												null, "weight")));

						soundNextName = parser.getAttributeValue(null,
								"soundNextName");
						soundNextGroupUri = parser.getAttributeValue(null,
								"soundNextGroupUri");
						soundNextGroupOn = Boolean.parseBoolean(parser
								.getAttributeValue(null, "soundNextGroupOn"));
						countBeforeTheEnd = Boolean.parseBoolean(parser
								.getAttributeValue(null, "countBeforeTheEnd"));
						pathToMp3 = parser.getAttributeValue(null, "pathToMp3");
						playMusic = Boolean.parseBoolean(parser
								.getAttributeValue(null, "playMusic"));
						music_quieter = Boolean.parseBoolean(parser
								.getAttributeValue(null, "music_quieter"));
						expand_text = Boolean.parseBoolean(parser
								.getAttributeValue(null, "expand_text"));
						speach_descr = Boolean.parseBoolean(parser
								.getAttributeValue(null, "speach_descr"));
						locked = Boolean.parseBoolean(parser.getAttributeValue(
								null, "locked"));

					} else if (parser.getName().equals("children")) {
						currentGroup = record;
						listDataChild
								.put(currentGroup, new ArrayList<Record>());
					} else if (parser.getName().equals("record")) {

						int duration = 0;
						if (parser.getAttributeValue(null, "duration") != null)
							duration = Integer.parseInt(parser
									.getAttributeValue(null, "duration"));

						String strUUID = parser.getAttributeValue(null, "id");
						UUID uuid;
						if (strUUID == null || strUUID.equals("null"))
							uuid = null;
						else uuid = UUID.fromString(strUUID);
						
						record = new Record(parser.getAttributeValue(null,
								"name"),
								parser.getAttributeValue(null, "text"),
								Boolean.parseBoolean(parser.getAttributeValue(null, "rest")), 
								duration,
								uuid, 
								Boolean.parseBoolean(parser.getAttributeValue(null, "weight")));

						//record.setID(Records.findRecord(record.getText()));
						
						
						
						if (currentGroup != null) {
							listDataChild.get(currentGroup).add(record);
						} else {
							listDataHeader.add(record);
						}

					}
				}

				try {
					eventType = parser.next();
				} catch (XmlPullParserException e) {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(R.string.error_load_xml)
									+ ". " + e.toString(), Toast.LENGTH_LONG).show();
					return false;
				}
			}

		} catch (Throwable t) {
			Toast.makeText(
					mContext,
					mContext.getResources().getString(R.string.error_load_xml)
							+ ". " + t.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
		
*/
	}

}
