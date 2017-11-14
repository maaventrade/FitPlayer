package alex.mochalov.files;
import alex.mochalov.calendar.*;
import alex.mochalov.editor.*;
import alex.mochalov.fitplayer.*;
import alex.mochalov.main.*;
import alex.mochalov.player.*;
import alex.mochalov.programm.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.ContextMenu.*;
import android.widget.*;
import android.widget.ExpandableListView.*;
import java.io.*;

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

	private PFile copyPFile = null;

	private String directory = "";
	
	private LinearLayout llFiles;

	public interface OnStartProgrammListener
	{
		public void onGoSelected(String text);
		public void onEditSelected(String text);
	}

	public OnStartProgrammListener listener;

	public FragmentFiles(Activity context)
	{
		super();
		mContext = context;
	}

	public FragmentFiles()
	{
		super();
	}

	public void setParams(Activity context)
	{
		mContext = context;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);

        rootView = inflater.inflate(R.layout.fragment_files, container, false);

        llFiles =  (LinearLayout)rootView.findViewById(R.id.llFiles);
        
		listViewFiles = (ExpandableListView)rootView.findViewById(R.id.ListViewFiles); 
		//listViewFiles.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		registerForContextMenu(listViewFiles);
		Files.readFilesList();

		adapter = new AdapterFiles(mContext, mContext, Files.getGroups(), Files.getChilds());

		adapter.listener = new AdapterFiles.OnButtonClickListener(){

			@Override
			public void onEdit(String text)
			{

				if (listener != null && text.length() > 0)
					listener.onGoSelected(text);

			}

			@Override
			public void onAdd(String text)
			{

			}

			@Override
			public void onContextMenu(int groupPosition, int childPosition) {
				//showPopupMenu(listViewFiles, groupPosition, childPosition);
				
				PFile pFile = (PFile)adapter.getGroup(groupPosition);
				Log.d("h","pfile "+pFile.isDirectory());

				if (pFile.isDirectory())
				{
					selectedGroupIndex = groupPosition;
					selectedItemIndex = -1;
				}
				else
				{
					selectedGroupIndex = -1;
					selectedItemIndex = groupPosition;
				}
				
				//selectedGroupIndex = groupPosition;
				//selectedItemIndex = childPosition;
				
				//Toast.makeText(mContext,"You Clicked : "+ groupPosition+"  "+ childPosition ,Toast.LENGTH_SHORT).show();
				listViewFiles.setSelection(childPosition);
				mContext.openContextMenu(listViewFiles);
				
			}
		};
		
		

		listViewFiles.setAdapter(adapter);

		listViewFiles.setOnGroupClickListener(new OnGroupClickListener() {
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
											int groupPosition, long id)
				{

					int index = parent.getFlatListPosition(ExpandableListView
														   .getPackedPositionForGroup(groupPosition));
					parent.setItemChecked(index, true);

					PFile pFile = (PFile)adapter.getGroup(groupPosition);

					if (pFile.isDirectory())
					{
						selectedGroupIndex = groupPosition;
						selectedItemIndex = -1;
					}
					else
					{
						selectedGroupIndex = -1;
						selectedItemIndex = groupPosition;
					}

					return false;
				}
			});

		listViewFiles.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
											int groupPosition, int childPosition, long id)
				{

					int index = parent.getFlatListPosition(ExpandableListView
														   .getPackedPositionForChild(groupPosition, childPosition));
					parent.setItemChecked(index, true);

					selectedGroupIndex = groupPosition;
					selectedItemIndex = childPosition;

					return true;
				}

			});


		
		 listViewFiles.setOnItemClickListener( new ListView.OnItemClickListener(){
		 @Override
		 public void onItemClick(AdapterView<?> adapter, View p2, int index, long p4)
		 {
			 Toast.makeText(mContext,"You Clicked : ",Toast.LENGTH_SHORT).show();  
			 
			      
			 PopupMenu popup = new PopupMenu(mContext, p2);  
			 //Inflating the Popup using xml file  
			 popup.getMenuInflater().inflate(R.menu.popup_files, popup.getMenu());  

			 //registering popup with OnMenuItemClickListener  
			 popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
					 public boolean onMenuItemClick(MenuItem item) {  
						return true;  
					 }  
				 });  

			 popup.show();//showing popup menu  
		 }}
		 );	
		 

		mContext. getActionBar().setTitle(mContext.getResources().getString(R.string.timers));
		;
		mContext. getActionBar().setDisplayHomeAsUpEnabled(false);
		return rootView;
	}
/*
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.fragment_files, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

*/
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	
		
		if (selectedItemIndex >= 0)
			if (selectedGroupIndex == -1)
				menu.setHeaderTitle(Files.getItem(selectedGroupIndex, selectedItemIndex).getName());
			else
				menu.setHeaderTitle(Files.getGroup(selectedGroupIndex).getName() + "/" +
							   Files.getItem(selectedGroupIndex, selectedItemIndex).getName()
							   );
		else
			if (selectedGroupIndex == -1)
				return;
			else
				menu.setHeaderTitle(Files.getGroup(selectedGroupIndex).getName());
			
		Toast.makeText(mContext, "menu", Toast.LENGTH_LONG).show();
		super.onCreateContextMenu(menu, v, menuInfo);
		
		if (v.getId()==R.id.ListViewFiles) {
			MenuInflater inflater = mContext.getMenuInflater();
			
			if (selectedGroupIndex < 0)
				inflater.inflate(R.menu.popup_files, menu);
			else {
				PFile pFile = (PFile)adapter.getGroup(selectedGroupIndex);

				if (pFile.isDirectory())
					inflater.inflate(R.menu.popup_files_group, menu);
				else
					inflater.inflate(R.menu.popup_files, menu);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		
		  
		
		switch (id)
		{
		case R.id.action_go1:
			
				Toast.makeText(mContext,"You selectedItemIndex : " + selectedItemIndex,Toast.LENGTH_SHORT).show();  
				
			if (selectedItemIndex >= 0)
			{
				FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

				FragmentPlayer fragmentPlayer = new FragmentPlayer(mContext);

				Bundle args = new Bundle();
				
				if (selectedGroupIndex == -1)
					args.putString("name",
								   Files.getItem(selectedGroupIndex, selectedItemIndex).getName()
								   );
				else
					args.putString("name",
								   Files.getGroup(selectedGroupIndex).getName() + "/" +
								   Files.getItem(selectedGroupIndex, selectedItemIndex).getName()
								   );
				
				fragmentPlayer.setArguments(args);

				ft.replace(R.id.frgmCont, fragmentPlayer, FragmentPlayer.TAG_FRAGMENT_PLAYER);
				ft.addToBackStack(null);

				ft.commit();
			}
			return true;
		case R.id.action_edit:
				if (selectedItemIndex >= 0)
				{
					FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();
					FragmentEditor fragmentEditor = new FragmentEditor(mContext);

					Bundle args = new Bundle();

					if (selectedGroupIndex == -1)
						args.putString("name",
									   Files.getItem(selectedGroupIndex, selectedItemIndex).getName()
									   );
					else
						args.putString("name",
									   Files.getGroup(selectedGroupIndex).getName() + "/" +
									   Files.getItem(selectedGroupIndex, selectedItemIndex).getName()
									   );

					fragmentEditor.setArguments(args);

					ft.replace(R.id.frgmCont, fragmentEditor, TAG_FRAGMENT_EDITOR);
					ft.addToBackStack(null);

					ft.commit();
				}
				return true;

			case R.id.action_delete:
				if (selectedItemIndex >= 0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("Delete " +
									   Files.getItem(selectedGroupIndex, selectedItemIndex) + ". Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

				}

				return true;
			case R.id.action_copy:
				if (selectedItemIndex >= 0)
					copyPFile = Files.getItem(selectedGroupIndex, selectedItemIndex);
				else 
					Toast.makeText(mContext, getResources().getString(R.string.warning_select_programm), Toast.LENGTH_LONG).show();
				return true;
			case R.id.action_paste:
				if (copyPFile !=  null)
					DialogAddPasteRename("paste", getResources().getString(R.string.action_add_programm));
				return true;
			case R.id.action_rename:
				if (selectedItemIndex >= 0)
					DialogAddPasteRename("rename", getResources().getString(R.string.action_rename_group));
				else
					DialogAddPasteRename("renameGroup", getResources().getString(R.string.action_rename_group));
				return true;
			case R.id.action_add_programm:
				DialogAddPasteRename("add", getResources().getString(R.string.action_add_programm));
				return true;

			case R.id.action_add_group:
				DialogAddPasteRename("addGroup", getResources().getString(R.string.action_add_group));
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		switch (id)
		{
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

	private void extract()
	{
		DialogSelectPath dialog = new DialogSelectPath(mContext, directory, getResources().getString(R.string.extract_from_archive), false);

		dialog.callback = new DialogSelectPath.SelectFileCallback() {
			@Override
			public void callbackOk(String path)
			{
				File file = new File(path);
				directory = file.getParent(); // to get the parent dir name				
				//if (Utils.extract(mContext, path, files, adapter)){
				// 1111111111	
				//}
			}
		}; 
		dialog.show(); 

	}

	private void archive()
	{
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

		String groupName = "root";
		if (selectedGroupIndex != -1)
			groupName = Files.getGroup(selectedGroupIndex).getName();
		
		final EditText name = new EditText(mContext);
		name.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(name);

		if (p0.equals("rename"))
			name.setText(Utils.trimExt(Files.getItem(selectedGroupIndex, selectedItemIndex).getName()));
		else if (p0.equals("renameGroup"))
			name.setText(Files.getGroup(selectedGroupIndex).getName());
		else if (p0.equals("paste"))
		{
			builder.setTitle(getResources().getString(R.string.copy) + " " 
					+ copyPFile.getName()+" "
					+ getResources().getString(R.string.into)+" "
					+ groupName);
			name.setVisibility(View.INVISIBLE);

		}

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String newFileName = Utils.trimExt(name.getText().toString());
					if (! p0.equals("addGroup") && ! p0.equals("renameGroup"))
						newFileName = newFileName + ".xml";

					if (p0.equals("add"))
					{
						Programm.clear();
						Utils.setFileName(newFileName);
						File file = Programm.save(mContext, newFileName);
						Files.addChildRecord(Files.getGroup(selectedGroupIndex), file);

					}
					else if (p0.equals("addGroup"))
					{

						File file = Utils.addDirectory(mContext, newFileName);
						if (file != null) 
							Files.addGroup(file, selectedGroupIndex);

					}
					else if (p0.equals("rename"))
					{
						String groupName = "";
						if (selectedGroupIndex == -1)
							groupName = Files.getGroup(selectedGroupIndex).getName()+"/";
							
						 if (!Utils.rename(mContext, 
										  groupName,
						 	Files.getItem(selectedGroupIndex, selectedItemIndex).getName(), 
							newFileName))
						 return;
						 
						 
						 Utils.setFileName(newFileName);
						// Files.setChild(selectedGroupIndex, selectedItemIndex, Utils.getFileName());
					}
					else if (p0.equals("renameGroup") )
					{
						 if (!Utils.rename(mContext, 
										  "",
						 	Files.getGroup(selectedGroupIndex).getName(), 
							newFileName))
							
						 return;
						 Files.getGroup(selectedGroupIndex).setName(newFileName);
						 
						 Utils.setFileName(newFileName);
						// Files.setChild(selectedGroupIndex, selectedItemIndex, Utils.getFileName());
					}
					else
					{
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
	    public void onClick(DialogInterface dialog, int which)
		{
	        switch (which)
			{
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
