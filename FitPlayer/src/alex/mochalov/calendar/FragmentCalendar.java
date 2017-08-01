package alex.mochalov.calendar;

import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
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

public class FragmentCalendar extends Fragment {
	private Activity mContext;
	private View rootView;

	public FragmentCalendar(Activity context) {
		super();
		mContext = context;
	}

	public FragmentCalendar() {
		super();
	}

	public void setParams(Activity context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

		//Bundle args = getArguments();
		//Utils.setFileName(args.getString("name", ""));
		// textViewFileName.setText(fileName);


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
		return super.onOptionsItemSelected(item);
	}
	
}
