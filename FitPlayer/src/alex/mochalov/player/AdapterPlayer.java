package alex.mochalov.player;

import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class AdapterPlayer extends BaseAdapter {

	private LayoutInflater inflater;
	
	private ArrayList<Record> objects;
	private Context mContext;

	private boolean mEnabled;
	
	AdapterPlayer(Context context, ArrayList<Record> records) {

		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		objects = records;
		//records = mainFolder.getRecords(); 
		mContext = context;
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
		
		ImageView imgWeight = (ImageView) convertView
			.findViewById(R.id.imgWeight);

		if (record.isWeight())
			imgWeight.setImageDrawable(mContext.getResources().getDrawable(R.drawable.weight));
		else
			imgWeight.setImageDrawable(mContext.getResources().getDrawable(R.drawable.void1));
		
		
		return convertView;
	}
}
