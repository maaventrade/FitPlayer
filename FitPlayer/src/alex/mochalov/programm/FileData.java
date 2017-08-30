package alex.mochalov.programm;

public class FileData {
	public long duration;
	private boolean mLocked;

	private String mInfo;

	public void setInfo(String info)
	{
		mInfo = info;
	}

	public boolean isLocked()
	{
		
		return mLocked;
	}

	public void setLocked(boolean locked)
	{
		mLocked = locked;
	}


	public CharSequence getInfo()
	{
		
		return mInfo;
	}}
