package alex.mochalov.record;

public class Prog
{
	
	private String mName;
	private boolean mCompleted;
	
	public Prog(String name) {
		mName = name;
		mCompleted = false;
	}

	public boolean isCompleted() {
		return mCompleted;
	}

	public CharSequence getName() {
		return mName;
	}
	
}
