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
	private boolean mRest;

	public Record(String name, String text, Boolean rest, int duration) {
		mName = name;
		mText = text;
		mDuration = duration;
		mRest = rest;

		modifications = new ArrayList<Record>();
	}
	
	public Record(Record record) {
		mName = record.mName;
		mText = record.mText;
		mDuration = record.mDuration;
		mRest = record.mRest;

		//modifications = new ArrayList<Record>();
	}
	
	public Record(String name) {
		mName = name;
		
		modifications = new ArrayList<Record>();
	}

	public void copy()
	{
		Utils.setCopyRecord(this);
	}

	public void log()
	{
//		Log.d("","name "+mName);
//		for( Record r:children)
//			r.log();
	}
	
	public void setName(Editable text)
	{
		mName = text.toString();
	}

	public String getName() {
		return mName;
	}

	public boolean isRest() {
		return mRest;
	}

	public void setRest(boolean rest) {
		mRest = rest;
	}
	
	public long getDuration() {
		return mDuration;
	}

	public String getText() {
		return mText;
	}

	public void setText(Editable text) {
		mText = text.toString();
	}

	public void setText(String text) {
		mText = text;
	}

	public void setDuration(Editable text) {
		String s = text.toString();
		
		try {
			mDuration = 
					Integer.parseInt(
					s.substring(0, 2)) * 60 * 1000 +
					Integer.parseInt(
							s.substring(3, 5)) * 1000;
		} catch (NumberFormatException e) {
			mDuration = 0;
		} catch (IndexOutOfBoundsException e) {
			mDuration = 0;
		}
		 
		
	}

	public void setDuration(long duration) {
		mDuration = duration;
	}
}
