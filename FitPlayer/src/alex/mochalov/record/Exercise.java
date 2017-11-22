package alex.mochalov.record;

import java.util.*;
import android.text.*;

public class Exercise
{

	String mName;
	boolean mRest;
	boolean mWeight;
	
	private String mText;
	private UUID UUID; 
	
	public Exercise(String name, String text, Boolean rest, UUID uuid, Boolean weight) {
		mName = name;
		mText = text;
		UUID = uuid;
		mRest = rest;
		mWeight = weight;
	}
	
	public Exercise() {
		mName = "";
		mText = "";
		UUID =  java.util.UUID.randomUUID();
	}

	public void setText(String text)
	{
		mText = text;
	}

	public void setName(String name)
	{
		mName = name;
	}
	
	public String getText() {
		return mText;
	}
	
	
	public UUID getUUID() {
		return UUID;
	}

	public void setID(UUID uuid) {
		UUID = uuid;		
	}
	
	public String getName() {
		return mName;
	}

	public boolean isRest() {
		return mRest;
	}

	public void setRest(Boolean rest) {
		mRest = rest;
	}

	public void setWeight(Boolean weight) {
		mWeight = weight;
	}
	
	public boolean isWeight() {
		return mWeight;
	}

}
