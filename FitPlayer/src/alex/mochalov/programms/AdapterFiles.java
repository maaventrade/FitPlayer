package alex.mochalov.programms;

import java.util.ArrayList;

import alex.mochalov.record.Folder;
import alex.mochalov.record.Record;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import alex.mochalov.fitplayer.*;

public class AdapterFiles extends BaseAdapter {

	private LayoutInflater inflater;
	
	private Folder mainFolder;
	private ArrayList<String> mObjects;

	AdapterFiles(Context context, ArrayList<String> objects) {

		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mObjects = objects;
	}
	
	AdapterFiles() {

		
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			convertView = inflater.inflate(R.layout.item_files, null);
		}
				
		TextView textViewName = (TextView)convertView.findViewById(R.id.TextViewName);
		//TextView textViewText = (TextView)convertView.findViewById(R.id.TextViewText);
		
    	textViewName.setText(mObjects.get(position));
		
		return convertView;
	}
}
