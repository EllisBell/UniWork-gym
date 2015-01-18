import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;
import java.io.*;

/**
 * Defines a GUI that displays details of a FitnessProgram object
 * and contains buttons enabling access to the required functionality.
 */
public class SportsCentreGUI extends JFrame implements ActionListener {

	/** GUI JButtons */
	private JButton closeButton, attendanceButton;
	private JButton addButton, deleteButton;

	/** GUI JTextFields */
	private JTextField idIn, classIn, tutorIn;

	/** Display of class timetable */
	private JTextArea display;

	/** Display of attendance information */
	private ReportFrame report;

	/** Names of input text files */
	private final String classesInFile = "ClassesIn.txt";
	private final String classesOutFile = "ClassesOut.txt";
	private final String attendancesFile = "AttendancesIn.txt";

	/** FitnessProgram object */
	FitnessProgram fitProg;

	/**
	 * Constructor for AssEx3GUI class
	 */
	public SportsCentreGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Boyd-Orr Sports Centre");
		setSize(750, 300);
		setLocation(300,150);
		display = new JTextArea();
		display.setFont(new Font("Courier", Font.PLAIN, 14));
		display.setEditable(false);
		//add display to ScrollPane - n.b. useful only if max number of classes were to increase
		JScrollPane scroller = new JScrollPane(display);
		add(scroller, BorderLayout.CENTER);
		layoutTop();
		layoutBottom();

		fitProg = new FitnessProgram();
		initLadiesDay();
		initAttendances();
		updateDisplay();
	}

	/**
	 * Creates the FitnessProgram list ordered by start time
	 * using data from the file ClassesIn.txt. Calls method
	 * that reads from that file and passes contents to relevant
	 * FitnessProgram method
	 */
	public void initLadiesDay() {
		readFromFile(classesInFile);
	}

	/**
	 * Initialises the attendances using data
	 * from the file AttendancesIn.txt. Calls method
	 * that reads from that file and passes contents to relevant
	 * FitnessProgram method
	 */
	public void initAttendances() {
		readFromFile(attendancesFile);
	}
	
	/**
	 * Opens and reads lines from file.
	 * Depending on name of file, passes lines to relevant
	 * FitnessProgram method to either add class to list or
	 * populate attendances array of class
	 * @param fileToRead the file to be read and passed to FitProg
	 */
	private void readFromFile(String fileToRead) {
		FileReader reader = null;
		Scanner in = null;

		try {
			try {
				reader = new FileReader(fileToRead);
				in = new Scanner(reader);
				while(in.hasNextLine()) {
					String line = in.nextLine();
					if(fileToRead.equals(classesInFile))
						fitProg.addClassFromFile(line);
					else if(fileToRead.equals(attendancesFile))
						fitProg.populateAttendances(line);
				}
			}
			finally {
				if(reader != null)
					reader.close();
				if(in != null)
					in.close(); 
			}
		}
		catch(IOException e) {
			System.out.println("File not found");
		} 
	}

	/**
	 * Instantiates timetable display and adds it to GUI
	 */
	public void updateDisplay() {   
		StringBuilder timeLine = new StringBuilder(); //first line of display
		StringBuilder nameLine = new StringBuilder(); //second line of display
		StringBuilder tutorLine = new StringBuilder(); //third line of display

		int firstTime = FitnessProgram.FIRST_START_TIME;
		int lastTime = FitnessProgram.LAST_START_TIME;

		//cycle through each timeslot
		for(int time = firstTime; time <= lastTime; time++) {
			//add each timeslot to time line of display
			timeLine.append(String.format(" %-12s", time + "-" + (time+1)));
			//get FitnessClass object in relevant timeslot
			FitnessClass fc = fitProg.getClassAtTime(time); 
			if(fc != null) { //if there is a class at that time
				//get and add class name to name line of display
				String name = fc.getClassName();
				nameLine.append(String.format(" %-12s", name));
				//get and add tutor name to tutor line of display
				String tutor = fc.getClassTutor();
				tutorLine.append(String.format(" %-12s", tutor));
			}
			else { //if there is no class at that time
				//display indication that timeslot is vacant
				nameLine.append(String.format(" %-12s", "Available"));
				tutorLine.append(String.format(" %-12s", ""));
			}
		}
		timeLine.append("\n"); //add carriage return to first line
		nameLine.append("\n"); //add carriage return to second line
		//convert one of the StringBuilders to a String so they can all be concatenated
		String timetable = timeLine.toString() + nameLine + tutorLine;
		display.setText(timetable);
	}

	/**
	 * adds buttons to top of GUI
	 */
	public void layoutTop() {
		JPanel top = new JPanel();
		closeButton = new JButton("Save and Exit");
		closeButton.addActionListener(this);
		top.add(closeButton);
		attendanceButton = new JButton("View Attendances");
		attendanceButton.addActionListener(this);
		top.add(attendanceButton);
		add(top, BorderLayout.NORTH);
	}

	/**
	 * adds labels, text fields and buttons to bottom of GUI
	 */
	public void layoutBottom() {
		// instantiate panel for bottom of display
		JPanel bottom = new JPanel(new GridLayout(3, 3));

		// add upper label, text field and button
		JLabel idLabel = new JLabel("Enter Class Id");
		bottom.add(idLabel);
		idIn = new JTextField();
		bottom.add(idIn);
		JPanel panel1 = new JPanel();
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		panel1.add(addButton);
		bottom.add(panel1);

		// add middle label, text field and button
		JLabel nmeLabel = new JLabel("Enter Class Name");
		bottom.add(nmeLabel);
		classIn = new JTextField();
		bottom.add(classIn);
		JPanel panel2 = new JPanel();
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		panel2.add(deleteButton);
		bottom.add(panel2);

		// add lower label text field and button
		JLabel tutLabel = new JLabel("Enter Tutor Name");
		bottom.add(tutLabel);
		tutorIn = new JTextField();
		bottom.add(tutorIn);

		add(bottom, BorderLayout.SOUTH);
	}

	/**
	 * Processes adding a class
	 */
	public void processAdding() {
		//first check if there is room for another class
		if(fitProg.getEarliestTime() == FitnessProgram.LIST_IS_FULL) {
			JOptionPane.showMessageDialog(null, "There is no room for another class", 
					"Sorry", JOptionPane.ERROR_MESSAGE);
			clearTextFields();
		}
		else {
			//read user input from GUI
			String idNum = idIn.getText().trim();
			String cName = classIn.getText().trim();
			String cTutor = tutorIn.getText().trim();

			if(checkInputInfo(idNum, cName, cTutor)) { //checkInputInfo returns true if info is OK
				fitProg.addClassFromGui(idNum, cName, cTutor); //pass info to addNewClass of fitProg
				clearTextFields();
				updateDisplay(); //update display after new class is added
			}
		}
	}

	/**
	 * Checks id, name and tutor information input by user
	 * into GUI textfields
	 * @param id the class ID input by the user
	 * @param name the class name input by the user
	 * @param tutor the class tutor input by the user
	 * @return true if info is all OK, false otherwise
	 */
	private boolean checkInputInfo(String id, String name, String tutor) {
		boolean infoIsOk = false; //start with boolean at false
		
		//first check if any fields were not filled in
		if(id.isEmpty() || name.isEmpty() || tutor.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill in all fields", 
					"Info Needed", JOptionPane.ERROR_MESSAGE);
		}
		//check if ID already taken by another class
		else if(fitProg.getClassFromId(id) != null) {
			JOptionPane.showMessageDialog(null, "The classID " + id + " already exists", 
					"Sorry", JOptionPane.ERROR_MESSAGE);
		}
		//check if each input is one word only
		else if(checkForSpaces(id) || checkForSpaces(name) || checkForSpaces(tutor)) {
			JOptionPane.showMessageDialog(null, "Please enter single words only", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		//if above problems did not occur, change boolean to true
		else {
			infoIsOk = true;
		}
		return infoIsOk; //return final value of boolean
	}
	
	/**
	 * checks if given string has any spaces (i.e. if it is
	 * more than one word)
	 * @param text the String to be checked
	 * @return true if String contains a space, false if it doesn't
	 */
	private boolean checkForSpaces(String text) {
		return (text.indexOf(" ") != -1); //indexOf will return -1 if no spaces found
	}


	/**
	 * Processes deleting a class
	 */
	public void processDeletion() {
		String id = idIn.getText(); //get class ID input by user
		//check if no ID was input by user
		if(id.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please enter the ID of the class you wish to delete", 
					"Need ID info", JOptionPane.ERROR_MESSAGE);
		}
		//check if input ID belongs to existing class or not
		else if(fitProg.getClassFromId(id) == null) { //no such class exists
			JOptionPane.showMessageDialog(null, "There is no class with the ID " + id, 
					"Sorry", JOptionPane.ERROR_MESSAGE);
		}
		else { //if class with input ID exists, call deleteClass method of fitProg
			fitProg.deleteClass(id);
			updateDisplay(); //update display after deletion of class
		}
		clearTextFields();
	}

	/**
	 * Instantiates a new window and displays the attendance report
	 */
	public void displayReport() {
		report = new ReportFrame(fitProg); //instantiate new ReportFrame object
		report.setVisible(true);
	}

	/**
	 * Clears GUI textfields
	 */
	private void clearTextFields(){
		idIn.setText("");
		classIn.setText("");
		tutorIn.setText("");
	}

	/**
	 * Writes lines to file representing class name, 
	 * tutor and start time and then exits from the program
	 */
	public void processSaveAndClose() {
		PrintWriter classWriter = null;
		try {
			try{
				classWriter = new PrintWriter(classesOutFile);
				String text = fitProg.makeClassesOutText(); //get text to write to file
				classWriter.write(text); //write text to file
			}
			finally {
				if(classWriter != null)
					classWriter.close();
			}
			System.exit(0); //if IO operations successful, exit program
		}
		catch(IOException e) {
			JOptionPane.showMessageDialog(null, "There has been a problem with the classes out file", 
					"Sorry", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Process button clicks.
	 * @param ae the ActionEvent
	 */
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == attendanceButton)
			displayReport();
		else if(ae.getSource() == addButton)
			processAdding();
		else if(ae.getSource() == deleteButton)
			processDeletion();
		else
			processSaveAndClose();
	}
}
