package alex.mochalov.files;

import alex.mochalov.fitplayer.*;
import alex.mochalov.main.*;
import alex.mochalov.player.*;
import alex.mochalov.programm.FileData;
import alex.mochalov.record.*;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import java.text.*;
import java.util.*;

public class AdapterFiles extends BaseExpandableListAdapter {
	private LayoutInflater inflater;
	
	private Activity mActivity;
	private Context mContext;
	
	private List<PFile> mGroups; // header titles
	private HashMap<PFile, List<PFile>> mChilds;
	
	public interface OnButtonClickListener {
		public void onEdit(String text);
		public void onAdd(String text);
	}
	public OnButtonClickListener listener;

	AdapterFiles(Activity activity, Context context, List<PFile> groups,
			HashMap<PFile, List<PFile>> childs) {
		
		mContext = context;
		mActivity = activity;
		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mGroups = groups;
		mChilds = childs;
		
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return mChilds.get(mGroups.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		return getView(groupPosition, childPosition, isLastChild, false,
				convertView, parent);
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {

		if (mChilds.get(mGroups.get(groupPosition)) == null)
			return 0;

		else
			return mChilds.get(mGroups.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		return getView(groupPosition, -1, false, isExpanded, convertView,
				parent);
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@SuppressLint("NewApi")
	private View getView(final int groupPosition, final int childPosition,
			boolean isLastChild, boolean isExpanded, View convertView,
			ViewGroup parent) {
		
		if (convertView == null) { 
			convertView = inflater.inflate(R.layout.item_files, null);
		}
				
		if (childPosition >= 0){
			convertView.setPadding(40, 0, 0, 0);
		}
		TextView textViewName = (TextView)convertView.findViewById(R.id.TextViewName);
		TextView TextViewDate = (TextView)convertView.findViewById(R.id.TextViewDate);
		ImageView ivLocked =  (ImageView)convertView.findViewById(R.id.ivLocked);
		TextView tvDuration = (TextView)convertView.findViewById(R.id.tvDuration);
		TextView tvInfo = (TextView)convertView.findViewById(R.id.tvInfo);
		
		PFile record = null;
		if (childPosition < 0)
			record = (PFile) getGroup(groupPosition);
		else
			record = (PFile)getChild(groupPosition, childPosition);

		ImageButton ibFile = (ImageButton)convertView.findViewById(R.id.ibFile);
		if (record.isDirectory())
			ibFile.setImageResource(R.drawable.folder);
		else	
			ibFile.setImageResource(R.drawable.file);
		
		
		ImageButton brnGo = (ImageButton)convertView.findViewById(R.id.imageButtonGo);
		brnGo.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {

					FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();

					FragmentPlayer fragmentPlayer = new FragmentPlayer(mActivity);

					Bundle args = new Bundle();
					
					PFile record = null;
					if (childPosition < 0)
						record = (PFile) getGroup(groupPosition);
					else
						record = (PFile)getChild(groupPosition, childPosition);
					
					args.putString("name", record.getName());
					fragmentPlayer.setArguments(args);

					ft.replace(R.id.frgmCont, fragmentPlayer, FragmentPlayer.TAG_FRAGMENT_PLAYER);
					ft.addToBackStack(null);

					ft.commit();

				}});
		
		
    	textViewName.setText(record.getName());
		
		Date date = record.getDate();
	//	Log.d("d","date "+date+"  "+Utils.getDateOfTheLastFale());
		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
		DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(mContext);

		//return ;
		
    	TextViewDate.setText(dateFormat.format(date)+" "+timeFormat.format(date));
//if (date.equals(Utils.getDateOfTheLastFale()))
//			textViewName.setTypeface(null, Typeface.BOLD);
// 1111111111111111	;
	
		
		
		
	/*	
    	FileData fileData = Utils.getFileData(mActivity, record);
    	tvDuration.setText(Utils.MStoString(fileData.duration));
		tvInfo.setText(fileData.getInfo());
/*    	
		if (fileData.isLocked())
			ivLocked.setImageDrawable(mContext.getResources().getDrawable(R.drawable.lock));
		else
			ivLocked.setImageDrawable(mContext.getResources().getDrawable(R.drawable.void1));
*/
		return convertView;
	}
}
