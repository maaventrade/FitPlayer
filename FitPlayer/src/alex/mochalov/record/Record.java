package alex.mochalov.record;

import alex.mochalov.main.*;
import android.content.*;
import android.text.*;
import android.util.*;
import android.widget.*;

import java.io.*;
import java.util.*;

public class Record
{
	private String mName;
	private String mText;
	private boolean mRest;
	private boolean mWeight;
	
	private long mDuration;
//	private String mDetails = "";

	private Exercise mExercise;
	//private ArrayList<Record> modifications;
	
	public Record(String name, String text, Boolean rest, int duration, Boolean weight) {
		
		mName = name;
		mText = text;
		mDuration = duration;
		mRest = rest;
		mWeight = weight;
		
		//modifications = new ArrayList<Record>();
	}
	
	public Record(Record record) {
		
		mName = record.mName;
		mExercise = record.mExercise;
		mDuration = record.mDuration;
		mRest = record.mRest;
		mWeight = record.mWeight;
	}
	
	public Record(String name) {
		mName = name;
		
		//modifications = new ArrayList<Record>();
	}

	public Record(String name, String text, Boolean rest, Boolean weight) {
		mName = name;
		mText = text;
		mRest = rest;
		mWeight = weight;
		
		//modifications = new ArrayList<Record>();
	}
	

	public Record(String name, String text, Boolean rest, int duration, UUID uuid, Boolean weight) {
		mName = name;
		mExercise = Exercises.getRecordByID(uuid);
		mDuration = duration;
		mRest = rest;
		mWeight = weight;
		
		//modifications = new ArrayList<Record>();
	}

	public void setRec()
	{
		Log.d("a", mName);
		for (Exercise e:Exercises.getExercises()){
			if (e.getName().equals(mName)){
				mExercise = e;
				break;
			}
		}
	}

	public void setText(String text)
	{
		mText = text;
	}

	public void copy()
	{
		Utils.setCopyRecord(this);
	}

	public void log()
	{
	}
	
	
	public long getDuration() {
		return mDuration;
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

	public String getText() {
		if (mExercise != null)
			if (mText != null && mText.length() > 0)
				return mExercise.getText()+" "+mText;
			else
				return mExercise.getText();
		else
			return mText; 
	}


	public String getName() {
		return mName;
	}

	public boolean isRest() {
		return mRest;
	}

	public boolean isWeight() {
		return mWeight;
	}

	public void setName(String name) {
		mName = name;
		
	}

	public void setRest(boolean rest) {
		mRest = rest;
	}

	public void setID(UUID uuid) {
		getExercise().setID(uuid);
	}

	public void setWeight(boolean weight) {
		mWeight = weight;
	}

	public Exercise getExercise() {
		return mExercise;
	}	

	public String getIdString() {
		if (getExercise() == null || getExercise().getUUID() == null) 
			return "";
		else return " id=\"" + getExercise().getUUID() + "\"";
	}

	public UUID getUUID() {
		if (getExercise() == null || getExercise().getUUID() == null) 
			return null;
		else return getExercise().getUUID();
	}

	public void setExercise(Exercise exercise) {
		mExercise = exercise;
	}	
	
}
