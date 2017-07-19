package alex.mochalov.editor;

import alex.mochalov.editor.*;
import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.player.FragmentPlayer;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import java.util.*;

public class AdapterSelectRecord extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<Record> objects;
	
	public interface OnButtonClickListener {
		public void onEdit(String text);
		public void onAdd(String text);
	}
	
	public OnButtonClickListener listener;

	AdapterSelectRecord(Context context, ArrayList<Record> obj) {
		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		objects = obj;
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
			convertView = inflater.inflate(R.layout.item_select, null);
		}
		Record record = objects.get(position);
				
		TextView textViewName = (TextView)convertView.findViewById(R.id.TextViewName);
		TextView textViewText = (TextView)convertView.findViewById(R.id.TextViewText);
		
    	textViewName.setText(record.getName());
    	textViewText.setText(record.getText());
		
		return convertView;
	}
}
