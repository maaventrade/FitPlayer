package alex.mochalov.programms;
import alex.mochalov.fitplayer.*;
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
	
	private ArrayList<String> programms;
	
	private ListView listViewFiles;
	private AdapterFiles adapter;
	
	private String selectedString = "";
	
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
					selectedString = (String) adapter.getItemAtPosition(index);
					/*
					if (Utils.action.equals("edit") &&
						listener != null){
							Utils.action = "";
						Toast.makeText(mContext, selectedString, Toast.LENGTH_LONG).show();
						listener.onGoSelected(selectedString);
						}
						*/
				}}
		);	
		
		
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
					builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

				}

			
				
			return true;
		case R.id.action_go:

			if (listener != null && selectedString.length() > 0)
				listener.onGoSelected(selectedString);
			
			return true;
		case R.id.action_add:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(getResources().getString(R.string.action_add));
				
			final EditText name = new EditText(mContext);
			name.setInputType(InputType.TYPE_CLASS_TEXT);
			builder.setView(name);
			
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							Programm.clear();
							Utils.setFileName(name.getText()+".xml");
							Programm.save(mContext, Utils.getFileName());
							
							programms.add(Utils.getFileName());
							
							//listViewFiles.setSelection(programms.size()-1);
							listViewFiles.setItemChecked(programms.size()-1, false);
							
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
