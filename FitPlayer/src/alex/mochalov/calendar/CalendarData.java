package alex.mochalov.calendar;
import alex.mochalov.record.*;
import java.util.*;

public class CalendarData
{
	static HashMap<Date, ArrayList<Prog>>  hm = new HashMap<Date, ArrayList<Prog>>();

	public static ArrayList<Prog> get(Date date)
	{
		if (hm.get(date) == null)
			return new ArrayList<Prog>();
		else return hm.get(date);
	}

	public static void replace(Date date, ArrayList<Prog> programms)
	{
		hm.put(date, programms);
	}}
