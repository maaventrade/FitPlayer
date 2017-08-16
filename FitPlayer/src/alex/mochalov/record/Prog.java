package alex.mochalov.record;

public class Prog
{
	
	private String mName;
	private boolean mCompleted;
	
	public Prog(String name) {
		mName = name;
		mCompleted = false;
	}

	public Prog() {
		mName = "new";
		mCompleted = false;
	}

	public Prog(String name, boolean completed) {
		mName = name;
		mCompleted = completed;
	}

	public boolean isCompleted() {
		return mCompleted;
	}

	public String getName() {
		return mName;
	}

	public void changeCompleted() {
		mCompleted = !mCompleted;
	}
	
}
