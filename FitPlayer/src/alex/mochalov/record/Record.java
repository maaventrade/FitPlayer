package alex.mochalov.record;

import java.util.ArrayList;

public class Record extends RecObject{

	private String mText;
	private long mDuration;
	private ArrayList<RecObject> modifications;
	
	public Record(String name, String text, int duration) {
		super(name);
		mText = text;
		mDuration = duration;
		
		modifications = new ArrayList<RecObject>();
	}
	
	public long getDuration() {
		return mDuration;
	}

	public String getText() {
		return mText;
	}


}
