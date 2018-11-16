
import java.util.ArrayList;
import java.util.LinkedList;

public class Activity implements Comparable<Activity>
{
	String activityName;
	int duration;
	ArrayList<String> dependencies;

	public Activity(String activityName, int duration, ArrayList<String> dependencies)
	{
		this.activityName = activityName;
		this.duration = duration;
		this.dependencies = dependencies;
	}

	public Activity()
	{
		activityName = "";
		duration = 0;
		dependencies = new ArrayList<String>();
	}

	public String getActivityName()
	{
		return this.activityName;
	}

	public int getDuration()
	{
		return this.duration;
	}

	public ArrayList<String> getDependencies()
	{
		return this.dependencies;
	}

	@Override
	public int compareTo(Activity other)
	{

		return activityName.compareTo(other.activityName);
	}
}
