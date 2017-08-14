package alex.mochalov.calendar;

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

public class AdapterProg extends BaseAdapter {
	private LayoutInflater inflater;

	private Context mContext;

	private Record mainFolder;
	private ArrayList<Prog> mObjects;

	public interface OnButtonClickListener {
		public void onEdit(String text);
		public void onAdd(String text);
	}
	public OnButtonClickListener listener;

	AdapterProg(Context mContext2, ArrayList<Prog> objects) {
		mContext = mContext2;
		inflater = (LayoutInflater)mContext2
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mObjects = objects;
	}

	AdapterProg() {
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
			convertView = inflater.inflate(R.layout.item_prog, null);
		}


		TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
		ImageView ivCompleted =  (ImageView)convertView.findViewById(R.id.ivCompleted);


		Prog prog = mObjects.get(position);
		
		tvName.setText(prog.getName());

		if (prog.isCompleted())
			ivCompleted.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ok));
		else
			ivCompleted.setImageDrawable(mContext.getResources().getDrawable(R.drawable.void1));

		return convertView;
	}
}
