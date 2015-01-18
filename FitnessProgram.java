import java.util.*;

/**
 * Maintains a list of Fitness Class objects
 * The list is initialised in order of start time
 * The methods allow objects to be added and deleted from the list
 * In addition an array can be returned in order of average attendance
 */
public class FitnessProgram {
	//class constants
	public static final int MAX_CLASSES = 7;
	private static final int CLASS_LENGTH = 1;
	public static final int FIRST_START_TIME = 9;
	public static final int LAST_START_TIME = FIRST_START_TIME + MAX_CLASSES - CLASS_LENGTH;
	public static final int LIST_IS_FULL = -1;
	//instance variables
	private FitnessClass [] fitnessClasses; //array of FitnessClass objects
	private int numClasses; //counter to keep track of number of objects in array

	//FitnessProgram constructor
	public FitnessProgram() {
		fitnessClasses = new FitnessClass [MAX_CLASSES]; //instantiate array of size MAX_CLASSES
		numClasses = 0; //set number of classes in array at 0
	}

	/**
	 * returns the number of FitnessClass objects currently in 
	 * the fitnessClasses array
	 * @return numClasses
	 */
	public int getNumClasses() {
		return numClasses;
	}
	
	/**
	 * instantiates new FitnessClass object based on info from
	 * ClassesIn file and adds it to fitnessClasses array according
	 * to its start time.
	 * @param classInfo a line from the ClassesIn file that contains class information
	 */
	public void addClassFromFile(String classInfo) {
		if(!(classInfo.isEmpty())) {
			FitnessClass fc = new FitnessClass(classInfo); //instantiate new FitnessClass object
			int position = fc.getClassTime() - FIRST_START_TIME; //get its position within array
			fitnessClasses[position] = fc; //add class to array
			numClasses++; //keep count of number of classes in array
		}
	}

	/**
	 * instantiates new FitnessClass object based on info input
	 * in GUI textfields, gives it the earliest available start time
	 * and adds it to the fitnessClasses array in this timeslot.
	 * @param id the class ID input by user
	 * @param n the class name input by user
	 * @param t the class time input by user 
	 */
	public void addClassFromGui(String id, String name, String tutor) {
		FitnessClass fc = new FitnessClass(id, name, tutor); //instantiate new FitnessClass object
		int time = getEarliestTime(); //get earliest available time slot
		fc.setClassTime(time); //set time of class to earliest available time
		int position = time - FIRST_START_TIME; //get position within array
		fitnessClasses[position] = fc; //add new FitnessClass obj to array
		numClasses++; //increment number of classes in array
	}
	
	/**
	 * removes a FitnessClass object with a given ID from
	 * the fitnessClasses array.
	 * @param id the ID of the class to be removed
	 */
	public void deleteClass(String id) {
		FitnessClass fc = getClassFromId(id); //get FitnessClass obj with input ID
		int position = fc.getClassTime() - FIRST_START_TIME; //get its position within array
		fitnessClasses[position] = null; //remove class by setting its position to null
		numClasses--; //decrement number of classes in array
	}
	
	/**
	 * returns the FitnessClass object at a given position within the array
	 * or null if there is no object in such position
	 * @param index the position of the class to be returned
	 * @return the FitnessClass object at index
	 */
	public FitnessClass getClassFromIndex(int index) {
		return fitnessClasses[index];
	}
	
	/**
	 * returns the FitnessClass object with the given ID, if no class has that ID
	 * returns null
	 * @param idNumber the ID of the class to be returned
	 * @return the FitnessClass object with the relevant ID, or null if no such
	 * object exists in the array
	 */
	public FitnessClass getClassFromId(String idNumber){
		FitnessClass fc = null;
		boolean found = false;
		int index = 0;
		//loop through fitnessClasses array while class with ID not found
		while(!found && index < MAX_CLASSES) {
			fc = fitnessClasses[index];
			if(fc != null) { //check if position of array has an object
				if(fc.getClassID().equals(idNumber))
					found = true; //if input ID matches classID, set found to true and exit loop
				else
					index++; //if there is no match, check next position of array
			}
			else //if position of array is null, check next position
				index++;
		}
		//after loop is over
		if(!found) //if there was no match, return null
			return null;
		else //if there was a match, return the corresponding FitnessClass object
			return fc;
	}		
	
	/**
	 * returns the FitnessClass object at the array position corresponding to
	 * a given start time (or null if there is no FC object in that position)
	 * @param t the start time of the FitnessClass
	 * @return the object at the position corresponding to the given time
	 */
	public FitnessClass getClassAtTime(int t) {
		int position = t - FIRST_START_TIME; //get array position corresponding to given time
		return fitnessClasses[position]; //return whatever is in that position
	}
	
	/**
	 * returns the earliest vacant start time for a FitnessClass.
	 */
	public int getEarliestTime() {
		//first check if array is already full
		if(numClasses == MAX_CLASSES)
			return -1; //if there are no vacancies return -1
		//if there are vacancies look for the earliest one
		int t = FIRST_START_TIME;
		boolean foundVacancy = false;
		//loop through fitnessClasses array at each start time
		while(!foundVacancy && t <= LAST_START_TIME) {
			if(getClassAtTime(t) == null) //if there is no class at time t
				foundVacancy = true; //vacancy is found, change boolean to true and exit loop
			else
				t++; //if timeslot not vacant, check next timeslot
		}
		return t; //return final value of t
	}

	/**
	 * sets the attendance figures for a FitnessClass based on information
	 * obtained from a line of the attendances file
	 * @param attLine the line of text from the attendances file
	 */
	public void populateAttendances(String attLine) {
		String [] attTokens = attLine.split("[ ]+"); //convert attLine into array of strings
		String id = attTokens[0]; //get classID from first position of array
		FitnessClass fc = getClassFromId(id); //get FitnessClass with that ID
		//loop through weeks 1 to NUM_WEEKS (number of weeks attendance is monitored for)
		for(int week = 1; week <= FitnessClass.NUM_WEEKS; week++) {
			//for each week, get attendance figure from attTokens array
			int attendance = Integer.parseInt(attTokens[week]);
			//call setAttendance method of the relevant FC to set attendance for that week
			fc.setAttendance(week, attendance);
		}
	}

	/**
	 * makes new array from existing fitness classes (i.e. with no
	 * null values) and sorts it in non-decreasing order according
	 * to average attendance
	 * @return the sorted list
	 */
	public FitnessClass[] makeSortedList() {
		FitnessClass [] sortedClasses; //declare new array
		if(numClasses == MAX_CLASSES) {
			//if fitnessClasses array has no null values, new array can be the same
			sortedClasses = (FitnessClass []) fitnessClasses.clone();
		}
		else { //if there are null values in original array, remove them
			//set size of new array to current amount of existing classes
			sortedClasses = new FitnessClass [numClasses];
			int j=0; //index of new array
			for(int i=0; i < MAX_CLASSES; i++) { //loop through original array
				FitnessClass fc = fitnessClasses[i]; 
				if(fc != null) { //if FitnessClass at i in original array is not null
					sortedClasses[j] = fc; //put FitnessClass in position j of new array
					j++; //move to next position of new array
				}
			}
		}
		//sort new array on avg attendance (see FitnessClass.compareTo())
		Arrays.sort(sortedClasses);
		return sortedClasses; //return the sorted array
	}

	/**
	 * calculates the overall average attendance for all classes
	 * @return the overall average
	 */
	public double getOverallAvg() {
		double sum = 0; // variable to store sum of averages of each class
		for(int i=0; i<MAX_CLASSES; i++) { //loop through fitnessClasses array
			FitnessClass fc = fitnessClasses[i];
			if(fc != null) //add the avg attendance for each existing class to sum
				sum += fc.getAvgAttendance();
		}
		//if numClasses is 0, change to 1 to avoid dividing by 0
		if(numClasses == 0)
			numClasses = 1;
		
		double overallAvg = sum/numClasses; //calculate overall average
		return overallAvg; //return the overall average
	}

	/**
	 * prepares the text to be written to the ClassesOut file
	 * @return the ClassesOutFile text as 1 string
	 */
	public String makeClassesOutText() {
		StringBuilder cOutBuilder = new StringBuilder();
		FitnessClass fc = null;
		int j = 0;
		for(int i = 0; i < MAX_CLASSES; i++) { //loop through fitnessClasses array
			fc = fitnessClasses[i];
			if(fc != null) { //get information line from each existing class
				j++;
				String classLine = fc.getClassesOutLine();
				cOutBuilder.append(classLine); //add line to cOutBuilder
				if(j != numClasses)
					cOutBuilder.append("\r\n");
			}
		}
		String classesOutText = cOutBuilder.toString(); //convert cOutBuilder to string
		return classesOutText; //return string with full text to be written
	}
}
