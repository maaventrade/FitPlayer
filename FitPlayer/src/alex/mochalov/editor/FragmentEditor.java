package alex.mochalov.editor;
import java.util.ArrayList;

import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class FragmentEditor extends Fragment
{
	Context mContext;
	private View rootView;

	AdapterEditorExp adapter; 
	ListView listViewRecords;

	private String fileName;
	
	private Record selectedRecord = null;

	public FragmentEditor(Context context){
		super();
		mContext = context;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
        rootView = inflater.inflate(R.layout.fragment_editor, container, false);

		TextView textViewFileName = (TextView)rootView.findViewById(R.id.TextViewFileName);
        
		Bundle args = getArguments();
		fileName = args.getString("name", "");
		textViewFileName.setText(fileName);
        
        Programm.loadXML(mContext, fileName);
        
        ExpandableListView listViewRecords = (ExpandableListView)rootView.findViewById(R.id.ListViewRecords);
		//listViewRecords.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        
       //Создаем адаптер и передаем context и список с данными
        adapter = new AdapterEditorExp(mContext, Programm.getGroups(), Programm.getChilds());
        listViewRecords.setAdapter(adapter);    
        
        listViewRecords.setOnGroupClickListener(new OnGroupClickListener(){

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				
				int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
				parent.setItemChecked(index, true);
				
				selectedRecord = Programm.getGroup(groupPosition);
				
				return false;
			}});
        
        listViewRecords.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
			    parent.setItemChecked(index, true);
			    
				selectedRecord = Programm.getItem(groupPosition, childPosition);
				
			    return true;
			}
		});
        	
		return rootView;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_editor, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
    public void onPause() {
        super.onPause();
    }	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id){
			case R.id.action_edit:
				if (selectedRecord != null){
					
					DialogEdit dialog = new DialogEdit(mContext, selectedRecord,
													   adapter);
					dialog.show();
					
				}
				return true;
			case R.id.action_add_child:
				if (selectedRecord != null){
				
					selectedRecord = Programm.addCHildRecord(selectedRecord);
					adapter.notifyDataSetChanged();
				}
				return true;
			case R.id.action_add:
				
	        	selectedRecord = Programm.addRecord(selectedRecord);
	        	adapter.notifyDataSetChanged();

				return true;
			case R.id.action_delete:
				if (selectedRecord != null){
					
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();
					
				}
				return true;
			case R.id.action_save:
				Programm.save(mContext, fileName);
				return true;
			default:	
				return super.onOptionsItemSelected(item);
		}
	}
	
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
				
	        	Programm.deleteRecord(selectedRecord);
	        	selectedRecord = null;
	        	adapter.notifyDataSetChanged();
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            break;
	        }
	    }
	};

}
