package alex.mochalov.programms;
import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.util.*;

public class FragmentPlayer extends Fragment
{
	Context mContext;
	//Fragment thisFragment;
	private View rootView;

	TextView textViewTimer;

    AdapterPlayer adapter; 
	ListView listViewRecords;

	boolean isRunning = false;
	boolean isPaused = false;

	Record record;

	long restOfTime = 0;

	private Record mainFolder;
	
	public FragmentPlayer(Context context){
		super();
		mContext = context;
	}

	public void start()
	{
		//record = mainFolder.getRecord();
		//if (record != null)
		//	start(record);
	}




	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.fragment_player, container, false);

		textViewTimer = (TextView)rootView.findViewById(R.id.TextViewTimer);
		listViewRecords = (ListView)rootView.findViewById(R.id.ListViewRecords); 
		listViewRecords.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		Bundle args = getArguments();
		String name = args.getString("name", "");
		Log.d("","load");
		mainFolder = Utils.loadXML(mContext, name);

		Log.d("","mainFolder "+mainFolder);
		//mainFolder.log();
		
		
        adapter = new AdapterPlayer(mContext, mainFolder);

		listViewRecords.setAdapter(adapter);
		listViewRecords.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
			        if (isRunning) {

			        	timerHandler.removeCallbacks(timerRunnable);
			            isRunning = false;

			        } else {

			            //record = mainFolder.setRecord(index);
			        	listViewRecords.setItemChecked(index, true);
			            setTextViewTimer(record.getDuration());

			        }

				}}
		);	

//        record = mainFolder.getFirstRecord();
        
        if (record != null){

    		listViewRecords.setItemChecked(0, true);
        	setTextViewTimer(record.getDuration());

        };

		return rootView;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_player, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
    public void onPause() {
        super.onPause();

        isRunning = false;
        timerHandler.removeCallbacks(timerRunnable);

    }	
	
	//runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

        	restOfTime = restOfTime - 100;

        	if (restOfTime <= 0){

        		adapter.setEnabled(true);
        		timerHandler.removeCallbacks(timerRunnable);
            	getNextRecord();

        	} else {

        		setTextViewTimer(restOfTime);
                timerHandler.postDelayed(this, 100);

        	}

        }
    };
	
	private void setTextViewTimer(long time){
	    int seconds = (int) ((time) / 1000);
	    int minutes = seconds / 60;
	    seconds = seconds % 60;
	    textViewTimer.setText(String.format("%02d:%02d", minutes, seconds));
	}

	private boolean getNextRecord(){
       // record = mainFolder.getNextRecord();

        if (record != null){

        	int position = 0; // mainFolder.getIndex();
    		listViewRecords.setItemChecked(position, true);
    		listViewRecords.smoothScrollToPositionFromTop(position, 0, 500);

    		start(record);

			TtsUtils.speak(record.getText());

            return true;

        } else {

        	isRunning = false;
            timerHandler.removeCallbacks(timerRunnable);

        	return false;
        }
	}
	private void start(Record record){

		isRunning = true;
		isPaused = false;

        restOfTime = record.getDuration() + 1000;

    	adapter.setEnabled(false);
        timerHandler.postDelayed(timerRunnable, 0);

		TtsUtils.speak(record.getText());

	}

	public void pause(){

		if (isPaused){
			isPaused = false;
	        timerHandler.postDelayed(timerRunnable, 0);
		} else {
			isPaused = true;
			timerHandler.removeCallbacks(timerRunnable);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id){
			case R.id.action_pause:
				pause();
				return true;
			case R.id.action_settings:
				return true;
			case R.id.action_start:
				start();
				return true;
			default:	
				return super.onOptionsItemSelected(item);
		}
	}
	
}
