package alex.mochalov.editor;
import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class FragmentEditor extends Fragment
{
	Context mContext;
	private View rootView;

	AdapterEditor adapter; 
	ListView listViewRecords;

	Folder mainFolder;

	public FragmentEditor(Context context){
		super();
		mContext = context;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		
        rootView = inflater.inflate(R.layout.fragment_editor, container, false);
        listViewRecords = (ListView)rootView.findViewById(R.id.ListViewRecords);
        TextView textViewFileName = (TextView)rootView.findViewById(R.id.TextViewFileName);
        
		Bundle args = getArguments();
		String name = args.getString("name", "");
		textViewFileName.setText(name);
		
		mainFolder = Utils.loadXML(mContext, name);

        adapter = new AdapterEditor(mContext, mainFolder);

        listViewRecords.setAdapter(adapter);
        listViewRecords.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
					DialogEdit dialog = new DialogEdit(mContext);
					dialog.show();
					
				}}
		);	

		return rootView;
	}
	
	//public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	//	inflater.inflate(R.menu.fragment_records, menu);
	//	super.onCreateOptionsMenu(menu, inflater);
	//}

	@Override
    public void onPause() {
        super.onPause();
    }	
	
}
