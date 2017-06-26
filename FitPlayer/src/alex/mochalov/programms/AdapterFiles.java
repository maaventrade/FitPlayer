package alex.mochalov.programms;

import java.util.ArrayList;

import alex.mochalov.record.Record;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import alex.mochalov.fitplayer.*;

public class AdapterFiles extends BaseAdapter {

	private LayoutInflater inflater;
	
	private Record mainFolder;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			convertView = inflater.inflate(R.layout.item_files, null);
		}
				
		final ListView listView = (ListView) parent;
		TextView textViewName = (TextView)convertView.findViewById(R.id.TextViewName);
		//TextView textViewText = (TextView)convertView.findViewById(R.id.TextViewText);
		
		ImageButton brnEdit = (ImageButton)convertView.findViewById(R.id.imageButtonEdit);
		brnEdit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				listView.performItemClick(listView.getChildAt(position), position, 
						listView.getItemIdAtPosition(position));
			}});
		
		ImageButton brnAdd = (ImageButton)convertView.findViewById(R.id.imageButtonAdd); 
		
    	textViewName.setText(mObjects.get(position));
		
		return convertView;
	}
}
