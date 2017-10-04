package alex.mochalov.main;

import alex.mochalov.files.AdapterFiles;
import alex.mochalov.fitplayer.R;
import alex.mochalov.programm.FileData;
import alex.mochalov.programm.Programm;
import alex.mochalov.record.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.util.*;
import android.webkit.*;
import android.widget.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.xmlpull.v1.*;

class FileNameComparator implements Comparator<String> {   
	public int compare(String fileA, String fileB) {
		return fileA.compareToIgnoreCase(fileB);
	}
}

class RecordNameComparator implements Comparator<Exercise> {   
	public int compare(Exercise a, Exercise b) {
		return a.getName().compareToIgnoreCase(b.getName());
	}
}

public class Utils {

	static final String PROGRAMM_FOLDER = "fitPlayer";
	static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
	public final static String APP_FOLDER = EXTERNAL_STORAGE_DIRECTORY+"/xolosoft/"+PROGRAMM_FOLDER;
	static String REC_FOLDER = APP_FOLDER+"/rec";
	
	private static String language = "rus";
	
	public static String action = "";
	
	private static String mFileName;

	private static Record copyRecord = null;
	
	public static void setCopyRecord(Record record)
	{
		copyRecord = new Record(record);
	}
	
	public static Record getCopyRecord(){
		return copyRecord;
	}
	
	public static boolean rename(Context mContext, String from, String to)
	{
		File src = new File(APP_FOLDER+"/"+from);
		File dst = new File(APP_FOLDER+"/"+to);
		Toast.makeText(mContext,from,Toast.LENGTH_LONG).show();
		Toast.makeText(mContext,to,Toast.LENGTH_LONG).show();
		src.renameTo(dst);
		
		return true;
	}

	public static String trimExt(String str)
	{
		int i = str.lastIndexOf(".");
		if (i > 0)
			return str.substring(0, i);
		else return str;
	}

	public static boolean copy(String from, String to) {
		File src = new File(APP_FOLDER+"/"+from);
		File dst = new File(APP_FOLDER+"/"+to);
		
		
		try {
		InputStream in = new FileInputStream(src);
		
	
			OutputStream out = new FileOutputStream(dst);
			
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			
				out.close();
	
			in.close();
		} catch(IOException e){
			return false;
		}
		return true;
	}

	public static Date getFileDateTime(String selectedString)
	{
		File file = new File(APP_FOLDER+"/"+selectedString);
		Date date  =  new Date(file.lastModified());
		return date;
	}
	
	public static String getFileName(){
		return mFileName;
	}
	
	public static void setFileName(String fileName){
		mFileName = fileName;
	}

	public static void deleteFile(String selectedString)
	{
		File file = new File(APP_FOLDER+"/"+selectedString);
		file.delete();
	}
	
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


	
	public static void sort(ArrayList<String> programms) {
		
		FileNameComparator fnc = new FileNameComparator();
        Collections.sort(programms, fnc);
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

	public static void sortR(ArrayList<Exercise> records) {

		RecordNameComparator fnc = new RecordNameComparator();
        Collections.sort(records, fnc);
	}

	public static FileData getFileData(Activity mContext, String string) {
		FileData fileData = new FileData();
		Programm.loadXMLInfo(mContext, string, fileData);
		
		return fileData;
	}



	public static String archive(ArrayList<String> files, String path) {
		int BUFFER = 1000;
		
		Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String formattedDate = df.format(c.getTime());
		String zipName = path+"/"+formattedDate+".zip";
		
		  
		try  { 
			  BufferedInputStream origin = null; 
			  FileOutputStream dest = new FileOutputStream(new File(zipName)); 

			  ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest)); 

			  byte data[] = new byte[BUFFER]; 

			  for(int i = 0; i < files.size(); i++) { 
				  FileInputStream fi = new FileInputStream(APP_FOLDER + "/" + files.get(i)); 
			    
				  origin = new BufferedInputStream(fi, BUFFER); 
			    
				  ZipEntry entry = new ZipEntry(files.get(i)); 
				  
				  out.putNextEntry(entry); 
			    
				  int count; 
				  while ((count = origin.read(data, 0, BUFFER)) != -1) { 
					  out.write(data, 0, count); 
				  } 
				  origin.close(); 
			  } 

			  out.close(); 
			} catch(Exception e) { 
				return e.toString();
			} 		
		return "Created "+zipName;
	}

	public static boolean extract(final Activity mContext, final String path, final ArrayList<String> files, final AdapterFiles adapter) {
		
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, 
				android.R.layout.select_dialog_singlechoice);
		
		try
	    {
			FileInputStream is = new FileInputStream(path);
	        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));

	        ZipEntry ze;

	        while ((ze = zis.getNextEntry()) != null)
	        	arrayAdapter.add(ze.getName());
	    } 

	    catch (FileNotFoundException e) { 
			Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
			return false;
	    }
	    catch (IOException e) 
		{ 
			Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
			return false;
	    }
	
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
		builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Select One Name:-");

		builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		            }
		        });

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                String strName = arrayAdapter.getItem(which);
		        		try
		        	    {
		        			FileInputStream is = new FileInputStream(path);
		        	        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
		        	        OutputStream out = new FileOutputStream(APP_FOLDER+"/"+strName);

		        	        ZipEntry ze;

		        	        while ((ze = zis.getNextEntry()) != null)
		        	            if (ze.getName().equals(strName)){

		        	            	byte[] buffer = new byte[9000];
			                        int len;
			                        while ((len = zis.read(buffer)) != -1) {
			                            out.write(buffer, 0, len);
			                        }
			                        out.close();
			                        break;
			                        
		        	            }
		        	        is.close();
		        	    } 
		        	    catch (FileNotFoundException e) { 
		        			Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
		        			return;
		        	    }
		        	    catch (IOException e) 
		        		{ 
		        			Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
		        			return;
		        	    }
		        		
						///      1111111111111111  Utils.readFilesList(files);
						adapter.notifyDataSetChanged();
						Toast.makeText(mContext, 
						"Ok", 
						Toast.LENGTH_LONG).show();
		        		
		        		
		            }
		        });
		builderSingle.show();		
		
		return true;
	}

	
}

//private ArrayList<Record> records = new ArrayList<Record>(); 
//Programm.loadXMLrecords(mContext, records);

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
