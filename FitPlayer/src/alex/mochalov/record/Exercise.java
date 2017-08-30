package alex.mochalov.record;

import java.util.*;

public class Exercise
{
	protected String mName;
	protected String mText;
	protected UUID UUID; 
	protected boolean mRest;
	protected boolean mWeight;
	
	public Exercise(String name, String text, Boolean rest, UUID uuid, Boolean weight) {
		mName = name;
		mText = text;
		UUID = uuid;
		mRest = rest;
		mWeight = weight;
	}
	
	public Exercise() {
		
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
	
	public String getText() {
		return mText;
	}
	
	public UUID getID() {
		//return UUID.randomUUID();
		return UUID;
	}

	public void setID(UUID uuid) {
		UUID = uuid;		
	}
	
}
