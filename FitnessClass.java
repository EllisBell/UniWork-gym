/** Defines an object representing a single fitness class
 */
public class FitnessClass implements Comparable<FitnessClass> {
	//class constant
	public static final int NUM_WEEKS = 5;
	
	//instance variables
	private String classID;
	private String className;
	private String classTutor;
	private int classTime;
	private int [] attendances;
	
    //constructor methods
	/**
	 * FitnessClass constructor. Sets instance variables based on
	 * a line of information read in from ClassesIn file
	 * @param classData string containing class info
	 */
	public FitnessClass(String classData) {
		String [] dataTokens = classData.split("[ ]+"); //turn info line into a String array
		classID = dataTokens[0]; //get classId from 1st position of array
		className = dataTokens[1]; //get className from 2nd position of array
		classTutor = dataTokens[2]; //get classTutor from 3rd position of array
		//get classTime from 4th position of array (must parse to int)
		classTime = Integer.parseInt(dataTokens[3]); 
		
		attendances = new int [NUM_WEEKS]; //instantiate attendances array
	}
	
	/**
	 * Second constructor for FitnessClass. Sets instance variables
	 * based on individual parameters passed into it.
	 * @param idNum the id number to set as classID
	 * @param name the name to be set as className
	 * @param tutor the tutor to be set as classTutor
	 */
	public FitnessClass(String idNum, String name, String tutor) {
		classID = idNum; //set classID to idNum
		className = name; //set className to name
		classTutor = tutor; //set classTutor to tutor
		attendances = new int [NUM_WEEKS]; //instantiate attendances array
	}
	
	//accessor methods
	public String getClassID() {
		return classID;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getClassTutor() {
		return classTutor;
	}
	
	public int getClassTime() {
		return classTime;
	}
	
	//mutator methods
	public void setClassID(String idNumber) {
		classID = idNumber;
	}
	
	public void setClassName(String name) {
		className = name;
	}
	
	public void setClassTutor(String tutor) {
		classTutor = tutor;
	}
	
	public void setClassTime(int time) {
		classTime = time;
	}
	
	//mutator and accessor methods for attendance
	/**
	 * Sets the attendance figure for a given week of this class.
	 * Note that each week (1 to 5) is positioned at week-1
	 * within the attendances array (which has indices 0 to 4)
	 * @param week the week for which attendance is set
	 * @param att the attendance value 
	 */
	public void setAttendance(int week, int att) {
		int index = week-1; //given week, get relevant position in the array
		attendances[index] = att; //set attendance value for that position
	}
	
	public int getAttendance(int week) {
		int index = week-1; //given week, get relevant position in the array
		return attendances[index]; //set attendance value for that position
	}
	
	
	/**
	 * calculates the average attendance figure over NUM_WEEKS
	 * for a class
	 * @return double representing the average attendance
	 */
	public double getAvgAttendance() {
		int sum = 0;
		for(int att : attendances)
			sum += att;
		double avg = (double)(sum)/attendances.length;
		return avg;
	}
	
	/**
	 * compareTo method to sort FitnessClass objects on avg attendance.
	 * @param other the FitnessClass object being compared to this FC object
	 */
	public int compareTo(FitnessClass other) {
		double thisAvg = this.getAvgAttendance(); //get avg for this object
		double otherAvg = other.getAvgAttendance(); //get avg for object being compared
		if(thisAvg < otherAvg)
		//as we want to sort in non-increasing order, here we return 1	
			return 1;
		else if(thisAvg == otherAvg)
			return 0;
		else
		//as we want to sort in non-increasing order, here we return -1		
			return -1;
    }
	
	/**
	 * Constructs a String to be displayed as a line of the attendances report,
	 * with info on classID, className, classTutor and attendances
	 * @return String to be displayed
	 */
	public String makeReportLine() {
		String attendances = makeAttendancePart(); //construct attendances part of line
		double avgAtt = getAvgAttendance(); //get average attendance
		//add above to relevant instance variables in a properly formatted string
		String reportLine = String.format("%-5s%-15s%-15s%-25s%-15.2f%n", classID, className,
				classTutor, attendances, avgAtt);
		return reportLine;
	}
	
	/**
	 * Constructs the part of the attendances report line above 
	 * that has the information on attendances over the 5 weeks
	 * @return String with attendance info, appropriately formatted
	 */
	private String makeAttendancePart() {
		StringBuilder attendanceBuilder = new StringBuilder();
		for(int i=0; i < NUM_WEEKS; i++) { //loop through attendances array
			int attendance = attendances[i];
			attendanceBuilder.append(String.format("%3d", attendance));
		}
		return attendanceBuilder.toString(); //convert StringBuilder to string and return
	}
	
	/**
	 * Constructs a String to be displayed as a line of the ClassesOut file
	 * @return the String
	 */
	public String getClassesOutLine() {
		//concatenate relevant instance variables (and ensure new line) and return
		return classID + " " + className + " " + classTutor + " " + classTime;
	}
}
