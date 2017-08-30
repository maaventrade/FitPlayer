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

import java.util.*;

public class FragmentFiles extends Fragment
{
	private String TAG_FRAGMENT_EDITOR = "TAG_FRAGMENT_EDITOR";
	private String TAG_FRAGMENT_CALENDAR = "TAG_FRAGMENT_CALENDAR";
	
	
	private Activity mContext;
	//Fragment thisFragment;
	private View rootView;
	//
	private ArrayList<String> files;
	
	private ListView listViewFiles;
	private AdapterFiles adapter;
	
	private int selectedStringIndex = -1;
	
	private int copyStringIndex = -1;
	
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
		
		listViewFiles = (ListView)rootView.findViewById(R.id.ListViewFiles); 
		listViewFiles.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		files = new ArrayList<String>();
		Utils.readFilesList(files);

		adapter = new AdapterFiles(mContext, mContext, files);

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
			if (selectedStringIndex >= 0){
			FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

			FragmentEditor fragmentEditor = new FragmentEditor(mContext);
			
			Bundle args = new Bundle();
		    args.putString("name", files.get(selectedStringIndex));
		    
		    fragmentEditor.setArguments(args);

			ft.replace(R.id.frgmCont, fragmentEditor, TAG_FRAGMENT_EDITOR);
			ft.addToBackStack(null);

			ft.commit();
			}
			return true;
			
		case R.id.action_delete:
				if (selectedStringIndex >= 0){

					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("Delete "+files.get(selectedStringIndex)+". Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

				}

			return true;
		case R.id.action_copy:
				if (selectedStringIndex >= 0){
					copyStringIndex = selectedStringIndex;
				}

				return true;
				
		case R.id.action_paste:
				if (copyStringIndex >= 0){
					DialogAddPasteRename("paste");
				}

				return true;
				
		case R.id.action_rename:
				if (selectedStringIndex >= 0){
					DialogAddPasteRename("rename");
				}

				return true;
			
		case R.id.action_add:
			
			DialogAddPasteRename("add");
			return true;
		case R.id.action_calendar:
				FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

				FragmentCalendar fragmentCalendar = new FragmentCalendar(mContext);

				Bundle arguments = new Bundle();

				Object[] objectsArray = files.toArray();
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
		default:	
			return super.onOptionsItemSelected(item);
		}
	}

	private void archive() {
		
		DialogSelectPath dialog = new DialogSelectPath(mContext, "");
		
		dialog.callback = new DialogSelectPath.SelectFileCallback() {
			@Override
			public void callbackOk(String path) {
				
				Toast.makeText(mContext, 
						Utils.archive(files, path), 
						Toast.LENGTH_LONG).show();
				
			}
		}; 
		dialog.show(); 
		
	}

	private void DialogAddPasteRename(final String p0)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(getResources().getString(R.string.action_add));

		final EditText name = new EditText(mContext);
		name.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(name);
		
		if(p0.equals("rename"))
			name.setText(Utils.trimExt(files.get(selectedStringIndex)));
		else if (p0.equals("paste")){
			builder.setTitle(getResources().getString(R.string.copy)+" "+files.get(selectedStringIndex));
			
		}

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String newFileName = Utils.trimExt(name.getText().toString())+".xml";
					
					if (p0.equals("add")){
						Programm.clear();
						Utils.setFileName(newFileName);
						Programm.save(mContext, newFileName);
						files.add(Utils.getFileName());
					} else if (p0.equals("rename")) {
						
						if (!Utils.rename(mContext,files.get(selectedStringIndex), newFileName))
							return;
						Utils.setFileName(newFileName);
						files.set(selectedStringIndex, Utils.getFileName());
						
					} else {
						if (!Utils.copy(files.get(selectedStringIndex), newFileName))
							return;
						Utils.setFileName(newFileName);
						files.add(Utils.getFileName());
					}
					
					
					Utils.sort(files);

					listViewFiles.setItemChecked(files.indexOf(selectedStringIndex), true);

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

					Utils.deleteFile(files.get(selectedStringIndex));
					files.remove(selectedStringIndex);
					
					selectedStringIndex = -1;
					adapter.notifyDataSetChanged();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
	        }
	    }
	};
	
	
}
