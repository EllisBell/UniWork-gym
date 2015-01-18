import java.awt.*;

import javax.swing.*;

/**
 * Class to define window in which attendance report is displayed.
 */
public class ReportFrame extends JFrame {
	//instance variables
	private JTextArea reportDisplay;
	private FitnessProgram fitnessProg;
	
	//Constructor for ReportFrame
	public ReportFrame(FitnessProgram fp) {
		fitnessProg = fp;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Attendance Report");
		//set vertical size of JFrame depending on number of classes
		setSize(750, (fp.getNumClasses() * 20) + 120); 
		setLocation(300, 300);
		reportDisplay = new JTextArea();
		reportDisplay.setFont(new Font("Courier", Font.PLAIN, 14));
		reportDisplay.setEditable(false);
		add(reportDisplay, BorderLayout.CENTER);
		buildReport(); //call method to display report
	}
	
	/**
	 * Constructs the text of the report to be displayed as
	 * a table by the ReportFrame
	 */
	private void buildReport() {
		//build the top line
		String headLine = String.format("%-5s%-15s%-18s%-20s%-15s%n%n", "Id", "Class", "Tutor", "Attendances",
				"Average Attendance");
		//build the content of the table
		StringBuilder tableContent = new StringBuilder();
		//get array of classes sorted by avg attendance to use in report
		FitnessClass [] reportArray = fitnessProg.makeSortedList();
		for(int i=0; i < reportArray.length; i++) { //loop through sorted array
			FitnessClass fc = reportArray[i];
			String classLine = fc.makeReportLine(); //get line of report for each class
			tableContent.append(classLine); //add each line to tableContent
		}
		//build the overall average part of report
		double avgForAll = fitnessProg.getOverallAvg(); //get overall average
		//format overall average line
		String overallAvgLine = String.format("%n%70s %.2f", "Overall Average:", avgForAll);
		//join report by concatenating the 3 parts
		String report = headLine + tableContent + overallAvgLine;
		reportDisplay.setText(report); //display report on JTextArea
	}
	
}
