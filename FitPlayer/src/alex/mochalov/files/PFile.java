package alex.mochalov.files;

import alex.mochalov.programm.*;
import android.content.*;

import java.io.*;
import java.util.*;

public class PFile {
		private String mName;
		private Boolean mIsDirectory;
		private Date mDate;
		
		public long mDuration;
		private boolean mLocked;
	private String mInfo;
		
	public PFile(File file) {
			mName = file.getName();
			mIsDirectory = file.isDirectory();
			mDate = new Date(file.lastModified());
			if (!mIsDirectory)
				Programm.loadXMLInfo(
	 mName, this);
		}

		public PFile(PFile currentRecord, Date date) {
			mName = currentRecord.mName;
			mIsDirectory = currentRecord.mIsDirectory;
		}

		public long getDuration()
		{
			return mDuration;
		}

		public void setDuration(int p0)
		{
		mDuration = p0;
		}

		public String getName() {
			return mName;
		}

		public Date getDate() {
			return mDate;
		}

		public boolean isDirectory() {
			return mIsDirectory;
		}
		
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
	}

	public void setName(String newFileName) {
		mName = newFileName;
		
	}
		
		
}
