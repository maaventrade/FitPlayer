package alex.mochalov.calendar;
import alex.mochalov.fitplayer.R;
import alex.mochalov.main.Utils;
import alex.mochalov.programm.Prog;
import alex.mochalov.record.*;
import android.content.Context;
import android.net.ParseException;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class CalendarData
{
	static HashMap<Date, ArrayList<Prog>>  hm = new HashMap<Date, ArrayList<Prog>>();

	public static int getProgsCount(Date date)
	{
		if (hm.get(date) == null)
			return 0;
		else return hm.get(date).size();
		
	}

	public static ArrayList<Prog> get(Date date)
	{
		if (hm.get(date) == null)
			return new ArrayList<Prog>();
		else return new ArrayList<>(hm.get(date));
	}

	public static void replace(Date date, ArrayList<Prog> programms)
	{
		hm.put(date, new ArrayList<>(programms));
	}

	public static boolean save(Context mContext) {
		try {

			File file = new File(Utils.APP_FOLDER);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(Utils.APP_FOLDER, "calendarData.xml");

			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));

			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
			writer.write("<body>" + "\n");

			
			Iterator<Entry<Date, ArrayList<Prog>>> it = hm.entrySet().iterator();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  

			while (it.hasNext()) {
		        Entry<Date, ArrayList<Prog>> pair = (Entry<Date, ArrayList<Prog>>)it.next();

				writer.write("<record date=\"" + format.format(pair.getKey()) + "\">" + "\n");
				
				for (Prog p : pair.getValue()) {
					writer.write("<prog name = \"" + p.getName() + "\"" 
							+ " completed = \"" + p.isCompleted() + "\" ></prog>" 
							+ "\n");
				}
				
				writer.write("</record>" + "\n");
		        
		        // it.remove(); // avoids a ConcurrentModificationException
		    }
		    
			writer.write("</body>" + "\n");
			writer.close();
		} catch (IOException e) {
			// Utils.setInformation(context.getResources().getString(R.string.error_save_file)+" "+e);
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.error_saving_file)
							+ " " + e, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public static boolean loadXML(Context mContext) {

		hm.clear();
		
		Date date = null;
		ArrayList<Prog> prog = new ArrayList<Prog>();

		try {

			String name = Utils.APP_FOLDER + "/calendarData.xml";

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
					if (parser.getName().equals("record")) 
						hm.put(date, new ArrayList<>(prog));
				} else if (eventType == XmlPullParser.START_TAG) {

					if (parser.getName() == null)
						;
					else if (parser.getName().equals("record")) {

						prog.clear();

						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd");
						try {
							date = format.parse(parser.getAttributeValue(null,
									"date"));
						} catch (ParseException e) {
							date = null;
							Toast.makeText(mContext, e.toString(),
									Toast.LENGTH_LONG).show();
						}

					} else if (parser.getName().equals("prog")) {
						if (date != null) {
							prog.add(new Prog(parser.getAttributeValue(null,
									"name"), Boolean.parseBoolean(parser
									.getAttributeValue(null, "completed"))));
						}
					}
				}
				try {
					eventType = parser.next();
				} catch (XmlPullParserException e) {
					Toast.makeText(
							mContext,
							e.toString(), Toast.LENGTH_LONG).show();
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

	public static String getText(Date date) {
		String s = "";
		
		if (hm.get(date) != null)
			for (Prog p: hm.get(date))
				s = s + p.getName().substring(0, p.getName().lastIndexOf('.')) + ",";
			
		
		
		return s;
	}

	public static boolean isCompleted(Date date) {
		boolean result = true;
		
		if (hm.get(date) != null)
			for (Prog p: hm.get(date))
				result = result & p.isCompleted();
		else result = false;
		
		return result;
	}

}
