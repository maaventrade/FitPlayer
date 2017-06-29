package alex.mochalov.editor;
import java.util.ArrayList;

import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
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

	public FragmentEditor(){
		super();
	}
	
	public void setParams(Context context){
		mContext = context;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
        rootView = inflater.inflate(R.layout.fragment_editor, container, false);
        
		Bundle args = getArguments();
		fileName = args.getString("name", "");
		//textViewFileName.setText(fileName);
        
        Programm.loadXML(mContext, fileName);

		final TextView nameMain = (TextView)rootView.findViewById(R.id.TextViewName);
		final TextView durationMain = (TextView)rootView.findViewById(R.id.TextViewDuration);
		final TextView textMain = (TextView)rootView.findViewById(R.id.TextViewText);
		
		nameMain.setText( Programm.getMainRecord().getName());
		durationMain.setText( Utils.MStoString(Programm.getMainRecord().getDuration()));
		textMain.setText( Programm.getMainRecord().getText());
        
		ImageButton brnEdit = (ImageButton)rootView
				.findViewById(R.id.imageButtonEdit);
		brnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				DialogEdit dialog = new DialogEdit(mContext, Programm.getMainRecord(), true);
				dialog.callback = new DialogEdit.MyCallback() {
					
					@Override
					public void callbackOk() {
						nameMain.setText( Programm.getMainRecord().getName());
						durationMain.setText( Utils.MStoString(Programm.getMainRecord().getDuration()));
						textMain.setText( Programm.getMainRecord().getText());
					}
				};
				
				dialog.show();

			}
		});
		
        ExpandableListView listViewRecords = (ExpandableListView)rootView.findViewById(R.id.ListViewRecords);
		//listViewRecords.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        
       //������� ������� � �������� context � ������ � �������
        adapter = new AdapterEditorExp(mContext, Programm.getGroups(), Programm.getChilds());
        adapter.callback = new AdapterEditorExp.MyCallback() {
			
			@Override
			public void callbackEdited() {
				durationMain.setText( Utils.MStoString(Programm.getMainRecord().getDuration()));
			}
		};
        
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
		case R.id.action_copy:
			Record newRecord = Programm.copyRecord(selectedRecord);
			adapter.notifyDataSetChanged();
			
			DialogEdit dialog = new DialogEdit(mContext, newRecord, false);
			dialog.callback = new DialogEdit.MyCallback() {
				@Override
				public void callbackOk() {
					adapter.notifyDataSetChanged();
				}
			};
			dialog.show();
			
		case R.id.action_add_child:
				if (selectedRecord != null){
				
					selectedRecord = Programm.addCHildRecord(selectedRecord);
					adapter.notifyDataSetChanged();
				}
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