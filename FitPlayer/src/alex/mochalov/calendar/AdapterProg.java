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

	private Activity mContext;

	private Record mainFolder;
	private ArrayList<Prog> mObjects;

	public interface OnButtonClickListener {
		public void onEdit(String text);
		public void onAdd(String text);
	}
	public OnButtonClickListener listener;

	AdapterProg(Activity context, ArrayList<Prog> objects) {
		mContext = context;
		inflater = (LayoutInflater)context
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
			convertView = inflater.inflate(R.layout.item_files, null);
		}


		TextView textViewName = (TextView)convertView.findViewById(R.id.TextViewName);
		TextView TextViewDate = (TextView)convertView.findViewById(R.id.TextViewDate);
		ImageView ivLocked =  (ImageView)convertView.findViewById(R.id.ivLocked);
		TextView tvDuration = (TextView)convertView.findViewById(R.id.tvDuration);

		ImageButton brnGo = (ImageButton)convertView.findViewById(R.id.imageButtonGo);
		brnGo.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {

					FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

					FragmentPlayer fragmentPlayer = new FragmentPlayer(mContext);

					Bundle args = new Bundle();
					args.putString("name", (String)getItem(position));
					fragmentPlayer.setArguments(args);

					ft.replace(R.id.frgmCont, fragmentPlayer, FragmentPlayer.TAG_FRAGMENT_PLAYER);
					ft.addToBackStack(null);

					ft.commit();

				}});

/*
    	textViewName.setText(mObjects.get(position));
    	TextViewDate.setText(Utils.getFileDateTime(mObjects.get(position), mContext));

    	FileData fileData = Utils.getFileData(mContext, mObjects.get(position));
    	tvDuration.setText(Utils.MStoString(fileData.duration));

		if (fileData.locked)
			ivLocked.setImageDrawable(mContext.getResources().getDrawable(R.drawable.lock));
		else
			ivLocked.setImageDrawable(mContext.getResources().getDrawable(R.drawable.void1));
*/
		return convertView;
	}
}
