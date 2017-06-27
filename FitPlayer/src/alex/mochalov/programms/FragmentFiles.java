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
	private Activity mContext;
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

		final ArrayList<String> programms = new ArrayList<String>();
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
				// TODO: Implement this method
			}
		};
		
		listViewFiles.setAdapter(adapter);
		
		listViewFiles.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
					selectedString = (String) p1.getItemAtPosition(index);
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
