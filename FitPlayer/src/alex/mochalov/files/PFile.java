package alex.mochalov.files;

import java.io.File;
import java.util.Date;

public class PFile {
		private String mName;
		private Boolean mIsDirectory;
		private Date mDate;
		
		public PFile(File file) {
			mName = file.getName();
			mIsDirectory = file.isDirectory();
			mDate = new Date(file.lastModified());
		}

		public PFile(PFile currentRecord, Date date) {
			mName = currentRecord.mName;
			mIsDirectory = currentRecord.mIsDirectory;
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
		
}
