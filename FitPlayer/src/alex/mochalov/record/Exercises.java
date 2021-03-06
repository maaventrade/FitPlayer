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
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import alex.mochalov.fitplayer.R;
import alex.mochalov.main.Utils;
import android.content.Context;
import android.os.BaseBundle;
import android.util.Log;
import android.widget.Toast;

public class Exercises {
	private static ArrayList<Exercise> exercises = new ArrayList<Exercise>();

	public static String getText(UUID id)
	{
		for (Exercise r: exercises)
			if (r.getUUID().equals(id))
				return r.getText();

		return "";
		
	}
	
	public static void addExercise(Exercise exercise)
	{
		exercises.add(exercise);
		Utils.sortR(exercises);
	}
	
	/**
	 * 
	 * @param mContext
	 * 
	 *  Load exercises from the all xml files to list "exercises"  
	 */
	public static void loadXMLrecords(Context mContext) {

		exercises.clear();
		
		File dir = new File(Utils.APP_FOLDER + "/");
		File[] files = dir.listFiles();

		if (files != null)
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(".xml")) {
					try {
						String name = Utils.APP_FOLDER + "/"
								+ files[i].getName();

						BufferedReader reader;
						BufferedReader rd = new BufferedReader(
								new InputStreamReader(new FileInputStream(name)));

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

						XmlPullParserFactory factory = XmlPullParserFactory
								.newInstance();
						factory.setNamespaceAware(true);
						XmlPullParser parser = factory.newPullParser();

						parser.setInput(reader);

						int eventType = parser.getEventType();
						while (eventType != XmlPullParser.END_DOCUMENT) {
							if (eventType == XmlPullParser.START_TAG) {
								if (parser.getName().equals("record")) {

									int duration = 0;
									if (parser.getAttributeValue(null,
											"duration") != null)
										duration = Integer.parseInt(parser
												.getAttributeValue(null,
														"duration"));
									
									String group = parser.getAttributeValue(null, "isGroup");

									Record record = new Record(
											parser.getAttributeValue(null,
													"name"),
											parser.getAttributeValue(null,
													"text"),
											Boolean.parseBoolean(parser
													.getAttributeValue(null,
															"rest")), duration,
											Boolean.parseBoolean(parser
														.getAttributeValue(null,
																"weight")),
											Boolean.parseBoolean("false")
										);

									if (record.getText() != null
											&& record.getText().length() > 0) {
										boolean found = false;
										for (Exercise r : exercises)
											if (r.mName.equals(record.getName())) {
												found = true;
												break;
											}

										if (!found){
											Exercise exercise = new Exercise();
											exercise.setName(record.getName());
											exercise.setText(record.getText());
											exercise.setRest(record.isRest());
											exercise.setWeight(record.isWeight());
											exercises.add(exercise);
										}
									}

								}
							}

							try {
								eventType = parser.next();
							} catch (XmlPullParserException e) {
								Log.d("", e.toString());
							}
						}
					} catch (Throwable t) {
					}
				}
			}
		Utils.sortR(exercises);
	}

	
	public static void replaceRecords(Context mContext) {
	}
	
	/**
	 * 
	 * @param mContext
	 * 
	 * Load full list of the exercises
	 */
	public static void loadExercises(Context mContext) {
		exercises.clear();
 
		try {
			String name = Utils.APP_FOLDER + "/records.xml";

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
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("record")) {

						int duration = 0;
						if (parser.getAttributeValue(null, "duration") != null)
							duration = Integer.parseInt(parser
									.getAttributeValue(null, "duration"));

						String strUUID = parser.getAttributeValue(null, "id");
						UUID uuid;
						if (strUUID.equals("null"))
							uuid = null;
						else uuid = UUID.fromString(strUUID);
						
						Exercise exercise = new Exercise(
								parser.getAttributeValue(null, "name"), 
								parser.getAttributeValue(null, "text"), 
								Boolean.parseBoolean(parser.getAttributeValue(null, "rest")), 
								uuid,
								Boolean.parseBoolean(parser.getAttributeValue(null, "weight")));

						exercises.add(exercise);

					}
				}

				try {
					eventType = parser.next();
				} catch (XmlPullParserException e) {
					Log.d("", e.toString());
				}
			}
		} catch (Throwable t) {
			Log.d("", t.toString());
		}

		Utils.sortR(exercises);
	}

	public static ArrayList<Exercise> getExercises() {

		return exercises;
	}

	public static boolean SaveExercises(Context mContext) {
		try {
			File file = new File(Utils.APP_FOLDER);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(Utils.APP_FOLDER, "records.xml");

			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));

			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
			writer.write("<body type = \"records\">" + "\n");

			for (Exercise r : exercises) {
				writer.write("<record name=\"" + r.getName() + "\""
						+ " text=\"" + r.getText() + "\"" + " rest=\""
						+ r.isRest() + "\"" + " id=\"" + r.getUUID() + "\""
						+ ">\n");
				writer.write("</record>" + "\n");
			}

			writer.write("</body>" + "\n");
			writer.close();
		} catch (IOException e) {
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.error_saving_file)
							+ " " + e, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public static UUID findRecord(String text) {
		for (Exercise r: exercises)
			if (r.getText().equals(text))
				return r.getUUID();
		
		return null;
	}
	public static Exercise getRecordByID(UUID uuid) {
		for (Exercise r: exercises)
			if (r.getUUID().equals(uuid))
				return r;
		
		return null;
	}


}
