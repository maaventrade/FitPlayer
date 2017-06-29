package alex.mochalov.player;
import alex.mochalov.fitplayer.*;
import alex.mochalov.record.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.util.*;

public class FragmentPlayer extends Fragment
{
	public static final String TAG_FRAGMENT_PLAYER = "TAG_FRAGMENT_PLAYER";
	
	Context mContext;
	//Fragment thisFragment;
	private View rootView;

	private TextView textViewTimer;
	private TextView textViewName;
	private TextView textViewText;
	private BImageView bImageView;
	

    AdapterPlayer adapter; 
	ListView listViewRecords;

	enum State {isRunning, isPaused, isStopped};
	private State state = State.isStopped;
	
	private ArrayList<Record> records;
	private int mIndex;

	long restOfTime = 0;
	
	public FragmentPlayer(Context context){
		super();
		mContext = context;
	}
	
	public FragmentPlayer(){
		super();
	}

	
	public void setParams(Context context){
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
		
		textViewTimer = (TextView)rootView.findViewById(R.id.TextViewTimer);
		textViewName = (TextView)rootView.findViewById(R.id.TextViewName);
		textViewText = (TextView)rootView.findViewById(R.id.TextViewText);
		
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
						
			        	listViewRecords.setItemChecked(index, true);
			            setTextViewTimer(records.get(mIndex).getDuration());
			            setTextViewGroup(records.get(mIndex));

			        }

				}}
		);	

       
        if (records.size() > 0){
			mIndex = 0;
    		listViewRecords.setItemChecked(0, true);
        	setTextViewTimer(records.get(mIndex).getDuration());
            setTextViewGroup(records.get(mIndex));
        };

		state = State.isStopped;
		setButtonImage();
	
		
		
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

        if (mIndex < records.size()){
			mIndex++;
			
        	//int position = 0; // mainFolder.getIndex();
    		listViewRecords.setItemChecked(mIndex, true);
    		listViewRecords.smoothScrollToPositionFromTop(mIndex, 0, 500);

    		start(records.get(mIndex));

			//TtsUtils.speak(records.get(mIndex).getName()+"."+
			//records.get(mIndex).getText());

            setTextViewGroup(records.get(mIndex));
            
            return true;

        } else {

        	state = State.isStopped;
        	setButtonImage();
            timerHandler.removeCallbacks(timerRunnable);

        	return false;
        }
	}
	
	private void start(Record record){
		state = State.isRunning;
		setButtonImage();

		listViewRecords.smoothScrollToPositionFromTop(mIndex, 0, 500);
		
		restOfTime = record.getDuration() + 1000;
	

    	adapter.setEnabled(false);
        timerHandler.postDelayed(timerRunnable, 0);

		TtsUtils.speak(record.getText());
	}
	
	private void reStart(){
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
			case R.id.action_settings:
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
		
		if (state == State.isStopped)
			bImageView.setImageResource(R.drawable.go_96);
		else if (state == State.isRunning)
			bImageView.setImageResource(R.drawable.stop_96);
		else if (state == State.isPaused)
			bImageView.setImageResource(R.drawable.go_96);
		
		bImageView.startAnimation(animation1);					
        
		;
	}
	
}
