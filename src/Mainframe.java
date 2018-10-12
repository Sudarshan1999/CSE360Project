import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Mainframe extends JPanel implements ActionListener {
	protected JTextField ActivityField;
	protected JTextField ActivityDuration;
	protected JTextField dependencyField;

	public Mainframe() {
		// set layout to gridbag
		super(new GridBagLayout());

		// define buttons etc
		ActivityField = new JTextField(20);
		ActivityField.addActionListener(this);
		JLabel ActivityLabel = new JLabel("Enter your activities: ");

		ActivityDuration = new JTextField(20);
		JLabel ActivityDurationLabel = new JLabel("Enter the duration: ");
		ActivityDuration.addActionListener(this);

		
		dependencyField = new JTextField(20);
		JLabel DependencyLabel = new JLabel("Enter the dependencyField: ");

		JButton Help = new JButton("Help");
		JButton Finish = new JButton("Finish");
		
		JButton AddActivity = new JButton("Add Activity");
		
		//add activity action implementation
		AddActivity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String activityName = ActivityField.getText();
				String activityDuration = ActivityDuration.getText();
				String dependencyNames = dependencyField.getText();
			}
		});

		GridBagConstraints gc = new GridBagConstraints();

		// add components to panel
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		// help button
		gc.gridx = 0;
		gc.gridy = 0;
		// gridbag.setConstraints(Help, gc);
		add(Help, gc);
		// finish button
		gc.gridx = 1;
		gc.gridy = 0;
		// gridbag.setConstraints(Finish, gc);
		add(Finish, gc);

		// activity field
		gc.gridx = 0;
		gc.gridy = 1;

		add(ActivityLabel, gc);
		gc.gridx = 1;
		gc.gridy = 1;

		add(ActivityField, gc);

		// duration field
		gc.gridx = 0;
		gc.gridy = 2;

		add(ActivityDurationLabel, gc);
		gc.gridx = 1;
		gc.gridy = 2;
		add(ActivityDuration, gc);

		// dependency field
		gc.gridx = 0;
		gc.gridy = 3;

		add(DependencyLabel, gc);
		gc.gridx = 1;
		gc.gridy = 3;
		add(dependencyField, gc);
		// add activity
		gc.gridx = 0;
		gc.gridy = 4;
		add(AddActivity, gc);

	}

	public void actionPerformed(ActionEvent activityEvt) {
		String text = ActivityField.getText();
		// textArea.append(text + newline);
		// textField.selectAll();

		// Make sure the new text is visible, even if there
		// was a selection in the text area.
		// textArea.setCaretPosition(textArea.getDocument().getLength());
		System.out.println(text);

	}
	
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("TextDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add contents to the window.
		frame.add(new Mainframe());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
				String[] dependencyNameArray = dependencyNames.split("");
				Activity myActivity = new Activity(activityName, duration, dependencyNames);
				AddToPath(myActivity);
			}
		});
	}
}
