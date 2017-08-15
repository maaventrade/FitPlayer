package alex.mochalov.calendar;

import java.util.Calendar;

import alex.mochalov.editor.DialogEditMain;
import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.ExpandableListView.*;
import android.view.View.OnClickListener;

public class FragmentCalendar extends Fragment {
	private Activity mContext;
	private View rootView;
	//private String[] files;

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

		CalendarData.loadXML(mContext);
		
		rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

		ViewPager viewPager = (ViewPager)rootView. findViewById(R.id.viewPager);
		
		ViewCalendar viewCalendar = (ViewCalendar)rootView. findViewById(R.id.viewCalendar);
		
		Calendar cal = Calendar.getInstance();
		
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		viewPager.setAdapter(new CustomPagerAdapter(mContext));		
		viewPager.setCurrentItem((year - 2000) * 12 + month);
        
		//Bundle args = getArguments();
		//files = args.getStringArray("files");		
		//viewCalendar.setFiles(files);
		
		// textViewFileName.setText(fileName);

		mContext.getActionBar().setTitle(
				mContext.getResources().getString(R.string.calendar));
		mContext.getActionBar().setDisplayHomeAsUpEnabled(true);

		return rootView;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_calendar, menu);
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

		CalendarData.save(mContext);
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		// mVIsible = false;
		super.onDestroyView();
	}

	class CustomPagerAdapter extends PagerAdapter {

	    private Context mContext;

	    public CustomPagerAdapter(Context context) {
	        mContext = context;
	    }

	    @Override
	    public Object instantiateItem(ViewGroup collection, int position) {
	        //CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
	        LayoutInflater inflater = LayoutInflater.from(mContext);
	        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.calendar, collection, false);
	        
	        ViewCalendar viewCalendar = (ViewCalendar)layout.findViewById(R.id.viewCalendar);
	        viewCalendar.init(position);
	        
	        collection.addView(layout);
	        return layout;
	    }

	    @Override
	    public void destroyItem(ViewGroup collection, int position, Object view) {
	        collection.removeView((View) view);
	    }

	    @Override
	    public int getCount() {
	        return 2400;
	    }

	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == object;
	    }

	    @Override
	    public CharSequence getPageTitle(int position) {
	        return "XXXXXXXXX";
	    }

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			getActivity().onBackPressed();
			return true;
		case R.id.action_edit:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
