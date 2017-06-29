package alex.mochalov.player;

import java.util.ArrayList;

import alex.mochalov.fitplayer.R;

import alex.mochalov.record.Record;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterPlayer extends BaseAdapter {

	private LayoutInflater inflater;
	
	private ArrayList<Record> objects;

	private boolean mEnabled;
	
	AdapterPlayer(Context context, ArrayList<Record> records) {

		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		objects = records;
		//records = mainFolder.getRecords(); 

		mEnabled = true;
	}

	public void setEnabled(boolean enabled) 
    { 
		mEnabled = enabled; 
    } 
	
	@Override
	public boolean isEnabled(int position) 
    { 
		return mEnabled; 
    } 
	
	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			convertView = inflater.inflate(R.layout.item_player, null);
		}
		Record record = objects.get(position);
				
		TextView textViewName = (TextView)convertView.findViewById(R.id.TextViewName);
		TextView textViewText = (TextView)convertView.findViewById(R.id.TextViewText);
		
    	textViewName.setText(record.getName());
    	textViewText.setText(record.getText());
		
		return convertView;
	}
}