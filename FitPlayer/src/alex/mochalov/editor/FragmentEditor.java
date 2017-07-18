package alex.mochalov.editor;

import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.ExpandableListView.*;
import android.view.View.OnClickListener;

public class FragmentEditor extends Fragment {
	private Activity mContext;
	private View rootView;

	private AdapterEditorExp adapter;

	private Record selectedRecord = null;

	private DialogEditMain dialogEditMain;

	private boolean mModyfied;

	private ExpandableListView listViewRecords;
	private TextView durationMain;

	public FragmentEditor(Activity context) {
		super();
		mContext = context;
	}

	public FragmentEditor() {
		super();
	}

	public boolean isModyfied() {
		return mModyfied;
	}

	public void setModyfied() {
		mModyfied = true;
	}

	public void setParams(Activity context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		rootView = inflater.inflate(R.layout.fragment_editor, container, false);

		Bundle args = getArguments();
		Utils.setFileName(args.getString("name", ""));
		// textViewFileName.setText(fileName);

		Programm.loadXML(mContext, Utils.getFileName());

		// Edit programm Header
		final TextView nameMain = (TextView) rootView
				.findViewById(R.id.TextViewName);
		durationMain = (TextView) rootView.findViewById(R.id.TextViewDuration);
		final TextView textMain = (TextView) rootView
				.findViewById(R.id.TextViewText);

		nameMain.setText(Programm.getMainRecord().getName());
		durationMain.setText(Utils.MStoString(Programm.getMainRecord()
				.getDuration()));
		textMain.setText(Programm.getMainRecord().getText());

		ImageButton brnEdit = (ImageButton) rootView
				.findViewById(R.id.imageButtonEdit);
		brnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialogEditMain = new DialogEditMain(mContext, Programm
						.getMainRecord(), false);
				dialogEditMain.callback = new DialogEditMain.MyCallback() {

					@Override
					public void callbackOk() {
						nameMain.setText(Programm.getMainRecord().getName());
						durationMain.setText(Utils.MStoString(Programm
								.getMainRecord().getDuration()));
						textMain.setText(Programm.getMainRecord().getText());
						adapter.notifyDataSetChanged();
						mModyfied = true;
					}
				};

				dialogEditMain.show();

			}
		});
		// \Edit programm Header

		listViewRecords = (ExpandableListView) rootView
				.findViewById(R.id.ListViewRecords);
		// listViewRecords.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		adapter = new AdapterEditorExp(mContext, Programm.getGroups(),
				Programm.getChilds());
		adapter.callback = new AdapterEditorExp.MyCallback() {

			@Override
			public void callbackEdited() {
				setModyfied();
				durationMain.setText(Utils.MStoString(Programm.getMainRecord()
						.getDuration()));
			}
		};

		listViewRecords.setAdapter(adapter);

		listViewRecords.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				int index = parent.getFlatListPosition(ExpandableListView
						.getPackedPositionForGroup(groupPosition));
				parent.setItemChecked(index, true);

				selectedRecord = Programm.getGroup(groupPosition);
				/*
				 * if (selectedRecord1 == selectedRecord)
				 * openDialogEdit(selectedRecord,
				 * adapter.getChildrenCount(groupPosition) > 0); else
				 * selectedRecord = selectedRecord1;
				 */
				return false;
			}
		});

		listViewRecords.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				int index = parent.getFlatListPosition(ExpandableListView
						.getPackedPositionForChild(groupPosition, childPosition));
				parent.setItemChecked(index, true);

				Record selectedRecord1 = Programm.getItem(groupPosition,
						childPosition);

				if (selectedRecord1 == selectedRecord)
					openDialogEdit(selectedRecord, false);
				else
					selectedRecord = selectedRecord1;

				return true;
			}

		});

		mContext.getActionBar().setTitle(
				mContext.getResources().getString(R.string.edit_timer));

		mModyfied = false;

		return rootView;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_editor, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPause() {
		// Programm.save(mContext, Utils.getFileName());
		/*
		 * AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		 * builder
		 * .setMessage("Delete "+selectedRecord.getName()+".Are you sure?"
		 * ).setPositiveButton("Yes", dialogClickListener)
		 * .setNegativeButton("No", dialogClickListener).show();
		 */

		super.onPause();
	}

	@Override
	public void onDestroyView() {
		// mVIsible = false;
		super.onDestroyView();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Record newRecord;
		int id = item.getItemId();

		switch (id) {
		case R.id.action_copy:
			if (isLocked())
				return true;

			if (selectedRecord == null) {
				Toast.makeText(mContext, "Line not selected", Toast.LENGTH_LONG)
						.show();
				return true;
			}

			selectedRecord.copy();
			return true;

		case R.id.action_cut:
			if (isLocked())
				return true;

			if (selectedRecord == null) {
				Toast.makeText(mContext, "Line not selected", Toast.LENGTH_LONG)
						.show();
				return true;
			}

			selectedRecord.copy();

			Programm.deleteRecord(selectedRecord);
			selectedRecord = null;
			adapter.notifyDataSetChanged();

			setModyfied();
			return true;

		case R.id.action_paste:
			if (isLocked())
				return true;

			if (Utils.getCopyRecord() == null) {
				Toast.makeText(mContext, "Copy is null", Toast.LENGTH_LONG)
						.show();
				return true;
			}

			if (selectedRecord == null) {
				return true;
			}

			newRecord = Programm.pasteRecord(Utils.getCopyRecord(),
					selectedRecord);
			selectedRecord = newRecord;

			Programm.summDurationsAll();
			durationMain.setText(Utils.MStoString(Programm.getMainRecord()
					.getDuration()));

			listViewRecords.setItemChecked(Programm.getIndex(newRecord), true);
			adapter.notifyDataSetChanged();

			// openDialogEdit(newRecord, false);

			setModyfied();
			return true;
		case R.id.action_add:
			if (isLocked())
				return true;

			openDialogAdd();

			setModyfied();
			return true;
		case R.id.action_add_child:
			if (isLocked())
				return true;
			
			if (selectedRecord != null) {

				selectedRecord = Programm.addCHildRecord(selectedRecord);
				adapter.notifyDataSetChanged();

			}
			setModyfied();
			return true;
		case R.id.action_delete:
			if (isLocked())
				return true;
			
			if (selectedRecord != null) {

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage(
						"Delete " + selectedRecord.getName() + ".Are you sure?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();

			}
			setModyfied();
			return true;
		case R.id.action_save:
			Programm.save(mContext, Utils.getFileName());
			
			durationMain.setText(Utils.MStoString(Programm.getMainRecord()
					.getDuration()));
			
			mModyfied = false;
			return true;
		case R.id.action_set_rests_duration:
			if (isLocked())
				return true;
			
			DialogSetRestsTime();
			setModyfied();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void DialogSetRestsTime() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(getResources().getString(
				R.string.action_set_rests_duration));

		final EditText time = new EditText(mContext);
		time.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(time);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p1, int p2) {
				int duration = Integer.parseInt(time.getText().toString());
				Programm.setRestsDuration(duration);
				durationMain.setText(Utils.MStoString(Programm.getMainRecord()
						.getDuration()));
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int p2) {
						dialog.cancel();
					}
				});
		builder.show();
	}

	private void openDialogAdd() {
		DialogEdit dialog = new DialogEdit(mContext, null, false);
		dialog.callback = new DialogEdit.MyCallback() {

			@Override
			public void callbackOkNew(Record newRecord) {

				Programm.addRecord(newRecord, selectedRecord);

				selectedRecord = newRecord;
				listViewRecords.setItemChecked(Programm.getIndex(newRecord),
						true);

				adapter.notifyDataSetChanged();
			}

			@Override
			public void callbackOk() {
				adapter.notifyDataSetChanged();
			}
		};
		dialog.show();
	}

	private void openDialogEdit(Record record, boolean isGroup) {
		DialogEdit dialog = new DialogEdit(mContext, record, isGroup);
		dialog.callback = new DialogEdit.MyCallback() {

			@Override
			public void callbackOkNew(Record newRecord) {

			}

			@Override
			public void callbackOk() {
				adapter.notifyDataSetChanged();
			}
		};
		dialog.show();

	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				// dialog.get

				listViewRecords.setItemChecked(
						Programm.getIndex(selectedRecord), false);

				Programm.deleteRecord(selectedRecord);
				selectedRecord = null;

				adapter.notifyDataSetChanged();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
		}
	};

	private boolean isLocked() {
		if (Programm.isLocked()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage("File is locked.")
					.setCancelable(true)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			return true;
		}

		return false;
	}
	
}
