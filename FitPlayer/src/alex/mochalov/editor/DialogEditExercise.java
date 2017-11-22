package alex.mochalov.editor;

import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.programm.Programm;
import alex.mochalov.record.*;
import android.app.*;
import android.database.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import android.media.projection.*;
import android.widget.MediaController.*;
import android.content.*;

public class DialogEditExercise extends Dialog 
{
	private EditText etName;
	private EditText etDescription;
	private Exercise mExercise;
	private boolean mAdd;
	private Context mContext;

	public MyCallback callback = null;
	public interface MyCallback {
		void callbackOk(Exercise exercise); 
	} 
	
	public DialogEditExercise(Context context, Exercise exercise, boolean add) {
		super(context);
		mExercise = exercise;
		mAdd = add;
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_edit_exercise);
		this.setTitle(getContext().getResources().getString(R.string.edit_exercise));
		
		etName = (EditText)findViewById(R.id.etName);
		if (mExercise != null)
			etName.setText(mExercise.getName());
		
		if (mAdd)
			etName.setHint("New exercise");
		
		etName.requestFocus();
		
		etDescription = (EditText)findViewById(R.id.etDescription);
		if (mExercise != null)
			etDescription.setText(mExercise.getText());
		else
			etDescription.setHint("Description");
		
		Button btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View p1)
			{
				mExercise.setName(etName.getText().toString());
				mExercise.setText(etDescription.getText().toString());
				if (callback != null)
					callback.callbackOk(mExercise);
				dismiss();
			}
		});
					
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        switch (which){
					        case DialogInterface.BUTTON_POSITIVE:
								dismiss();
					            break;
					        case DialogInterface.BUTTON_NEGATIVE:
					            break;
					        }
					    }
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();
				}
			});
		
	}

}
