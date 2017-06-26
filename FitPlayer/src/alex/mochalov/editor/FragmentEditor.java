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

	private Record mainFolder;
	private String fileName;
	
	private int mIndex = -1;

	public FragmentEditor(Context context){
		super();
		mContext = context;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		
        rootView = inflater.inflate(R.layout.fragment_editor, container, false);
        listViewRecords = (ListView)rootView.findViewById(R.id.ListViewRecords);
		listViewRecords.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		TextView textViewFileName = (TextView)rootView.findViewById(R.id.TextViewFileName);
        
		Bundle args = getArguments();
		fileName = args.getString("name", "");
		textViewFileName.setText(fileName);
		
		mainFolder = Utils.loadXML(mContext, fileName);

        adapter = new AdapterEditor(mContext, mainFolder);

        listViewRecords.setAdapter(adapter);
		
        listViewRecords.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
					mIndex = index;
				}}
		);	

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
			case R.id.action_edit:
				if (mIndex >= 0){
					DialogEdit dialog = new DialogEdit(mContext, 
													   mainFolder.getRecords().get(mIndex),

													   adapter);
					dialog.show();
				}
				return true;
			case R.id.action_add:
				return true;
			case R.id.action_delete:
				//fr.start();
				return true;
			case R.id.action_go:
				Utils.saveProgramm(mContext, fileName, mainFolder);
				return true;
			default:	
				return super.onOptionsItemSelected(item);
		}
	}
	
}
