
import java.util.ArrayList;

public class Activity {
	String activityName;
	int duration;
	ArrayList<String> dependencies;

	public Activity(String activityName, int duration, ArrayList<String> dependencies) {
		this.activityName = activityName;
		this.duration = duration;
		this.dependencies = dependencies;
	}

	public String getActivityName() {
		return this.activityName;
	}

	public int getDuration() {
		return this.duration;
	}

	public ArrayList<String> getDependencies() {
		return this.dependencies;
	}

}
