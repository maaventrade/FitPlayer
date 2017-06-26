package alex.mochalov.record;

import alex.mochalov.fitplayer.*;
import android.content.*;
import android.text.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class Record {

	protected String mName;
	private String mText;
	private long mDuration;
	private ArrayList<Record> modifications;
	private ArrayList<Record> children;
	private int mIndex = -1;

	public Record(String name, String text, int duration) {
		mName = name;
		mText = text;
		mDuration = duration;

		children = new ArrayList<Record>(); 
		modifications = new ArrayList<Record>();
	}
	
	public Record(String name) {
		mName = name;
		
		children = new ArrayList<Record>(); 
		modifications = new ArrayList<Record>();
	}

	public void log()
	{
		Log.d("","name "+mName);
		for( Record r:children)
			r.log();
	}
	
	public void setName(Editable text)
	{
		mName = text.toString();
	}

	public String getName() {
		return mName;
	}

	public boolean writeToFile(Context context, Writer writer, Record current) {

		try {

			if (current != null){
				if (current.getClass() == Record.class){
					writer.write("<record name=\""
								 +current.getName()+"\""
								 +" text=\""+current.getText()+"\""
								 +" duration=\""+current.getDuration()+"\""
								 +">"+"\n");
					writer.write("</record>"+"\n");
				}

				else {
					writer.write("<folder name=\""
								 +current.getName()+"\""
								 +">"+"\n");

					ArrayList<Record> subRecords = current.getRecords();
					for (Record s: subRecords)
						s.writeToFile(context, writer, s);

					writer.write("</folder>"+"\n");
				}
			}
		} catch (IOException e) {
			//Utils.setInformation(context.getResources().getString(R.string.error_save_file)+" "+e);
			Toast.makeText(context, context.getResources().getString(R.string.error_saving_file) +" "+e , Toast.LENGTH_LONG).show();
			return false;
		}
		return true;


	}
	
	
	
	
	public long getDuration() {
		return mDuration;
	}

	public String getText() {
		return mText;
	}

	public void addRecord(String name, String text, int duration) {
		children.add( new Record(name, text, duration) );

	}

	public void addRecord(Record record) {
		children.add(record);

	}
	
	
	public Record getNextRecord() {
		mIndex++;
		if (mIndex < children.size()) return (Record)children.get(mIndex);
		else return null;
	}

	public void reset() {
		mIndex = -1;
	}

	public Record getFirstRecord() {
		mIndex = 0;
		if (mIndex < children.size()) return children.get(mIndex);
		else return null;
	}

	public int getCount() {

		int count = 0;
		for (Record r: children){
	
				count++;
			
				count = count + r.getCount();
		}

		return count;
	}

	
	public ArrayList<Record> getRecords() {
		return children;
		/*
		ArrayList<Record> records = new ArrayList<Record >();

		for (Record r: children){
			
			records.add(r);
			
				ArrayList<Record> subRecords = r.getRecords();
				for (Record s: subRecords)
					records.add(s);
			
		}

		return records;
		*/
	}

	public int getIndex() {
		return mIndex;
	}

	public Record setRecord(int index) {
		mIndex = index;
		return children.get(mIndex);
	}

	public Record getRecord() {
		if (mIndex < children.size() && mIndex > -1) return (Record)children.get(mIndex);
		else return null;
	}
	
}
