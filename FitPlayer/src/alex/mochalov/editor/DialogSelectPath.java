package alex.mochalov.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import alex.mochalov.editor.DialogEditMain.MyCallback;
import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
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
	private String mTitle = "";
	private Boolean mDir = true;
	private String mDirectory = "";
	
	private ArrayList<File> listFiles = new ArrayList<File>();
	private ArrayAdapter<String> adapter;
	private ListView listViewFiles;

	public SelectFileCallback callback = null;
	public interface SelectFileCallback {
		void callbackOk(String path); 
	} 
	
	public DialogSelectPath(Context context, String path, String title, Boolean dir) {
		
		super(context);
		mContext = context;
		mTitle = title;
		mDir = dir;
		
		dialog = this;
		
		PATH = path;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle();
		
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
				
				if (listFiles.get(index).isFile()){
					if (callback != null)
						callback.callbackOk(PATH + "/" + f);
					dialog.dismiss();
				}
				
				if (f.equals("..")){
					int i = PATH.lastIndexOf("/");
					if (i >= 0)
						PATH = PATH.substring(0, i);
				} else {
					PATH = PATH + "/" + f;
				}
				
				setTitle();
				
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
					if (callback != null)
						callback.callbackOk(PATH);
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

	private void setTitle() {
		String s = mTitle;
		if ( !s.equals("") )
			s = s + ". ";
				
		if (mDir)
			this.setTitle(s+mContext.getResources().getString(R.string.select_path)+": "+PATH);
		else
			this.setTitle(s+mContext.getResources().getString(R.string.select_file)+": "+PATH);
	}

	
	class FileNameComparator implements Comparator<File> {   
		public int compare(File fileA, File fileB) {
			if ((fileA.isDirectory())&&(!fileB.isDirectory()))
				return -1;
			else if ((!fileA.isDirectory())&&(fileB.isDirectory()))
				return 1;
			else
				return fileA.getName().compareToIgnoreCase(fileB.getName());
			}
	}
	
	private String[] readFiles(String path) {

		listFiles.clear();
        File dir = new File(path+"/"); 
		
        File[] files = dir.listFiles();
        if (files != null )
            for (int i=0; i<files.length; i++){
            	boolean addFile = false;
            	if (files[i].isDirectory()) 
            		addFile = true;
            	else if ( !mDir ) addFile = true;
            	//else if (fileExt.length == 0) addFile = true;
            	//else for (int j=0; j<fileExt.length; j++)
            	//	if (files[i].getName().endsWith(fileExt[j])){
            	//		addFile = true;
            	//		break;
            	//	}
            	if (addFile) listFiles.add(files[i]);
        	}	

        FileNameComparator fnc = new FileNameComparator();
        Collections.sort(listFiles, fnc);
        
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
