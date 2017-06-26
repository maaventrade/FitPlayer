package alex.mochalov.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import alex.mochalov.fitplayer.R;
import alex.mochalov.fitplayer.Utils;
import alex.mochalov.record.Record;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import alex.mochalov.record.*;

public class AdapterEditorExp extends BaseExpandableListAdapter  {

    private Context mContext;
    
    private List<Record> mGroups; // header titles
    private HashMap<Record, List<Record>> mChilds;    
  
    public AdapterEditorExp (Context context, List<Record> groups, HashMap<Record, List<Record>> childs){
        mContext = context;
        
        mGroups = groups;
        mChilds = childs;
    }
    
    
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return mChilds.get(mGroups.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final Record record = (Record) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_editor_child, null);
        }
 
        TextView name = (TextView) convertView
                .findViewById(R.id.TextViewName);
        name.setText(record.getName());
        
        TextView duration = (TextView) convertView
        		.findViewById(R.id.TextViewDuration);
        duration.setText(Utils.MStoString(record.getDuration()));
        
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
    	
    	if (mChilds.get(mGroups.get(groupPosition)) == null) return 0;
    	
    	else return mChilds.get(mGroups.get(groupPosition))
                .size();
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
        
    	Record record = (Record)getGroup(groupPosition);
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_editor_group, null);
        }
 
        TextView name = (TextView) convertView
                .findViewById(R.id.TextViewName);
        
        if (getChildrenCount(groupPosition) > 0)
        	name.setTypeface(null, Typeface.BOLD);
        
        name.setText(record.getName());
 
        return convertView;
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
