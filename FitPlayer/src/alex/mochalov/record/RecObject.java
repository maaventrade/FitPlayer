package alex.mochalov.record;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import alex.mochalov.fitplayer.R;
import android.content.Context;
import android.widget.Toast;

public class RecObject {

	protected String mName;
	
	public RecObject(String name) {
		mName = name;
	}

	public long getDuration() {
		return 0;
	}

	public String getName() {
		return mName;
	}

	public boolean writeToFile(Context context, Writer writer, RecObject current) {
		
		try {
		
		if (current != null){
			if (current.getClass() == Record.class){
				writer.write("<record name=\""
						+current.getName()+"\""
						+" text=\""+((Record)current).getText()+"\""
						+" duration=\""+current.getDuration()+"\""
						+">"+"\n");
				writer.write("</record>"+"\n");
			}
				
			else {
				writer.write("<folder name=\""
						+current.getName()+"\""
						+">"+"\n");
				
				ArrayList<Record> subRecords = ((Folder)current).getRecords();
				for (Record s: subRecords)
					s.writeToFile(context, writer, s);
				
				writer.write("</folder>"+"\n");
			}
		}
		} catch (IOException e) {
			//Utils.setInformation(context.getResources().getString(R.string.error_save_file)+" "+e);
			Toast.makeText(context, context.getResources().getString(R.string.error_saving_file) +" "+e , Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
			
		
	}
	

}
