package alex.mochalov.editor;

import alex.mochalov.editor.DialogEdit.MyCallback;
import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.record.*;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import java.util.*;

public class AdapterEditorExp extends BaseExpandableListAdapter {

	private Context mContext;

	private List<Record> mGroups; // header titles
	private HashMap<Record, List<Record>> mChilds;

	private AdapterEditorExp adapter;

	MyCallback callback = null; 
	interface MyCallback {
		void callbackEdited(); 
	} 
	
	public AdapterEditorExp(Context context, List<Record> groups,
			HashMap<Record, List<Record>> childs) {
		mContext = context;

		mGroups = groups;
		mChilds = childs;

		adapter = this;
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

		/*
		 * final Record record = (Record) getChild(groupPosition,
		 * childPosition);
		 * 
		 * if (convertView == null) { LayoutInflater infalInflater =
		 * (LayoutInflater) mContext
		 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); convertView =
		 * infalInflater.inflate(R.layout.item_editor_child, null); }
		 * 
		 * TextView name = (TextView) convertView
		 * .findViewById(R.id.TextViewName); name.setText(record.getName());
		 * 
		 * TextView duration = (TextView) convertView
		 * .findViewById(R.id.TextViewDuration);
		 * duration.setText(Utils.MStoString(record.getDuration()));
		 * 
		 * return convertView;
		 */

	}

	private View getView(final int groupPosition, final int childPosition,
			boolean isLastChild, boolean isExpanded, View convertView,
			ViewGroup parent) {

		Record record = null;
		if (childPosition < 0)
			record = (Record) getGroup(groupPosition);
		else
			record = (Record) getChild(groupPosition, childPosition);

		final Record recordF = record;

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.item_editor, null);
		}

		TextView name = (TextView) convertView.findViewById(R.id.TextViewName);
		name.setText(record.getName());

		TextView text = (TextView) convertView.findViewById(R.id.TextViewText);
		text.setText(record.getText());

		TextView duration = (TextView) convertView
				.findViewById(R.id.TextViewDuration);
		duration.setText(Utils.MStoString(record.getDuration()));

		if (childPosition < 0) {
			if (getChildrenCount(groupPosition) > 0)
				name.setTypeface(null, Typeface.BOLD);
		} else {
			LinearLayout layoutChild = (LinearLayout) convertView
					.findViewById(R.id.LayoutChild);

			layoutChild.setTranslationX(50f);
		}

		ImageButton brnEdit = (ImageButton) convertView
				.findViewById(R.id.imageButtonEdit);
		if (Programm.isLocked()){
			brnEdit.setVisibility(View.INVISIBLE);
		} else {
			brnEdit.setVisibility(View.VISIBLE);
			brnEdit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (childPosition < 0) 
						OpenDialogEdit(recordF, getChildrenCount(groupPosition) > 0);
					else	
						OpenDialogEdit(recordF, false);
				}
			});
			
		}

		return convertView;

	}

	protected void OpenDialogEdit(Record recordF, boolean isGroup) {
		DialogEdit dialog = new DialogEdit(mContext, recordF, isGroup);
		dialog.callback = new DialogEdit.MyCallback() {

			@Override
			public void callbackOkNew(Record newRecord)
			{
				
			}

			
			@Override
			public void callbackOk() {
				adapter.notifyDataSetChanged();
				
				if (callback != null)
					callback.callbackEdited();
				
			}
		};
		dialog.show();
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

}
