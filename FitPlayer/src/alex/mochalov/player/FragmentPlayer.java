package alex.mochalov.player;

import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.media.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import java.io.*;
import java.util.*;

public class FragmentPlayer extends Fragment
{
	public static final String TAG_FRAGMENT_PLAYER = "TAG_FRAGMENT_PLAYER";
	
	private Activity mContext;
	//Fragment thisFragment;
	private View rootView;

	private TextView textViewTimer;
	private TextView textViewFullTime;
	
	private TextView textViewName;
	private TextView textViewText;
	private BImageView bImageView;

	private ImageView imageViewRepeat;
	private ImageView imageViewSound;
	
	private AdapterPlayer adapter; 
	private ListView listViewRecords;
	private TextView textViewNameE;
	private TextView textViewTextE;

	enum State {isRunning, isPaused, isStopped};
	private State state = State.isStopped;
	
	private ArrayList<Record> records;
	private int mIndex;
	
	private long restOfTime = 0;
	private long restOfFullTime = 0;
	
	private boolean restartMusic = true;
	
	private boolean[] counter = {false, false, false};
	
	private boolean repeat = false;
	
	private boolean mWaiting = false;

	

	public FragmentPlayer(Activity context){
		super();
		mContext = context;
	}
	
	public FragmentPlayer(){
		super();
	}

	
	public void setParams(Activity context){
		mContext = context;
	}
	
	//public void start()
	//{
	//	if (mIndex >= 0 && mIndex < records.size())
	//		start(records.get(mIndex));
	//}


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.fragment_player, container, false);

		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
							  //        WindowManager.LayoutParams.MATCH_PARENT);
        
        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		textViewTimer = (TextView)rootView.findViewById(R.id.TextViewTimer);
		textViewFullTime = (TextView)rootView.findViewById(R.id.TextViewFullTime);
		
		textViewName = (TextView)rootView.findViewById(R.id.TextViewName);
		textViewText = (TextView)rootView.findViewById(R.id.TextViewText);
		
		imageViewRepeat = (ImageView)rootView.findViewById(R.id.imageViewRepeat);
		imageViewRepeat.setVisibility(View.INVISIBLE);
		
		imageViewSound = (ImageView)rootView.findViewById(R.id.imageViewSound);
		imageViewSound.setVisibility(View.INVISIBLE);
		
		bImageView = (BImageView)rootView.findViewById(R.id.imageViewPlayPause);
		bImageView.callback = new BImageView.MyCallback(){
			@Override
			public void callbackTouch() {
				StartPause();
			}}; 
		
		listViewRecords = (ListView)rootView.findViewById(R.id.ListViewRecords); 
		listViewRecords.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		Bundle args = getArguments();
		String fileName = args.getString("name", "");
		
		Programm.loadXML(mContext, fileName);
		
		records = Programm.getList();
        adapter = new AdapterPlayer(mContext, records);

		listViewRecords.setAdapter(adapter);
		listViewRecords.setOnItemClickListener( new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
					mIndex = index;
					
					if (state == State.isRunning) {

			        	//timerHandler.removeCallbacks(timerRunnable);
			            //isRunning = false;

			        } else {
						
						state = State.isStopped;
						restartMusic = true;
						
			        	listViewRecords.setItemChecked(index, true);
			            setTextViewTimer(textViewTimer, records.get(mIndex).getDuration());
			            setTextViewTimer(textViewFullTime, Programm.getDurationRest(records.get(mIndex)));
			            setTextViewGroup(records.get(mIndex));

			        }

				}}
		);	

		LinearLayout linearLayoutExpand = (LinearLayout)rootView.findViewById(R.id.linearLayoutExpand);
		textViewNameE = (TextView)rootView.findViewById(R.id.textViewNameE);
		textViewTextE = (TextView)rootView.findViewById(R.id.textViewTextE);
		
		if (Programm.isExpand_text()){
			linearLayoutExpand.setVisibility(View.VISIBLE);
			listViewRecords.setVisibility(View.INVISIBLE);
		} else {
			linearLayoutExpand.setVisibility(View.INVISIBLE);
			listViewRecords.setVisibility(View.VISIBLE);
		}
		
       
        if (records.size() > 0){
			mIndex = 0;
    		listViewRecords.setItemChecked(0, true);
        	setTextViewTimer(textViewTimer, records.get(mIndex).getDuration());
        	setTextViewTimer(textViewFullTime, Programm.getMainRecord().getDuration());
            setTextViewGroup(records.get(mIndex));
            
            if (Programm.isExpand_text()){
            	textViewNameE.setText(records.get(mIndex).getName());
            	textViewTextE.setText(records.get(mIndex).getText());
            }
            
        };

		state = State.isStopped;
		setButtonImage();
		
		Media.loadMediaFiles(mContext);
		
		TtsUtils.callback = new TtsUtils.MyCallback() {
			@Override
			public void speakingDone(String param) {
				
				mContext.runOnUiThread(new Runnable() {
				     @Override
				     public void run() {
							imageViewSound.setVisibility(View.INVISIBLE);
				    }
				});				
				
            	if (mWaiting){
    				Record record = records.get(mIndex);
    				if (param.equals("name")){
    					
    					mContext.runOnUiThread(new Runnable() {
    					     @Override
    					     public void run() {
    								imageViewSound.setVisibility(View.VISIBLE);
    					    }
    					});				
    					//imageViewSound.setVisibility(View.VISIBLE);
    					
    					TtsUtils.speak(record.getText(), "text",  
    						Programm.isMusic_quieter(), true);
    				} else if (param.equals("text")){
    					timerHandler.postDelayed(timerRunnable, 0); 
    				}
            	}
			}
		}; 
		
		mContext. getActionBar().setTitle(mContext.getResources().getString(R.string.run));
		mContext. getActionBar().setDisplayHomeAsUpEnabled(true);
		
		return rootView;
	}
	
	protected void setTextViewGroup(Record record) {

		Record group = Programm.getGroup(record);
		textViewName.setText(group.getName());
		textViewText.setText(group.getText());
		
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_player, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
    public void onPause() {
        super.onPause();
        
		Media.stop();
        
        timerHandler.removeCallbacks(timerRunnable);
    }	
	
	//runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

        	restOfTime = restOfTime - 100;
        	restOfFullTime = restOfFullTime - 100;

        	if (restOfTime <= 0){

        		adapter.setEnabled(true);
        		timerHandler.removeCallbacks(timerRunnable);
            	getNextRecord();

        	} else {

        		if (Programm.isCountBeforeTheEndOn()){
        			mWaiting = false;
        			if (!counter[0] && restOfTime <= 3200){
    					imageViewSound.setVisibility(View.VISIBLE);
        		    	TtsUtils.speak("3", "",  
        		    			false, false);
        		    	counter[0] = true;
        			}	
        			else if (!counter[1] && restOfTime <= 2200){
    					imageViewSound.setVisibility(View.VISIBLE);
        		    	TtsUtils.speak("2", "", 
        		    			false, false);
        		    	counter[1] = true;
        			}	
        			else if (!counter[2] && restOfTime <= 1200){
    					imageViewSound.setVisibility(View.VISIBLE);
        		    	TtsUtils.speak("1", "", 
        		    			false, true);
        		    	counter[2] = true;
        			}	
        				
        		}
        		
        		setTextViewTimer(textViewTimer, restOfTime);
        		setTextViewTimer(textViewFullTime, restOfFullTime);
        		
                timerHandler.postDelayed(this, 100);

        	}

        }
    };
	
	private void setTextViewTimer(TextView textView, long time){
	    int seconds = (int) ((time) / 1000);
	    int minutes = seconds / 60;
	    seconds = seconds % 60;
	    textView.setText(String.format("%02d:%02d", minutes, seconds));
	}

	private boolean getNextRecord(){

		counter[0] = false;
		counter[1] = false;
		counter[2] = false;
		
        if (mIndex < records.size()-1){
			mIndex++;
			if (repeat)
				mIndex--;

            if (Programm.isExpand_text()){
            	textViewNameE.setText(records.get(mIndex).getName());
            	textViewTextE.setText(records.get(mIndex).getText());
            }
			
    		listViewRecords.setItemChecked(mIndex, true);
    		listViewRecords.smoothScrollToPositionFromTop(mIndex, 0, 500);

    		start(records.get(mIndex));

            setTextViewGroup(records.get(mIndex));
            
            return true;
        } else {
        	state = State.isStopped;
        	setButtonImage();
            timerHandler.removeCallbacks(timerRunnable);

        	return false;
        }
	}
	
	/**
	 * Starts playing the incoming Record
	 * 
	 * @param record - incoming Record
	 */
	private void start(Record record){
		state = State.isRunning;
		
		setButtonImage(); // Set image Pause 

		listViewRecords.smoothScrollToPositionFromTop(mIndex, 0, 500); // Move record to the top
		
		restOfTime = record.getDuration() + 1000; // Add a small reserve of the time
		restOfFullTime = Programm.getDurationRest(record);

    	adapter.setEnabled(false); // Lock the list of the records
    	
    	Media.restart(mContext, restartMusic);
    	restartMusic = false;

		setTextViewTimer(textViewTimer, restOfTime);
		setTextViewTimer(textViewFullTime, restOfFullTime);
		
		mWaiting = true;
		imageViewSound.setVisibility(View.VISIBLE);
    	TtsUtils.speak(record.getName(), "name", 
			Programm.isMusic_quieter(), true);

	}
	
	private void reStart(){
		
		Media.start();
    	
		state = State.isRunning;
		setButtonImage();

    	adapter.setEnabled(false);
        timerHandler.postDelayed(timerRunnable, 0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id){
			case android.R.id.home:
				getActivity().onBackPressed();
				return true;
            case R.id.action_settings:
				return true;
			case R.id.action_repeat:
				repeat = !repeat;
			
				if (repeat)
					imageViewRepeat.setVisibility(View.VISIBLE);
				else
					imageViewRepeat.setVisibility(View.INVISIBLE);
				
				return true;
			default:	
				return super.onOptionsItemSelected(item);
		}
	}

	protected void StartPause() {

		if (state == State.isStopped){
			start(records.get(mIndex));
		} else if (state == State.isPaused){
			reStart();
		} else {
			Media.pause();
			
			state = State.isPaused;
			setButtonImage();
			timerHandler.removeCallbacks(timerRunnable);
			adapter.setEnabled(true);
		}
	}

	private void setButtonImage() {
		AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
		animation1.setDuration(1000);
		animation1.setStartOffset(1000);
		animation1.setFillAfter(true);
		

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			if (state == State.isStopped)
				bImageView.setImageResource(R.drawable.go_vert);
			else if (state == State.isRunning)
				bImageView.setImageResource(R.drawable.stop_vert);
			else if (state == State.isPaused)
				bImageView.setImageResource(R.drawable.go_vert);
		} else {
			if (state == State.isStopped)
				bImageView.setImageResource(R.drawable.go_hor);
			else if (state == State.isRunning)
				bImageView.setImageResource(R.drawable.stop_hor);
			else if (state == State.isPaused)
				bImageView.setImageResource(R.drawable.go_hor);
		}
		
		
		bImageView.startAnimation(animation1);					
        
		;
	}
	
}
