package alex.mochalov.record;

import java.util.*;

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
		
	}
	
	public String getText() {
		return mText;
	}
	
	
	public UUID getID() {
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

	public boolean isWeight() {
		return mWeight;
	}	
}
