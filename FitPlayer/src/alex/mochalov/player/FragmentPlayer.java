package alex.mochalov.player;

import alex.mochalov.editor.DialogEditMain;
import alex.mochalov.fitplayer.R;
import alex.mochalov.main.*;
import alex.mochalov.programm.Programm;
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

public class FragmentPlayer extends Fragment {
	public static final String TAG_FRAGMENT_PLAYER = "TAG_FRAGMENT_PLAYER";

	private Activity mContext;
	// Fragment thisFragment;
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

	enum State {
		isRunning, isPaused, isStopped
	};

	private ArrayList<Record> records;
	
	public class RunParams{
		int index;
		long restOfTime = 0;
		long restOfFullTime = 0;
		State state = State.isStopped;
	};
	private static RunParams runParams;

	private boolean restartMusic = true;

	private boolean[] counter = { false, false, false };

	private boolean repeat = false;

	private boolean mWaiting = false;

	private LinearLayout linearLayoutExpand;

	private DialogEditMain dialogEditMain;
	
	private String mFileName;

	public FragmentPlayer(Activity context) {
		super();
		mContext = context;
	}

	public FragmentPlayer() {
		super();
	}

	public void setParams(Activity context) {
		mContext = context;
	}

	// public void start()
	// {
	// if (runParams.index >= 0 && runParams.index < records.size())
	// start(records.get(runParams.index));
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		rootView = inflater.inflate(R.layout.fragment_player, container, false);

		// getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
		// WindowManager.LayoutParams.MATCH_PARENT);

		mContext.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		textViewTimer = (TextView) rootView.findViewById(R.id.TextViewTimer);
		textViewFullTime = (TextView) rootView
				.findViewById(R.id.TextViewFullTime);

		textViewName = (TextView) rootView.findViewById(R.id.TextViewName);
		textViewText = (TextView) rootView.findViewById(R.id.TextViewText);

		imageViewRepeat = (ImageView) rootView
				.findViewById(R.id.imageViewRepeat);
		imageViewRepeat.setVisibility(View.INVISIBLE);

		imageViewSound = (ImageView) rootView.findViewById(R.id.imageViewSound);
		imageViewSound.setVisibility(View.INVISIBLE);

		bImageView = (BImageView) rootView
				.findViewById(R.id.imageViewPlayPause);
		bImageView.callback = new BImageView.MyCallback() {
			@Override
			public void callbackTouch() {
				StartPause();
			}
		};

		listViewRecords = (ListView) rootView
				.findViewById(R.id.ListViewRecords);
		listViewRecords.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		Bundle args = getArguments();
		mFileName = args.getString("name", "");

////
		records = Programm.getList();
		adapter = new AdapterPlayer(mContext, records);

		listViewRecords.setAdapter(adapter);
		listViewRecords
				.setOnItemClickListener(new ListView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> p1, View p2,
							int index, long p4) {
						runParams.index = index;

						if (runParams.state == State.isRunning) {

							// timerHandler.removeCallbacks(timerRunnable);
							// isRunning = false;

						} else {

							runParams.state = State.isStopped;
							restartMusic = true;

							listViewRecords.setItemChecked(index, true);
							setTextViewTimer(textViewTimer, records.get(runParams.index)
									.getDuration());
							setTextViewTimer(textViewFullTime, Programm
									.getDurationRest(records.get(runParams.index)));
							setTextViewGroup(records.get(runParams.index));

							if (Programm.isExpand_text()) {
								textViewNameE.setText(records.get(runParams.index)
										.getName());
								textViewTextE.setText(records.get(runParams.index)
										.getText());
							}

						}

					}
				});

		linearLayoutExpand = (LinearLayout) rootView
				.findViewById(R.id.linearLayoutExpand);
		textViewNameE = (TextView) rootView.findViewById(R.id.textViewNameE);
		textViewTextE = (TextView) rootView.findViewById(R.id.textViewTextE);

		linearLayoutExpand.setVisibility(View.INVISIBLE);
		listViewRecords.setVisibility(View.VISIBLE);

		if (runParams == null){
			runParams = new RunParams();
			Programm.loadXML(mContext, mFileName);
			Media.loadMediaFiles(mContext);
			Media.newMediaPlayer(mContext);
		} else {
			Media.restart(mContext, true);
		}
		
		if (records.size() > 0) {
			//runParams.index = 0;
			listViewRecords.setItemChecked(0, true);
			
			
			if (isStopped()){
				setTextViewTimer(textViewTimer, records.get(runParams.index).getDuration());
				setTextViewTimer(textViewFullTime, Programm.getMainRecord()
						.getDuration());
			} else {
				setTextViewTimer(textViewTimer, runParams.restOfTime);
				setTextViewTimer(textViewFullTime, runParams.restOfFullTime);
			}
			
			setTextViewGroup(records.get(runParams.index));

			if (Programm.isExpand_text()) {
				textViewNameE.setText(records.get(runParams.index).getName());
				textViewTextE.setText(records.get(runParams.index).getText());
			}
		}
		;

		setButtonImage();

		TtsUtils.callback = new TtsUtils.MyCallback() {
			@Override
			public void speakingDone(String param) {

				mContext.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						imageViewSound.setVisibility(View.INVISIBLE);
					}
				});

				if (mWaiting) {
					Record record = records.get(runParams.index);
					if (param.equals("name")) {

						// imageViewSound.setVisibility(View.VISIBLE);
						if (Programm.isSpeach_descr()) {
							mContext.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									imageViewSound.setVisibility(View.VISIBLE);
								}
							});
							TtsUtils.speak(record.getText(), "text",
									Programm.isMusic_quieter(), true);

						} else
							timerHandler.postDelayed(timerRunnable, 0);
					} else if (param.equals("text")) {
						timerHandler.postDelayed(timerRunnable, 0);
					}
				}
			}
		};

		mContext.getActionBar().setTitle(
				mContext.getResources().getString(R.string.run));
		mContext.getActionBar().setDisplayHomeAsUpEnabled(true);

		if (isRunning())
			timerHandler.postDelayed(timerRunnable, 0);
		
		return rootView;
	}

	protected void setTextViewGroup(Record record) {

		Record group = Programm.getGroup(record);

		if (record != group) {
			textViewName.setText(group.getName());
			textViewText.setText(group.getText());
		} else {
			textViewName.setText("");
			textViewText.setText("");
		}

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_player, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPause() {
		super.onPause();
		Media.pause();
	}

	@Override
	public void onDestroy() {
		timerHandler.removeCallbacks(timerRunnable);
		super.onDestroy();
	}

	// runs without a timer by reposting this handler at the end of the runnable
	Handler timerHandler = new Handler();
	Runnable timerRunnable = new Runnable() {

		@Override
		public void run() {

			runParams.restOfTime = runParams.restOfTime - 100;
			runParams.restOfFullTime = runParams.restOfFullTime - 100;

			if (runParams.restOfTime <= 0) {

				adapter.setEnabled(true);
				timerHandler.removeCallbacks(timerRunnable);
				getNextRecord();

			} else {

				if (Programm.isCountBeforeTheEndOn()) {
					mWaiting = false;
					if (!counter[0] && runParams.restOfTime <= 3200) {
						imageViewSound.setVisibility(View.VISIBLE);
						TtsUtils.speak("3", "", false, false);
						counter[0] = true;
					} else if (!counter[1] && runParams.restOfTime <= 2200) {
						imageViewSound.setVisibility(View.VISIBLE);
						TtsUtils.speak("2", "", false, false);
						counter[1] = true;
					} else if (!counter[2] && runParams.restOfTime <= 1200) {
						imageViewSound.setVisibility(View.VISIBLE);
						TtsUtils.speak("1", "", false, true);
						counter[2] = true;
					}

				}

				setTextViewTimer(textViewTimer, runParams.restOfTime);
				setTextViewTimer(textViewFullTime, runParams.restOfFullTime);

				timerHandler.postDelayed(this, 100);

			}

		}
	};

	private void setTextViewTimer(TextView textView, long time) {
		int seconds = (int) ((time) / 1000);
		int minutes = seconds / 60;
		seconds = seconds % 60;
		textView.setText(String.format("%02d:%02d", minutes, seconds));
	}

	private boolean getNextRecord() {

		counter[0] = false;
		counter[1] = false;
		counter[2] = false;

		if (runParams.index < records.size() - 1) {
			runParams.index++;
			if (repeat)
				runParams.index--;

			if (Programm.isExpand_text()) {
				textViewNameE.setText(records.get(runParams.index).getName());
				textViewTextE.setText(records.get(runParams.index).getText());
			}

			listViewRecords.setItemChecked(runParams.index, true);
			listViewRecords.smoothScrollToPositionFromTop(runParams.index, 0, 500);

			start(records.get(runParams.index));

			setTextViewGroup(records.get(runParams.index));

			return true;
		} else {
			runParams.state = State.isStopped;
			setButtonImage();
			timerHandler.removeCallbacks(timerRunnable);

			return false;
		}
	}

	/**
	 * Starts playing the incoming Record
	 * 
	 * @param record
	 *            - incoming Record
	 */
	private void start(Record record) {
		runParams.state = State.isRunning;

		setButtonImage(); // Set image Pause

		listViewRecords.smoothScrollToPositionFromTop(runParams.index, 0, 500); // Move
																		// record
																		// to
																		// the
																		// top

		runParams.restOfTime = record.getDuration() + 1000; // Add a small reserve of the
													// time
		runParams.restOfFullTime = Programm.getDurationRest(record);

		adapter.setEnabled(false); // Lock the list of the records

		Media.restart(mContext, restartMusic);
		restartMusic = false;

		setTextViewTimer(textViewTimer, runParams.restOfTime);
		setTextViewTimer(textViewFullTime, runParams.restOfFullTime);

		mWaiting = true;
		imageViewSound.setVisibility(View.VISIBLE);
		
		TtsUtils.speak(record.getName(), "name", Programm.isMusic_quieter(),
				true);

	}

	private void reStart() {

		Media.start();

		runParams.state = State.isRunning;
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

		switch (id) {
		case android.R.id.home:
			getActivity().onBackPressed();
			return true;
		case R.id.action_parameters:
			final boolean music = Programm.isPlayMusicOn();
			final String musicPath = Programm.getPathToMp3();
			
			dialogEditMain = new DialogEditMain(mContext,
					Programm.getMainRecord(), true);
			dialogEditMain.callback = new DialogEditMain.MyCallback() {
				@Override
				public void callbackOk() {
					if (music != Programm.isPlayMusicOn() || musicPath != Programm.getPathToMp3()){
						Media.loadMediaFiles(mContext);
						Media.restart(mContext, true);
					}	
				}
			};

			dialogEditMain.show();

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

		if (isStopped()) {
			start(records.get(runParams.index));
		} else if (isPaused()) {
			reStart();
		} else {
			Media.pause();

			runParams.state = State.isPaused;
			setButtonImage();
			timerHandler.removeCallbacks(timerRunnable);
			adapter.setEnabled(true);
		}
	}

	private boolean isStopped(){
		return runParams.state == State.isStopped;
	}

	private boolean isPaused(){
		return runParams.state == State.isPaused;
	}

	private boolean isRunning(){
		return runParams.state == State.isRunning;
	}
	
	
	private void setButtonImage() {
		AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
		animation1.setDuration(1000);
		animation1.setStartOffset(1000);
		animation1.setFillAfter(true);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (isStopped())
				bImageView.setImageResource(R.drawable.go_vert);
			else if (isRunning())
				bImageView.setImageResource(R.drawable.stop_vert);
			else if (isPaused())
				bImageView.setImageResource(R.drawable.go_vert);
		} else {
			if (isStopped())
				bImageView.setImageResource(R.drawable.go_hor);
			else if (isRunning())
				bImageView.setImageResource(R.drawable.stop_hor);
			else if (isPaused())
				bImageView.setImageResource(R.drawable.go_hor);
		}

		if (Programm.isExpand_text()) {
			if (isRunning()) {
				linearLayoutExpand.setVisibility(View.VISIBLE);
				listViewRecords.setVisibility(View.INVISIBLE);
			} else {
				linearLayoutExpand.setVisibility(View.INVISIBLE);
				listViewRecords.setVisibility(View.VISIBLE);
			}
		}

		bImageView.startAnimation(animation1);

		;
	}
	
}
