package alex.mochalov.programms;
import alex.mochalov.fitplayer.*;
import alex.mochalov.player.FragmentPlayer;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.util.*;   

import android.text.*;

public class FragmentFiles extends Fragment
{
	private Activity mContext;
	//Fragment thisFragment;
	private View rootView;
	//
	private ArrayList<String> programms;
	
	private ListView listViewFiles;
	private AdapterFiles adapter;
	
	private String selectedString = null;
	
	private String copyString = null;
	
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

		programms = new ArrayList<String>();
		Utils.readFilesList(programms);

		adapter = new AdapterFiles(mContext, programms);

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
					String selectedString1 = (String) adapter.getItemAtPosition(index);
					if (selectedString1.equals(selectedString)){
						FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

						FragmentPlayer fragmentPlayer = new FragmentPlayer(mContext);

						Bundle args = new Bundle();
						args.putString("name", selectedString);
						fragmentPlayer.setArguments(args);

						ft.replace(R.id.frgmCont, fragmentPlayer, FragmentPlayer.TAG_FRAGMENT_PLAYER);
						ft.addToBackStack(null);

						ft.commit();
					}
					else selectedString = selectedString1;
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
		case R.id.action_delete:
				if (selectedString != null){

					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("Delete "+selectedString+". Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

				}

			return true;
		case R.id.action_copy:
				if (selectedString != null){
					copyString = selectedString;
				}

				return true;
				
		case R.id.action_paste:
				if (copyString != null){
				DialogAddPaste("paste");
				}

				return true;
				
		case R.id.action_rename:
				if (selectedString != null){
					DialogAddPaste("rename");
				}

				return true;
			
		case R.id.action_add:
			
			DialogAddPaste("add");
			return true;
		default:	
			return super.onOptionsItemSelected(item);
		}
	}

	private void DialogAddPaste(final String p0)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(getResources().getString(R.string.action_add));

		final EditText name = new EditText(mContext);
		name.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(name);
		if(p0.equals("rename"))
			name.setText(Utils.trimExt(selectedString));

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String newFileName = Utils.trimExt(name.getText().toString())+".xml";
					
					if (p0.equals("add")){
						Programm.clear();
						Utils.setFileName(newFileName);
						Programm.save(mContext, newFileName);
					} else if (p0.equals("rename")) {
						
						if (!Utils.rename(mContext,selectedString, newFileName))
							return;
						Utils.setFileName(newFileName);
					} else {
						if (!Utils.copy(copyString, newFileName))
							return;
						Utils.setFileName(newFileName);
					}
					
					programms.add(Utils.getFileName());
					Utils.sort(programms);

					selectedString = Utils.getFileName();
					listViewFiles.setItemChecked(programms.indexOf(selectedString), true);

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

					Utils.deleteFile(selectedString);
					programms.remove(selectedString);
					
					selectedString = null;
					adapter.notifyDataSetChanged();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
	        }
	    }
	};
	
	
}
