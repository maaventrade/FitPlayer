package alex.mochalov.programm;

import alex.mochalov.files.*;
import alex.mochalov.fitplayer.*;
import alex.mochalov.main.*;
import alex.mochalov.record.*;
import android.content.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import java.util.Map.*;
import org.xmlpull.v1.*;

public class Programm {

	private static Record main = new Record("New programm");
	private static boolean soundNextGroupOn = false;
	private static String soundNextName = "";
	private static String soundNextGroupUri = "";
	private static boolean countBeforeTheEnd = true;
	private static boolean playMusic = false;
	private static String pathToMp3 = "";
	private static boolean music_quieter = true;
	private static boolean expand_text = false;
	private static boolean speach_descr = true;
	private static boolean locked = false;

	private static ArrayList<Record> listDataHeader = new ArrayList<Record>();
	private static HashMap<Record, List<Record>> listDataChild = new HashMap<Record, List<Record>>();

	public static void setRecs()
	{
		for (Entry<Record, List<Record>> entry : listDataChild.entrySet())
			for (Record r : entry.getValue())
				
					r.setRec();

				
	}

	public static boolean isProgramm(File file)
	{
		try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
												new FileInputStream(file.getPath())));
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(reader);

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("body")) 
					{
						if (parser.getAttributeValue(null, "type").equals("fpprogramm")) return true ;
					}
				} 
				try {
					eventType = parser.next();
				} catch (XmlPullParserException e) {
					return false;
				}
			}
		} catch (Throwable t) {
			return false;
		}
		return false;
	}

	public static void setLock() {
		locked = !locked;
	}

	public static void setSpeach_descr(boolean isChecked) {
		speach_descr = isChecked;
	}

	public static boolean isSpeach_descr() {
		return speach_descr;
	}

	public static boolean isLocked() {
		return locked;
	}

	public static void setExpand_text(boolean isChecked) {
		expand_text = isChecked;
	}

	public static boolean isExpand_text() {
		return expand_text;
	}

	public static void setMusic_quieter(boolean isChecked) {
		music_quieter = isChecked;
	}

	public static void setRestsDuration(int duration) {
		for (Entry<Record, List<Record>> entry : listDataChild.entrySet())
			for (Record r : entry.getValue())
				if (r.isRest()) {
					r.setDuration(duration);

				}
		summDurations(null);
	}

	public static int getIndex(Record newRecord) {
		int index = -1;

		for (Record r : listDataHeader) {
			index++;
			if (r == newRecord)
				return index;

			List<Record> l = listDataChild.get(r);
			if (l != null) {
				for (Record p : l) {
					index++;
					if (r == p)
						return index;
				}
			}

		}

		return index;
	}

	public static void clear() {
		main = new Record("New programm");
		soundNextGroupOn = false;
		soundNextName = "";
		soundNextGroupUri = "";
		countBeforeTheEnd = true;
		playMusic = false;
		pathToMp3 = "";
		music_quieter = true;
		expand_text = false;
		speach_descr = true;
		locked = false;

		listDataHeader = new ArrayList<Record>();
		listDataChild = new HashMap<Record, List<Record>>();

	}

	public static String getPathToMp3() {
		return pathToMp3;
	}

	public static boolean isPlayMusicOn() {
		return playMusic;
	}

	public static boolean isMusic_quieter() {
		return music_quieter;
	}

	public static boolean isCountBeforeTheEndOn() {

		return countBeforeTheEnd;
	}

	public static boolean isSoundNextGroupOn() {
		return soundNextGroupOn;
	}

	/*
	 * public static Record getNextRecord() { // TODO: Implement this method
	 * return null; }
	 * 
	 * public static Record getFirstRecord() {
	 * 
	 * return null; }
	 * 
	 * public static Record getCurrentRecord() { return currentRecord; }
	 */
	public static ArrayList<Record> getGroups() {
		return listDataHeader;
	}

	public static HashMap<Record, List<Record>> getChilds() {
		return listDataChild;
	}

	public static ArrayList<Record> getList() {

		ArrayList<Record> list = new ArrayList<Record>();

		for (Record r : listDataHeader) {
			boolean child = false;
			List<Record> l = listDataChild.get(r);
			if (l != null) {
				if (l.size() == 0)
					list.add(r);
				else {
					for (Record p : l)
						list.add(p);
				}
			} else {
				list.add(r);
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
		Record parent = null;

		listDataHeader.remove(selectedRecord);

		if (listDataChild.get(selectedRecord) != null)
			listDataChild.remove(selectedRecord);
		else {
			for (Entry<Record, List<Record>> entry : listDataChild.entrySet())
				for (Record r : entry.getValue())
					if (r == selectedRecord) {
						parent = entry.getKey();
						entry.getValue().remove(r);
						break;
					}

		}

		summDurations(parent);
	}

	public static Record addCHildRecord(Record selectedRecord) {
		Record record = new Record("New record");

		if (listDataChild.get(selectedRecord) == null) {
			ArrayList<Record> newArray = new ArrayList<Record>();
			newArray.add(record);
			listDataChild.put(selectedRecord, newArray);
		} else {
			listDataChild.get(selectedRecord).add(record);
		}

		return null;
	}

	public static File save(Context mContext, String fileName) {
		summDurationsAll();

		File file = new File(Utils.APP_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		file = new File(Utils.APP_FOLDER, fileName);
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));

			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
			writer.write("<body type = \"fpprogramm\">" + "\n");

			writer.write("<main text=\"" + main.getText()  
					+ "\"" + " duration=\""
					+ main.getDuration() + "\"" + " soundNextGroupOn=\""
					+ soundNextGroupOn + "\"" + " soundNextName=\""
					+ soundNextName + "\"" + " soundNextGroupUri=\""
					+ soundNextGroupUri + "\"" + " countBeforeTheEnd=\""
					+ countBeforeTheEnd + "\"" + " pathToMp3=\"" + pathToMp3
					+ "\"" + " playMusic=\"" + playMusic + "\""
					+ " music_quieter=\"" + music_quieter + "\""
					+ " expand_text=\"" + expand_text + "\""
					+ " speach_descr=\"" + speach_descr + "\""					
					+ " locked=\""+ locked + "\""
					+ ">" + "\n");
			writer.write("</main>" + "\n");

			for (Record r : listDataHeader) {
				
					writer.write(
							"<record name=\"" + r.getName() + "\""
									+ " text=\"" + r.getText() + "\"" 
									+ r.getIdString()   
									+ " duration=\""
									+ r.getDuration() + "\"" + ">" + "\n");
				
				if (listDataChild.get(r) != null) {
					writer.write("<children>" + "\n");
					for (Record l : listDataChild.get(r)) {
						writer.write("<record name=\"" + l.getName() + "\""
								+ " text=\"" + l.getText() + "\"" 
								+ " rest=\""+ l.isRest() + "\"" 
								+ " weight=\""+ l.isWeight() + "\"" 
								+ l.getIdString()   
								+ " duration=\""+ l.getDuration() + "\"" + ">" + "\n");
						writer.write("</record>" + "\n");
					}
					writer.write("</children>" + "\n");
				}
				writer.write("</record>" + "\n");
			}

			writer.write("</body>" + "\n");

			writer.close();
			// Toast.makeText(mContext,
			// mContext.getResources().getString(R.string.file_saved)+" "+fileName,
			// Toast.LENGTH_LONG)
			// .show();
		} catch (IOException e) {
			// Utils.setInformation(context.getResources().getString(R.string.error_save_file)+" "+e);
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.error_saving_file)
							+ " " + e, Toast.LENGTH_LONG).show();
			return null;
		}
		return file;
	}

	public static Record getMainRecord() {
		return main;
	}

	public static void summDurations(Record record) {

		if (record != null) {
			for (Entry<Record, List<Record>> entry : listDataChild.entrySet()) {
				if (entry.getValue().contains(record) || record == null) {
					Record group = entry.getKey();
					long duration = 0;

					for (Record r : entry.getValue())
						duration = duration + r.getDuration();
					group.setDuration(duration);
				}
			}
		}
		//
		// Calculate total duration
		//
		long duration = 0;
		for (Record group : listDataHeader) 
				duration = duration + group.getDuration();
		main.setDuration(duration);
	}

	public static void summDurationsAll() {

		for (Record group : listDataHeader) {
			long duration = 0;
			int childCount = 0;

			if (listDataChild.get(group) != null)
				for (Record r : listDataChild.get(group)) {
					duration = duration + r.getDuration();
					childCount++;
				}

			if (childCount > 0)
				group.setDuration(duration);
		}
		//
		// Calculate total duration
		//
		long duration = 0;
		for (Record group : listDataHeader) 
				duration = duration + group.getDuration();
		main.setDuration(duration);
	}

	public static Record addRecord(Record newRecord, Record currentRecord) {
		if (newRecord == null)
			newRecord = new Record("new record");

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
			for (Entry<Record, List<Record>> entry : listDataChild.entrySet())
				if (entry.getValue().contains(currentRecord))
					entry.getValue().add(
							entry.getValue().indexOf(currentRecord) + 1,
							newRecord);

		}

		return newRecord;
	}

	public static Record copyRecord(Record currentRecord) {

		Record record = new Record(currentRecord);

		if (listDataHeader.indexOf(currentRecord) > 0) {
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

		for (Record o : listDataHeader) {
			List<Record> entry = listDataChild.get(o);
			if (entry != null)
				for (Record r : entry) {
					if (r == record)
						return time;
					else
						time = time + r.getDuration();
				}

		}

		return time;
	}

	public static Record pasteRecord(Record copyRecord, Record selectedRecord) {

		Record record = new Record(copyRecord);

		if (listDataHeader.indexOf(selectedRecord) >= 0) {
			listDataHeader.add(listDataHeader.indexOf(selectedRecord) + 1,
					record);
		} else {
			for (Entry<Record, List<Record>> entry : listDataChild.entrySet())
				if (entry.getValue().contains(selectedRecord))
					entry.getValue().add(
							entry.getValue().indexOf(selectedRecord) + 1,
							record);

		}

		return record;
	}

	public static boolean loadXMLInfo( String fileName, PFile pFile) {

		try {
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(reader);

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("main")){
						pFile.setLocked(Boolean.parseBoolean(parser.getAttributeValue(null, "locked")));
						pFile.setInfo(parser.getAttributeValue(null, "text"));
						pFile.setDuration(0);
						if (parser.getAttributeValue(null, "duration") != null)
							pFile.setDuration(Integer.parseInt(parser
									.getAttributeValue(null, "duration")));
						return true;
					}
				}
				try {
					eventType = parser.next();
				} catch (XmlPullParserException e) {
				}
			}
		} catch (Throwable t) {
		}
		
		return false;
	}

	public static long getDurationRest(Record record) {
		long past = 0;

		for (Record r : listDataHeader) {

			List<Record> l = listDataChild.get(r);
			if (l != null) {
				for (Record p : l) {
					if (p == record)
						return main.getDuration() - past;
					else {
						past = past + p.getDuration();
					}
				}
			} else {
				if (r == record)
					return main.getDuration() - past;
				else {
					past = past + r.getDuration();
				}
			}
		}

		return 0;
	}
	
}
