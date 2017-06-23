package alex.mochalov.record;

import java.io.Writer;
import java.util.ArrayList;

public class Folder extends RecObject{

	private ArrayList<RecObject> children;
	private int mIndex = -1;

	public Folder(String name) {
		super(name);
		children = new ArrayList<RecObject>(); 
	}

	public void addRecord(String name, String text, int duration) {
		children.add( new Record(name, text, duration) );
		
	}

	public Record getNextRecord() {
		mIndex++;
		if (mIndex < children.size()) return (Record)children.get(mIndex);
		else return null;
	}

	public void reset() {
		mIndex = -1;
	}

	public RecObject getFirstRecord() {
		mIndex = 0;
		if (mIndex < children.size()) return children.get(mIndex);
		else return null;
	}

	public int getCount() {
		
		int count = 0;
		for (RecObject r: children){
			if (r.getClass() == Record.class)
				count++;
			else if (r.getClass() == Folder.class)
				count = count + ((Folder)r).getCount();
		}
		
		return count;
	}

	public ArrayList<Record> getRecords() {
		ArrayList<Record> records = new ArrayList<Record>();
		
		for (RecObject r: children){
			if (r.getClass() == Record.class)
				records.add((Record)r);
			else if (r.getClass() == Folder.class){
				ArrayList<Record> subRecords = ((Folder)r).getRecords();
				for (Record s: subRecords)
					records.add(s);
			}
		}
		
		return records;
	}

	public int getIndex() {
		return mIndex;
	}

	public Record setRecord(int index) {
		mIndex = index;
		return (Record)children.get(mIndex);
	}

	public Record getRecord() {
		if (mIndex < children.size() && mIndex > -1) return (Record)children.get(mIndex);
		else return null;
	}



}
