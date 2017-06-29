package alex.mochalov.editor;

import java.io.File;
import java.util.ArrayList;

import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.util.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.text.*;

public class DialogSelectPath extends Dialog
{
	private Context mContext;
	private Dialog dialog;

	private String PATH = "";
	
	private ArrayList<File> listFiles = new ArrayList<File>();
	private ArrayAdapter<String> adapter;
	private ListView listViewFiles;

	private TextView mSelectedPath;
	
	public DialogSelectPath(Context context, TextView selectedPath) {
		
		super(context);
		mContext = context;
		mSelectedPath = selectedPath;
		dialog = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//this.setTitle(mContext.getResources().getString(R.string.));
		
		setContentView(R.layout.dialog_select_path);
	
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
	              WindowManager.LayoutParams.MATCH_PARENT);

		listViewFiles = (ListView)findViewById(R.id.listViewFiles);

		String[] stringArray = readFiles(PATH);
		
		adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, 
				stringArray);
		
		listViewFiles.setAdapter(adapter);
		listViewFiles.setOnItemClickListener( new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
			{
				String f = (String) p1.getItemAtPosition(index);
				
				if (f.equals("..")){
					int i = PATH.lastIndexOf("/");
					if (i >= 0)
						PATH = PATH.substring(0, i);
				} else {
					PATH = PATH + "/" + f;
				}
				
				Log.d("","PATH "+PATH);
				
				
				String[] stringArray = readFiles(PATH);
				
				adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, 
						stringArray);
				
				listViewFiles.setAdapter(adapter);
				
			}}
		);	
		
		Button btnOk = (Button)findViewById(R.id.dialogeditButtonOk);
		btnOk.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					mSelectedPath.setText(PATH);
					dialog.dismiss();
				}
			});
		
		
		Button btnCancel = (Button)findViewById(R.id.dialogeditButtonCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
					dialog.dismiss();
				}
			});
		
	}

	private String[] readFiles(String path) {

		listFiles.clear();
        File dir = new File(path+"/"); 
		
        File[] files = dir.listFiles();
        if (files != null )
            for (int i=0; i<files.length; i++){
            	boolean addFile = false;
            	if (files[i].isDirectory()) addFile = true;
            	//else if (fileExt.length == 0) addFile = true;
            	//else for (int j=0; j<fileExt.length; j++)
            	//	if (files[i].getName().endsWith(fileExt[j])){
            	//		addFile = true;
            	//		break;
            	//	}
            	if (addFile) listFiles.add(files[i]);
        	}	

        String[] stringArray;
		int i = 0;
        
		if (!path.equals("/")){
			stringArray = new String[listFiles.size()+1];
			stringArray[0] = "..";
			i = 1;
		} else {
			stringArray = new String[listFiles.size()];
		}
			
		for (File f: listFiles){
			stringArray[i] = f.getName();
			i++;
		}
		
        return stringArray;
	}	
	

	
	
}
