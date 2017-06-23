package alex.mochalov.programms;
import alex.mochalov.fitplayer.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import android.widget.AdapterView.*;

public class FragmentFiles extends Fragment
{
	private Context mContext;
	//Fragment thisFragment;
	private View rootView;
	
	private ListView listViewFiles;
	private AdapterFiles adapter;
	
	private String selectedString = "";
	
	public interface OnStartProgrammListener {
		public void onGoSelected(String text);
		public void onEditSelected(String text);
	}
	
	public OnStartProgrammListener listener;
	
	public FragmentFiles(Context context){
		super();
		mContext = context;
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

        rootView = inflater.inflate(R.layout.fragment_files, container, false);
		
		listViewFiles = (ListView)rootView.findViewById(R.id.ListViewFiles); 
		listViewFiles.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		final ArrayList<String> programms = new ArrayList<String>();
		Utils.readFilesList(programms);

		adapter = new AdapterFiles(mContext, programms);

		
		listViewFiles.setAdapter(adapter);
		
		listViewFiles.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
					//if (listener != null)
					//	listener.onItemSelected((String) p1.getItemAtPosition(index));
					selectedString = (String) p1.getItemAtPosition(index);
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
		case R.id.action_edit:

			if (listener != null && selectedString.length() > 0)
				listener.onEditSelected(selectedString);
			
			return true;
		case R.id.action_add:
			return true;
		case R.id.action_delete:
			//fr.start();
			return true;
		case R.id.action_go:

			if (listener != null && selectedString.length() > 0)
				listener.onGoSelected(selectedString);
			
			return true;
		default:	
			return super.onOptionsItemSelected(item);
		}
	}
	
}
