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

	private PFile getPFile(int groupPosition, int childPosition){
		if (childPosition < 0)
			return (PFile)adapter.getGroup(groupPosition);
		else if (groupPosition < 0)
			return (PFile)adapter.getGroup(childPosition);
		else 
			return (PFile)adapter.getChild(groupPosition, childPosition);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);

        rootView = inflater.inflate(R.layout.fragment_files, container, false);

        llFiles =  (LinearLayout)rootView.findViewById(R.id.llFiles);
        
		listViewFiles = (ExpandableListView)rootView.findViewById(R.id.ListViewFiles); 
		listViewFiles.setChoiceMode(ListView.CHOICE_MODE_SINGLE);////
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

				PFile pFile = getPFile(groupPosition, childPosition);

				/*
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
				*/
				
				selectedGroupIndex = groupPosition;
				selectedItemIndex = childPosition;
				
				//Toast.makeText(mContext,"You Clicked : "+ groupPosition+"  "+ childPosition ,Toast.LENGTH_SHORT).show();
				listViewFiles.setSelection(childPosition);
				mContext.openContextMenu(listViewFiles);
				
			}

			@Override
			public void onGoClicked(int groupPosition, int childPosition) {
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
						if (selectedGroupIndex == -1 && selectedItemIndex == groupPosition)
							StartEdit();
						
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

					
					if (selectedGroupIndex == groupPosition && selectedItemIndex == childPosition){
						StartEdit();
					}
						
					
					selectedGroupIndex = groupPosition;
					selectedItemIndex = childPosition;

					return true;
				}

			});


		mContext. getActionBar().setTitle(mContext.getResources().getString(R.string.timers));
		;
		mContext. getActionBar().setDisplayHomeAsUpEnabled(false);
		return rootView;
	}

	protected void StartEdit() {
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

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.fragment_files, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}


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
			
		super.onCreateContextMenu(menu, v, menuInfo);
		
		if (v.getId()==R.id.ListViewFiles) {
			MenuInflater inflater = mContext.getMenuInflater();
			
			if (selectedGroupIndex < 0)
				inflater.inflate(R.menu.popup_files, menu);
			else {
				PFile pFile = getPFile(selectedGroupIndex, selectedItemIndex);

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
		
		PFile pFile = getPFile(selectedGroupIndex, selectedItemIndex);
		
		switch (id)
		{
			case R.id.action_edit:
			if (!pFile.isDirectory())
			{
				FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();
				FragmentEditor fragmentEditor = new FragmentEditor(mContext);

				Bundle args = new Bundle();

				if (selectedItemIndex == -1)
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
			case R.id.action_move:
				if (!pFile.isDirectory())
					DialogMove();
				else 
					Toast.makeText(mContext, getResources().getString(R.string.warning_select_programm), Toast.LENGTH_LONG).show();
				return true;
			case R.id.action_delete:
				if (!pFile.isDirectory())
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("Delete " +
									   Files.getItem(selectedGroupIndex, selectedItemIndex) + ". Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

				}
				else 
					Toast.makeText(mContext, getResources().getString(R.string.warning_select_programm), Toast.LENGTH_LONG).show();

				return true;
			case R.id.action_copy:
				if (!pFile.isDirectory())
					copyPFile = Files.getItem(selectedGroupIndex, selectedItemIndex);
				else 
					Toast.makeText(mContext, getResources().getString(R.string.warning_select_programm), Toast.LENGTH_LONG).show();
				return true;
			case R.id.action_paste:
				if (copyPFile !=  null)
					DialogAddPasteRename("paste", getResources().getString(R.string.action_add_programm));
				return true;
			case R.id.action_rename:
				DialogAddPasteRename("rename", getResources().getString(R.string.action_rename));
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

	private void DialogMove()
	{
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(Files.getItem(selectedGroupIndex, selectedItemIndex).getName());
		//dialog.setMessage(getResources().getString(R.string.action_move));
		
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
		arrayAdapter.add("root");
		Files.getGroups(arrayAdapter);
		

		dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

		dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String strName = arrayAdapter.getItem(which);
					Utils.move(Files.getItem(selectedGroupIndex, selectedItemIndex).getName(), strName);
					Files.readFilesList();
					adapter.notifyDataSetChanged();
				}
			});
		dialog.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

		PFile pFile = null;
		if (selectedGroupIndex >= 0 || selectedItemIndex >= 0 )
			pFile = getPFile(selectedGroupIndex, selectedItemIndex);
		
		switch (id)
		{
			case R.id.action_calendar:
				FragmentCalendar fragmentCalendar = new FragmentCalendar(mContext);

				Bundle arguments = new Bundle();
				
				 Object[] objectsArray = Files.getList().toArray();
				 String[] stringsArray = new String[objectsArray.length];

				 for (int i = 0; i < objectsArray.length - 1; i++)
				 stringsArray[i] = objectsArray[i].toString();

				 arguments.putStringArray("files", stringsArray); 
				 fragmentCalendar.setArguments(arguments);

				 ft.replace(R.id.frgmCont, fragmentCalendar, TAG_FRAGMENT_CALENDAR);

				 ft.addToBackStack(null);

				 ft.commit();
				 
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
		
		 dialog.callback = new DialogSelectPath.SelectFileCallback() {
		 @Override
		 public void callbackOk(String path) {
		 File file = new File(path);
		 directory = file.getParent(); // to get the parent dir name				
		 Toast.makeText(mContext, 
		 
		 Utils.archive(Files.getGroups(), path),
		 
		 Toast.LENGTH_LONG).show();

		 }
		 }; 
		 dialog.show();

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
					if (! p0.equals("addGroup"))
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
						
						if (selectedGroupIndex != -1 && selectedItemIndex != -1) 
							groupName = Files.getGroup(selectedGroupIndex).getName()+"/";
							
						 if (!Utils.rename(mContext, 
										  groupName,
						 	Files.getItem(selectedGroupIndex, selectedItemIndex).getName(), 
							newFileName))
						 return;
						 
						 
						 Files.getItem(selectedGroupIndex, selectedItemIndex).setName(newFileName); 
						 Utils.setFileName(newFileName);
						 
						 adapter.notifyDataSetChanged();
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

	public int getSelectedGroupIndex() {
		return selectedGroupIndex;
	}

	public int getSelectedItemIndex() {
		return selectedItemIndex;
	}

	public void setSelectedGroupIndex(int par) {
		selectedGroupIndex = par;
	}

	public void setSelectedItemIndex(int par) {
		selectedItemIndex = par;
	}


}
