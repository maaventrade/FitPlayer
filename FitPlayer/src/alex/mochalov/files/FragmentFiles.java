package alex.mochalov.files;
import alex.mochalov.calendar.*;
import alex.mochalov.editor.*;
import alex.mochalov.fitplayer.*;
import alex.mochalov.main.*;
import alex.mochalov.player.*;
import alex.mochalov.programm.Programm;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import java.io.File;
import java.util.*;

public class FragmentFiles extends Fragment
{
	private String TAG_FRAGMENT_EDITOR = "TAG_FRAGMENT_EDITOR";
	private String TAG_FRAGMENT_CALENDAR = "TAG_FRAGMENT_CALENDAR";
	
	
	private Activity mContext;
	//Fragment thisFragment;
	private View rootView;
	//
	
	//private ArrayList<String> files;
	
	private ExpandableListView listViewFiles;
	private AdapterFiles adapter;
	
	private int selectedGroupIndex = -1;
	private int selectedItemIndex = -1;
	
	private int copyItemIndex = -1;
	private int copyGroupIndex = -1;

	private String directory = "";
	
	public interface OnStartProgrammListener {
		public void onGoSelected(String text);
		public void onEditSelected(String text);
	}
	
	public OnStartProgrammListener listener;
	
	public FragmentFiles(Activity context){
		super();
		mContext = context;
	}
	
	public FragmentFiles(){
		super();
	}
	
	public void setParams(Activity context){
		mContext = context;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

        rootView = inflater.inflate(R.layout.fragment_files, container, false);
		
		listViewFiles = (ExpandableListView)rootView.findViewById(R.id.ListViewFiles); 
		//listViewFiles.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		Files.readFilesList();

		adapter = new AdapterFiles(mContext, mContext, Files.getGroups(), Files.getChilds());

		adapter.listener = new AdapterFiles.OnButtonClickListener(){

			@Override
			public void onEdit(String text)
			{
				//Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
				if (listener != null && text.length() > 0)
					listener.onGoSelected(text);
				
			}

			@Override
			public void onAdd(String text)
			{
				
			}
		};
		
		listViewFiles.setAdapter(adapter);
		
		listViewFiles.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				int index = parent.getFlatListPosition(ExpandableListView
						.getPackedPositionForGroup(groupPosition));
				parent.setItemChecked(index, true);

				
				selectedGroupIndex = groupPosition;
				selectedItemIndex = -1;
				
				/*
				 * if (selectedRecord1 == selectedRecord)
				 * openDialogEdit(selectedRecord,
				 * adapter.getChildrenCount(groupPosition) > 0); else
				 * selectedRecord = selectedRecord1;
				 */
				return false;
			}
		});

		listViewFiles.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				int index = parent.getFlatListPosition(ExpandableListView
						.getPackedPositionForChild(groupPosition, childPosition));
				parent.setItemChecked(index, true);

				selectedGroupIndex = groupPosition;
				selectedItemIndex = childPosition;
				
				/*
				Record selectedRecord1 = Programm.getItem(groupPosition,
						childPosition);

				if (selectedRecord1 == selectedRecord)
					openDialogEdit(selectedRecord, false);
				else
					selectedRecord = selectedRecord1;
*/
				return true;
			}

		});
		
		
		/*
		listViewFiles.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View p2, int index, long p4)
				{
					//String selectedString1 = (String) adapter.getItemAtPosition(index);
					if (selectedStringIndex == index){
						FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

						FragmentPlayer fragmentPlayer = new FragmentPlayer(mContext);

						Bundle args = new Bundle();
						args.putString("name", files.get(selectedStringIndex));
						fragmentPlayer.setArguments(args);

						ft.replace(R.id.frgmCont, fragmentPlayer, FragmentPlayer.TAG_FRAGMENT_PLAYER);
						ft.addToBackStack(null);

						ft.commit();
					}
					else selectedStringIndex = index;
				}}
		);	
		*/
		
		mContext. getActionBar().setTitle(mContext.getResources().getString(R.string.timers));
		;
		mContext. getActionBar().setDisplayHomeAsUpEnabled(false);
		return rootView;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_files, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id){
		case R.id.action_edit:
			if (selectedItemIndex >= 0){
			FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

			FragmentEditor fragmentEditor = new FragmentEditor(mContext);
			
			Bundle args = new Bundle();
		    args.putString("name", Files.getItem(selectedGroupIndex, selectedItemIndex).getName());
		    
		    fragmentEditor.setArguments(args);

			ft.replace(R.id.frgmCont, fragmentEditor, TAG_FRAGMENT_EDITOR);
			ft.addToBackStack(null);

			ft.commit();
			}
			return true;
			
		case R.id.action_delete:
				if (selectedItemIndex >= 0){
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("Delete "+
							Files.getItem(selectedGroupIndex, selectedItemIndex)+". Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

				}

			return true;
		case R.id.action_copy:
				if (selectedItemIndex >= 0){
					copyItemIndex = selectedItemIndex;
					copyGroupIndex = selectedGroupIndex;
				}
				return true;
		case R.id.action_paste:
				if (copyItemIndex >= 0){
					DialogAddPasteRename("paste", getResources().getString(R.string.action_add_child));
				}
				return true;
		case R.id.action_rename:
				if (copyItemIndex >= 0){
					DialogAddPasteRename("rename", getResources().getString(R.string.action_add_child));
				}

				return true;
			
		case R.id.action_add_child:
			DialogAddPasteRename("add", getResources().getString(R.string.action_add_child));
			return true;
			
		case R.id.action_add:
			DialogAddPasteRename("addGroup", getResources().getString(R.string.action_add_group));
			return true;
			
		case R.id.action_calendar:
				FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

				FragmentCalendar fragmentCalendar = new FragmentCalendar(mContext);

				Bundle arguments = new Bundle();
/*
				Object[] objectsArray = files.toArray();
				String[] stringsArray = new String[objectsArray.length];

				for (int i = 0; i < objectsArray.length - 1; i++)
					stringsArray[i] = objectsArray[i].toString();
				
				arguments.putStringArray("files", stringsArray); 
				fragmentCalendar.setArguments(arguments);
				
				ft.replace(R.id.frgmCont, fragmentCalendar, TAG_FRAGMENT_CALENDAR);
				
				ft.addToBackStack(null);

				ft.commit();
				*/
				return true;
		case R.id.action_archive:
			archive();
			return true;
		case R.id.action_extract:
			extract();
			return true;						
		default:	
			return super.onOptionsItemSelected(item);
		}
	}

	private void extract() {
		DialogSelectPath dialog = new DialogSelectPath(mContext, directory, getResources().getString(R.string.extract_from_archive), false);
		
		dialog.callback = new DialogSelectPath.SelectFileCallback() {
			@Override
			public void callbackOk(String path) {
				File file = new File(path);
				directory = file.getParent(); // to get the parent dir name				
				//if (Utils.extract(mContext, path, files, adapter)){
				// 1111111111	
				//}
			}
		}; 
		dialog.show(); 
		
	}

	private void archive() {
		DialogSelectPath dialog = new DialogSelectPath(mContext, directory, getResources().getString(R.string.archive_all), true);
		/* 11111111111
		dialog.callback = new DialogSelectPath.SelectFileCallback() {
			@Override
			public void callbackOk(String path) {
				File file = new File(path);
				directory = file.getParent(); // to get the parent dir name				
				Toast.makeText(mContext, 
						Utils.archive(files, path), 
						Toast.LENGTH_LONG).show();
				
			}
		}; 
		dialog.show();
		*/
		
	}

	private void DialogAddPasteRename(final String p0, String title)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		
		builder.setTitle(title);

		final EditText name = new EditText(mContext);
		name.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(name);
		
		if(p0.equals("rename"))
			name.setText(Utils.trimExt(Files.getItem(selectedGroupIndex, selectedItemIndex).getName()));
		else if (p0.equals("paste")){
			builder.setTitle(getResources().getString(R.string.copy)+" "+Files.getItem(selectedGroupIndex, selectedItemIndex));
			
		}

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String newFileName = Utils.trimExt(name.getText().toString());
					if (! p0.equals("addGroup"))
						newFileName = newFileName + ".xml";
					
					if (p0.equals("add")){
						Programm.clear();
						Utils.setFileName(newFileName);
						File file = Programm.save(mContext, newFileName);
						Files.addChildRecord(Files.getGroup(selectedGroupIndex), file);
						
					} else if (p0.equals("addGroup")) {

						File file = Utils.addDirectory(mContext, newFileName);
						if (file != null) 
							Files.addGroup(file, selectedGroupIndex);
						
					} else if (p0.equals("rename")) {
						/*
						if (!Utils.rename(mContext, Files.getItem(selectedGroupIndex, selectedItemIndex).getName(), newFileName))
							return;
						Utils.setFileName(newFileName);
						Files.setChild(selectedGroupIndex, selectedItemIndex, Utils.getFileName());
						*/
						
					} else {
						/*
						if (!Utils.copy(Files.getItem(selectedGroupIndex, selectedItemIndex), newFileName))
							return;
						Utils.setFileName(newFileName);
						Files.addChildRecord(Files.getGroup(selectedGroupIndex), Utils.getFileName());
						*/
					}
					
					
					//Utils.sort(files);

					listViewFiles.setItemChecked(selectedItemIndex, true); /// ???????????

					adapter.notifyDataSetChanged();
				}
			});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int p2)
				{
					dialog.cancel();
				}
			});
		builder.show();
	}
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
				case DialogInterface.BUTTON_POSITIVE:

					//Utils.deleteFile(Files.getItem(selectedGroupIndex, selectedItemIndex));
					//Files.deleteRecord(Files.getItem(selectedGroupIndex, selectedItemIndex));
					
					selectedItemIndex = -1;
					adapter.notifyDataSetChanged();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
	        }
	    }
	};
	

}
