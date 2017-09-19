package alex.mochalov.editor;

import java.io.File;
import java.util.ArrayList;

import alex.mochalov.editor.DialogEdit.MyCallback;
import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.player.AdapterPlayer;
import alex.mochalov.player.FragmentPlayer;
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

public class DialogSelectExercise extends Dialog
{
	private Context mContext;
	private Dialog dialog;

	private AdapterSelectExercise adapter;
	
	private int selectedStringIndex = -1;
	
	MyCallback callback = null;
	interface MyCallback {
		void selected(Exercise record); 
	} 
	
	public DialogSelectExercise(Context context) {
		super(context);
		mContext = context;
		dialog = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setTitle(mContext.getResources().getString(R.string.select));
		setContentView(R.layout.dialog_select_exercise);
	
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
	              WindowManager.LayoutParams.MATCH_PARENT);

		ListView listViewExercice = (ListView)findViewById(R.id.ListViewSelect);
		listViewExercice.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
        adapter = new AdapterSelectExercise(mContext, Exercises.getRecords());
		
        listViewExercice.setAdapter(adapter);
		
        listViewExercice.setOnItemClickListener( new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
			{
				if (selectedStringIndex == index){
					if (callback != null){
						callback.selected(Exercises.getRecords().get(index));
						dialog.dismiss();
					}
				}
				else selectedStringIndex = index;
			}}
		);	
		
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					dialog.dismiss();
				}
			});
		
		Button btnSelect = (Button)findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (selectedStringIndex != -1){
						if (callback != null){
							callback.selected(Exercises.getRecords().get(selectedStringIndex));
							dialog.dismiss();
						}
					}
				}
			});
		
		Button btnEdit = (Button)findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (selectedStringIndex != -1){
						DialogEditExercise dialogEditExercise = new DialogEditExercise(getContext(), Exercises.getRecords().get(selectedStringIndex));
						dialogEditExercise.show();
						dialogEditExercise.callback = new DialogEditExercise.MyCallback() {
							@Override
							public void callbackOk(Exercise exercise) {
								adapter.notifyDataSetChanged();
							}
						};
					}
				}
			});
		
		Button btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					DialogEditExercise dialogEditExercise = new DialogEditExercise(getContext(), new Exercise());
					dialogEditExercise.show();
					dialogEditExercise.callback = new DialogEditExercise.MyCallback() {
						@Override
						public void callbackOk(Exercise exercise) {
							Exercises.addExercise(exercise);
							adapter.notifyDataSetChanged();
						}
					};
				}
			});
		
	}

	protected void onDismiss(DialogInterface dialog){
		Exercises.SaveExercises(mContext);
	}
	
}
