package alex.mochalov.programms;

import alex.mochalov.editor.*;
import alex.mochalov.fitplayer.*;
import alex.mochalov.player.FragmentPlayer;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import java.util.*;

public class AdapterFiles extends BaseAdapter {

	private String TAG_FRAGMENT_EDITOR = "TAG_FRAGMENT_EDITOR";
	
	private LayoutInflater inflater;
	
	private Activity mContext;
	
	private Record mainFolder;
	private ArrayList<String> mObjects;
	
	public interface OnButtonClickListener {
		public void onEdit(String text);
		public void onAdd(String text);
	}
	public OnButtonClickListener listener;

	AdapterFiles(Activity context, ArrayList<String> objects) {
		mContext = context;
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
				
		
		TextView textViewName = (TextView)convertView.findViewById(R.id.TextViewName);
		//TextView textViewText = (TextView)convertView.findViewById(R.id.TextViewText);
		
		ImageButton brnEdit = (ImageButton)convertView.findViewById(R.id.imageButtonEdit);
		brnEdit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

				FragmentEditor fragmentEditor = new FragmentEditor(mContext);
				
				Bundle args = new Bundle();
			    args.putString("name", (String)getItem(position));
			    fragmentEditor.setArguments(args);

				ft.replace(R.id.frgmCont, fragmentEditor, TAG_FRAGMENT_EDITOR);
				ft.addToBackStack(null);

				ft.commit();
				
			}});
			
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
		
		ImageButton brnAdd = (ImageButton)convertView.findViewById(R.id.imageButtonAdd); 
		brnAdd.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {

					listener.onAdd("ttt");

				}});
    	textViewName.setText(mObjects.get(position));
		
		return convertView;
	}
}
