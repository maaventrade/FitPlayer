package alex.mochalov.editor;

import java.io.File;
import java.util.ArrayList;

import alex.mochalov.editor.DialogEdit.MyCallback;
import alex.mochalov.fitplayer.*;
import alex.mochalov.player.AdapterPlayer;
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

public class DialogSelectRecord extends Dialog
{
	private Context mContext;
	private Dialog dialog;

	private AdapterSelectRecord adapter;
	
	private ArrayList<Record> records = new ArrayList<Record>(); 

	private TextView mSelectedPath;
	
	MyCallback callback = null;
	interface MyCallback {
		void selected(Record record); 
	} 
	
	public DialogSelectRecord(Context context) {
		super(context);
		mContext = context;
		dialog = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setTitle(mContext.getResources().getString(R.string.select));
		
		setContentView(R.layout.dialog_select);
	
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
	              WindowManager.LayoutParams.MATCH_PARENT);

		ListView listViewFiles = (ListView)findViewById(R.id.ListViewSelect);

		Programm.loadXMLrecords(mContext, records);
		
        adapter = new AdapterSelectRecord(mContext, records);
		
		listViewFiles.setAdapter(adapter);
		
		listViewFiles.setOnItemClickListener( new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
			{
				if (callback != null){
					callback.selected(records.get(index));
					dialog.dismiss();
				}
			}}
		);	
	
		Button btnCancel = (Button)findViewById(R.id.dialogeditButtonCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					dialog.dismiss();
				}
			});
		
	}

}
