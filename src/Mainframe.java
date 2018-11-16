import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class Mainframe extends JPanel implements ActionListener
{
	protected JTextField ActivityField;
	protected JTextField ActivityDuration;
	protected JTextField dependencyField;

	static ArrayList<LinkedList<Activity>> paths;
	static ArrayList<Activity> activityList;
	static ArrayList<Activity> allActivities;
	String activityName;
	String activityDuration;
	String dependencyNames;
	String fileName;

	public Mainframe()
	{
		// set layout to gridbag
		super(new GridBagLayout());

		// define buttons etc
		ActivityField = new JTextField(20);
		ActivityField.addActionListener(this);
		JLabel ActivityLabel = new JLabel("Enter the Activity Name: ");

		ActivityDuration = new JTextField(20);
		JLabel ActivityDurationLabel = new JLabel("Enter the duration: ");
		ActivityDuration.addActionListener(this);

		dependencyField = new JTextField(20);
		JLabel DependencyLabel = new JLabel("Enter the dependencies: ");

		JButton Help = new JButton("Help");
		JButton Finish = new JButton("Finish");
		JButton About = new JButton("About");
		JButton Reset = new JButton("Reset");
		JButton DurationChanger = new JButton("Change Activity Duration");
		JButton FileMaker = new JButton("Make a new file");
		JButton displayCritical = new JButton("Display Critical Paths");

		JButton AddActivity = new JButton("Add Activity");

		Reset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				flushAll();
				allActivities.clear();
				JOptionPane.showMessageDialog(null, " All Data has been Reset. Program is ready to be used again");
			}
		});

		About.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(null,
						" \t\tFriday 3:05 pm Team 4. \nTeam Members: Sudarshan Kadalazhi,Jonah Anderson, Pawel Leszcyzynski, Hao Yang");
			}
		});

		Help.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(null,
						" In order to get started, enter an activity name, duration, and dependency list \n"
								+ "When you decide that you've entered in enough inputs, click on the finish option to process the information. \n"
								+ "The output will display a list of paths from start to finish listed in descending order by durations.");
			}

		});

		Finish.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int n = addActivityListToPath();
				if (n == 1)
					JOptionPane.showMessageDialog(null, "Nodes are not connected");
				else if (n == 2)
					JOptionPane.showMessageDialog(null, "Circular Dependency Created");
				else
				{
					String s = displayPathOrders(0);
					System.out.println(s);
					JOptionPane.showMessageDialog(null, s);
					try
					{
						createFile(fileName);
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});

		// add activity action implementation
		AddActivity.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				activityName = ActivityField.getText();
				activityDuration = ActivityDuration.getText();
				dependencyNames = dependencyField.getText();

				Activity current = new Activity();
				Activity copy = new Activity();
				try
				{
					current.duration = Integer.parseInt(activityDuration);
				} catch (NumberFormatException e1)
				{
					JOptionPane.showMessageDialog(null, "Duration must be an integer");
					return;
				}
				current.activityName = activityName;
				StringTokenizer str = new StringTokenizer(dependencyNames, ",");
				while (str.hasMoreTokens())
					current.dependencies.add(str.nextToken());
				
				System.out.println("Activity Name: "+ current.activityName);
				for(int i=0;i<current.dependencies.size();i++)
				{
					System.out.println(current.dependencies.get(i));
				}
				copy.activityName = current.activityName;
				copy.duration = current.duration;
				for(int i=0;i <current.dependencies.size();i++)
					copy.dependencies.add(current.dependencies.get(i));
				allActivities.add(copy);// use this to keep track of every activity entered
				createActivityList(current);
				ActivityField.setText("");
				ActivityDuration.setText("");
				dependencyField.setText("");
				printActivities();
			}
		});

		DurationChanger.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JTextField actField = new JTextField(5);
				JTextField durField = new JTextField(5);

				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Please enter an activity name:"));
				myPanel.add(actField);
				myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				myPanel.add(new JLabel("Please enter the new duration:"));
				myPanel.add(durField);

				int result = JOptionPane.showConfirmDialog(null, myPanel, "", JOptionPane.OK_CANCEL_OPTION);

				// Find activity in AllActivities
				String newName = actField.getText();
				int newDur = Integer.parseInt(durField.getText());

				for (int i = 0; i < allActivities.size(); i++)
				{
					if (allActivities.get(i).activityName.equals(newName))
						allActivities.get(i).duration = newDur;
				}

				flushAll();

				// Now recreate the activitiesList and the basic paths
				for (int i = 0; i < allActivities.size(); i++)
				{
					createActivityList(allActivities.get(i));
					System.out.println("Activity Name: "+ allActivities.get(i).activityName);
					if(allActivities.get(i).dependencies.size() != 0)
					{
						for(int j=0;j<allActivities.get(i).dependencies.size();j++)
							System.out.println(allActivities.get(i).dependencies.get(j)+" ");
					}
				}
			}

		});

		FileMaker.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				JTextField filenameField = new JTextField(5);

				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Please enter a name for the file:"));
				myPanel.add(filenameField);

				int result = JOptionPane.showConfirmDialog(null, myPanel, "", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					fileName = filenameField.getText();
				}

				int n = addActivityListToPath();
				if (n == 1)
					JOptionPane.showMessageDialog(null, "Nodes are not connected");
				else if (n == 2)
					JOptionPane.showMessageDialog(null, "Circular Dependency Created");
				else
				{
					try
					{
						createFile(fileName);
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});

		displayCritical.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int n = addActivityListToPath();
				if (n == 1)
					JOptionPane.showMessageDialog(null, "Nodes are not connected");
				else if (n == 2)
					JOptionPane.showMessageDialog(null, "Circular Dependency Created");
				String s = displayPathOrders(1);
				System.out.println(s);
				JOptionPane.showMessageDialog(null, s);
				try
				{
					createFile(fileName);
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}

		});

		GridBagConstraints gc = new GridBagConstraints();

		// add components to panel
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		// help button
		gc.gridx = 0;
		gc.gridy = 1;
		// gridbag.setConstraints(Help, gc);
		add(Help, gc);
		// finish button
		gc.gridx = 1;
		gc.gridy = 1;
		// gridbag.setConstraints(Finish, gc);
		add(Finish, gc);

		// activity field
		gc.gridx = 0;
		gc.gridy = 2;

		add(ActivityLabel, gc);
		gc.gridx = 1;
		gc.gridy = 2;

		add(ActivityField, gc);

		// duration field
		gc.gridx = 0;
		gc.gridy = 3;

		add(ActivityDurationLabel, gc);
		gc.gridx = 1;
		gc.gridy = 3;
		add(ActivityDuration, gc);

		// dependency field
		gc.gridx = 0;
		gc.gridy = 4;

		add(DependencyLabel, gc);
		gc.gridx = 1;
		gc.gridy = 4;
		add(dependencyField, gc);
		// add activity
		gc.gridx = 0;
		gc.gridy = 5;
		add(AddActivity, gc);
		// DurationChanger
		gc.gridwidth = 1;
		gc.gridx = 1;
		gc.gridy = 6;
		add(DurationChanger, gc);
		// FileMaker
		gc.gridwidth = 1;
		gc.gridx = 0;
		gc.gridy = 6;
		add(FileMaker, gc);
		// Reset button
		gc.gridwidth = 2;
		gc.gridx = 0;
		gc.gridy = 0;
		add(Reset, gc);
		// About button
		gc.gridwidth = 1;
		gc.gridx = 1;
		gc.gridy = 5;
		add(About, gc);
		// Display Critical
		gc.gridwidth = 2;
		gc.gridx = 0;
		gc.gridy = 7;
		add(displayCritical, gc);

	}

	protected void flushAll()
	{
		paths.clear();
		activityList.clear();
	}

	protected void createActivityList(Activity current)
	{
		if (current.dependencies.size() == 0)
		{
			LinkedList<Activity> action = new LinkedList<Activity>();
			action.add(current);
			paths.add(action);
		} else
		{
			activityList.add(current);
		}
	}

	//Key is 1 for display critical path and 0 normally
	protected String displayPathOrders(int key)
	{
		ArrayList<SortHelper> values = new ArrayList<>();
		for (int i = 0; i < paths.size(); i++)
		{
			SortHelper value = new SortHelper(i, 0);
			for (int j = 0; j < paths.get(i).size(); j++)
			{
				value.duration += paths.get(i).get(j).duration;
			}
			values.add(value);
		}
//		System.out.println("Program actually gets here");
//		for (int i = 0; i < values.size(); i++)
//			System.out.println(values.get(i).duration);
		// Sort the list
		int n = values.size();
		for (int i = 0; i < n - 1; i++)
		{
			// Find the max
			int max = i;
			for (int j = i + 1; j < n; j++)
			{
				if (values.get(j).duration > values.get(max).duration)
					max = j;
			}
			// Swap the found max element
			SortHelper temp = new SortHelper(values.get(max).key, values.get(max).duration);
			values.get(max).key = values.get(i).key;
			values.get(max).duration = values.get(i).duration;
			values.get(i).duration = temp.duration;
			values.get(i).key = temp.key;
		}
//		System.out.println("After Sort");
//		for (int i = 0; i < values.size(); i++)
//			System.out.println(values.get(i).duration);
		// Now the list is sorted in decending order by key and duration.
		// Just print in order of the keys in values
		String str = "";
		for (int i = 0; i < values.size(); i++)
		{
			LinkedList<Activity> list = paths.get(values.get(i).key);
			for (int j = 0; j < list.size(); j++)
			{
				str += list.get(j).activityName + " ";
			}
			str += "\t Duration: " + values.get(i).duration + " ";
			str += "\n";
		}
		if (key == 0)
			return str;
		else
		{
			int critDur = values.get(0).duration;
			int stopIndex = 0;
			str = "";
			for (int i = 1; i < values.size(); i++)
			{
				if (values.get(i).duration >= critDur)
					stopIndex++;
			}
			for (int i = 0; i <= stopIndex; i++)
			{
				LinkedList<Activity> list = paths.get(values.get(i).key);
				System.out.println("Getting paths["+values.get(i).key+"]");
				for (int j = 0; j < list.size(); j++)
				{
					str += list.get(j).activityName + " ";
				}
				str += "\t Duration: " + values.get(i).duration + " ";
				str += "\n";
			}
			return str;
		}
	}

	protected int addActivityListToPath()
	{
		convertToSingleDependency();
		int pass = checkCircularReference();
		if (pass != 0)
			return pass;
		int errors = 0;
		int i = 0;
		while (activityList.size() > 0)
		{
			int n = addDependencyToList(activityList.get(0).dependencies.get(0), activityList.get(i));
			if (errors > activityList.size())
				return 1;

			if (activityList.size() == 0)
				return 0;

			if (n == 0)
			{
				errors = 0;
				activityList.remove(0);
			} else
			{
				errors++;
				activityList.add(activityList.get(0));
				activityList.remove(0);
			}
			printActivities();
		}
		if (checkAdvancedCircularReference() != 0)
			return 2;
		deleteDuplicates();
		printActivities();
		return 0;
	}

	private void deleteDuplicates()
	{
		boolean flag = false;;
		ArrayList<String> checkDuplicate = new ArrayList<String>();
		//Prefill with values from first path
		for (int j = 0; j < paths.get(0).size(); j++)
		{
			checkDuplicate.add(paths.get(0).get(j).activityName);
		}
		for (int i = 1; i < paths.size(); i++)
		{
			flag = false;
			for(int j=0;j<paths.get(i).size();j++)
			{
				//If these are different, then populate duplicate Array with the next set to check
				if(checkDuplicate.get(j) != paths.get(i).get(j).activityName)
				{
					for(int k=0;k<paths.get(i).size();k++)
					{
						checkDuplicate.set(k,paths.get(i).get(k).activityName);
					}
					flag = true;
					break;
				}
			}
			//flag is false, duplicate path
			if(!flag)
			{
				paths.remove(i);
				i--;
			}
		}
	}

	private int checkAdvancedCircularReference()
	{
		ArrayList<String> checkDuplicate;
		for (int i = 0; i < paths.size(); i++)
		{
			checkDuplicate = new ArrayList<String>();
			for (int j = 0; j < paths.get(i).size(); j++)
			{
				if (checkDuplicate.size() == 0)
					checkDuplicate.add(paths.get(i).get(j).activityName);
				else
				{
					if (checkDuplicate.contains(paths.get(i).get(j).activityName))
						return 1;
					else
						checkDuplicate.add(paths.get(i).get(j).activityName);
				}
			}
		}
		return 0;

	}

	protected int checkCircularReference()
	{
		for (int i = 0; i < activityList.size(); i++)
		{
			for (int j = 0; j < activityList.size(); j++)
			{
				if (activityList.get(i).dependencies.get(0).equals(activityList.get(j).activityName)
						&& activityList.get(j).dependencies.get(0).equals(activityList.get(i).activityName))
					return 2;
			}
		}
		return 0;
	}

	protected void convertToSingleDependency()
	{
		for (int i = 0; i < activityList.size(); i++)
		{
			if (activityList.get(i).dependencies.size() > 1)
			{
				for (int j = activityList.get(i).dependencies.size() - 1; j >= 1; j--)
				{
					Activity copy = new Activity();
					copy.activityName = activityList.get(i).activityName;
					copy.dependencies.add(activityList.get(i).dependencies.get(j));
					copy.duration = activityList.get(i).duration;
					activityList.get(i).dependencies.remove(j);
					activityList.add(copy);
				}
			}
		}
	}

	protected int addDependencyToList(String dependency, Activity newNode)
	{
		int added = 0;
		for (int i = 0; i < paths.size(); i++)
		{
			if (i > paths.size())
				break;
			for (int j = 0; j < paths.get(i).size(); j++)
			{
				// check if this is where new dependency needs to be put in
				if (paths.get(i).get(j).activityName.equals(dependency))
				{
					Activity copy = new Activity();
					copy.activityName = newNode.activityName;
					copy.dependencies.add(dependency);
					copy.duration = newNode.duration;
					if (j == paths.get(i).size() - 1)
					{
						paths.get(i).add(copy);
						added++;
					} else // not the last element so duplicate the path up to this point
					{
						LinkedList<Activity> newPath = new LinkedList<>();
						for (int k = 0; k <= j; k++)
						{
							// copy the activity from original path and add
							Activity newActivity = new Activity();
							newActivity.activityName = paths.get(i).get(k).activityName;
							newActivity.duration = paths.get(i).get(k).duration;
							if (k != 0)
								newActivity.dependencies.add(paths.get(i).get(k).dependencies.get(0));
							newPath.add(newActivity);
						}
						newPath.add(copy);
						paths.add(i + 1, newPath);
						added++;
						i++;// skip the next one because we just created it
					}
				}
			}
		}
		// added dependency at least one time
		if (added > 0)
			return 0;
		return -1;// not found in list, try again later
	}

	public void actionPerformed(ActionEvent activityEvt)
	{
		String text = ActivityField.getText();
		// textArea.append(text + newline);
		// textField.selectAll();

		// Make sure the new text is visible, even if there
		// was a selection in the text area.
		// textArea.setCaretPosition(textArea.getDocument().getLength());

	}

	private static void createAndShowGUI()
	{
		// Create and set up the window.
		JFrame frame = new JFrame("TextDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add contents to the window.
		frame.add(new Mainframe());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		paths = new ArrayList<>();
		activityList = new ArrayList<>();
		allActivities = new ArrayList<>();
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		});
	}

	public void AddToPath(Activity newNode, ArrayList<LinkedList<Activity>> paths)
	{
		// start
		if (newNode.dependencies.size() == 0)
		{
			LinkedList<Activity> newPath = new LinkedList<>();
			newPath.add(newNode);
			paths.add(newPath);
		} else
		{
			// check if this path
			for (int k = 0; k < newNode.dependencies.size(); k++)
			{

				for (int i = 0; i < paths.size(); i++)
				{
					for (int j = 0; j < paths.get(i).size(); j++)
					{
						// last element in linked list is the dependency
						if (paths.get(i).getLast().activityName.equalsIgnoreCase(newNode.dependencies.get(k)))
						{
							paths.get(i).add(newNode);
						}
					}
				}
			}
		}

	}

	public void printActivities()
	{
		for (int i = 0; i < paths.size(); i++)
		{
			for (int j = 0; j < paths.get(i).size(); j++)
			{
				System.out.print(paths.get(i).get(j).activityName + " ");
			}
			System.out.println();
		}
		System.out.println("Activity List");
		for (int i = 0; i < activityList.size(); i++)
		{
			System.out.print(activityList.get(i).activityName + " ");
		}
		System.out.println();
		System.out.println();
	}

	public void createFile(String FileName) throws IOException
	{
		String myFileName = FileName + ".txt";
		try
		{
//			System.out.println("IN createFile. Creating: " + myFileName);
			File file = new File(myFileName);
			FileWriter fileWriter = new FileWriter(file, false);
			PrintWriter writer = new PrintWriter(fileWriter);
			writer.println(myFileName);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			writer.println(dtf.format(now));
			writer.println("Activity List: ");
			for (int i = 0; i < allActivities.size(); i++)
			{
				writer.print(allActivities.get(i).activityName + " ");
				writer.println(allActivities.get(i).duration);
//				if(allActivities.get(i).dependencies.size() != 0)
//				{
//					for(int j=0;j<allActivities.get(i).dependencies.size();j++)
//						writer.print(allActivities.get(i).dependencies.get(j)+" ");
//				}
				writer.println();
			}
			writer.println();
			writer.println("Paths: ");
			int duration;
			for (int i = 0; i < paths.size(); i++)
			{
				duration = 0;
				for (int j = 0; j < paths.get(i).size(); j++)
				{
					writer.print(paths.get(i).get(j).activityName + " ");
					duration += paths.get(i).get(j).duration;
				}
				writer.print(duration);
				writer.println();
			}
			writer.println();
			writer.close();

		} catch (UnsupportedEncodingException e)
		{

		}

	}

}
