package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {

        
        //Reads file and initializes
        StdIn.setFile(filename);
        int numOfStudents = StdIn.readInt();
        
        Student[] listOfStudents = new Student[numOfStudents];

    
        //Break each line into 3 seperate parts then insert to Student class contained in Array
        for(int i = 0; i < numOfStudents; i++){
           Student student = new Student(StdIn.readString(), StdIn.readString(), StdIn.readInt());
           listOfStudents[i] = student;
        }
        
        
        //Sort alphabetically
        for(int i = 0; i < listOfStudents.length - 1; i++){
                if(listOfStudents[i].compareNameTo(listOfStudents[i+1]) > 0){
                    Student temp = new Student();    
                    temp = listOfStudents[i];
                    listOfStudents[i] = listOfStudents[i+1];
                    listOfStudents[i+1] = temp;
                    i = -1;
                }
        }

        studentsInLine = new SNode();
        SNode studentNode = studentsInLine;
        for(int i = 0; i < numOfStudents; i++){
                studentNode.setStudent(listOfStudents[i]);
            if ( i < listOfStudents.length - 1){
                    studentNode.setNext(new SNode());
                    studentNode = studentNode.getNext();
                }
        }
    
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        StdIn.setFile(seatingChart);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        seatingAvailability = new boolean[r][c];
        studentsSitting = new Student[r][c];

        for(int i = 0; i < r; i++){
            for(int j = 0; j < c; j++){
                seatingAvailability[i][j] = StdIn.readBoolean();
        }
    }

	// WRITE YOUR CODE HERE
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {

        for(int i = 0; i < seatingAvailability.length; i++){
            for(int j = 0; j < seatingAvailability[0].length; j++){
                
                if(seatingAvailability[i][j] == true){
                    if (musicalChairs != null && musicalChairs.getNext() == musicalChairs){
                        studentsSitting[i][j] = musicalChairs.getStudent();
                        musicalChairs = null;

                    }
                    else if(studentsInLine != null){
                        studentsSitting[i][j] = studentsInLine.getStudent();
                        studentsInLine = studentsInLine.getNext();

                    }
                }
            }
        }
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        for(int i = 0; i < studentsSitting.length; i++){
            for(int j = 0; j < studentsSitting[0].length; j++){
                if (studentsSitting[i][j] != null){
                    SNode s = new SNode();
                    s.setStudent(studentsSitting[i][j]);

                    if(musicalChairs == null){
                    musicalChairs = s;
                    musicalChairs.setNext(musicalChairs);
                    } 
                    else{

                    s.setNext(musicalChairs.getNext());
                    musicalChairs.setNext(s);
                    musicalChairs = s;

                    }
                    studentsSitting[i][j] = null;
                }

            }
        }
    }
    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {

        SNode ptr = new SNode();
        int count = 1;
        SNode prev = new SNode();
        SNode current = new SNode();
        SNode sort = new SNode();

        for(ptr = musicalChairs; ptr.getNext() != musicalChairs; ptr = ptr.getNext()){
            count++;
        }
        while(musicalChairs.getNext() != musicalChairs){
            int random = StdRandom.uniform(count);
            prev = musicalChairs;
            current = musicalChairs.getNext();

        for(int i = 0; i < random; i++){
            current = current.getNext();
            prev = prev.getNext();
        }
        
        SNode loser = new SNode();
        loser.setStudent(current.getStudent());

        prev.setNext(current.getNext());
        current = null;

        
        if(random == count - 1){
            musicalChairs = prev;
        }
        count--;

        if(studentsInLine == null){
            studentsInLine = loser;
        }
        else{
            sort = studentsInLine;
            if(loser.getStudent().getHeight() < sort.getStudent().getHeight()){
                loser.setNext(sort);
                studentsInLine = loser;
            }
            else{
                while(sort.getNext() != null){
                    if(loser.getStudent().getHeight() == sort.getNext().getStudent().getHeight()){
                        break;
                    }
                    else if(loser.getStudent().getHeight() < sort.getNext().getStudent().getHeight()){
                        break;
                    }
                    else{
                        sort = sort.getNext();
                    }

                }
                loser.setNext(sort.getNext());
                sort.setNext(loser);
            }

        }
        }  

    
    
    seatStudents();
    }
        

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {

        //initialization of new student into snode and student class
        Student newstudent = new Student(firstName,lastName,height);
        SNode newstudentnode = new SNode();
        newstudentnode.setStudent(newstudent);
        

        //checks if studentsinline is working
        if(studentsInLine != null){
        SNode ptr = new SNode();

        for(ptr = studentsInLine; ptr != null; ptr = ptr.getNext()){
            if(ptr.getNext() == null){
                ptr.setNext(newstudentnode);
                newstudentnode.setNext(null);
                return;
            }
        }
        }

        //checks if musicalchairs is working
        else if(musicalChairs != null){
            newstudentnode.setNext(musicalChairs.getNext());
            musicalChairs.setNext(newstudentnode);
            musicalChairs = newstudentnode;
            return;
        }


        //checks if studentsSitting is working
        
        else{
            int count = 0;
        for(int i = 0; i < studentsSitting.length; i++){
            for(int j = 0; j < studentsSitting[0].length; j++){
                if(studentsSitting[i][j] != null){
                    count++;
                }
            }}
         if(count > 0){
            for(int i = 0; i < seatingAvailability.length; i++){
                for(int j = 0; j < seatingAvailability[0].length; j++){
                    if(studentsSitting[i][j] == null && seatingAvailability[i][j] == true){
                        studentsSitting[i][j] = newstudentnode.getStudent();
                        return;
        }}
    }
    }
}
    }


    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {
        
        SNode ptr = new SNode();
        SNode prev = new SNode();
        int count = 0;

        if(studentsInLine != null){
            prev = studentsInLine;
            ptr = studentsInLine.getNext();

        while(ptr.getNext() != null){

            String first = ptr.getStudent().getFirstName();
            String last = ptr.getStudent().getLastName();

            String first2 = studentsInLine.getStudent().getFirstName();
            String last2 = studentsInLine.getStudent().getLastName();

            if(first2.compareTo(firstName) == 0 && last2.compareTo(lastName) == 0){
                studentsInLine = ptr;
                prev = null;
                return;
            }
            else if(first.compareTo(firstName) == 0 && last.compareTo(lastName) == 0){
                prev.setNext(ptr.getNext());
                ptr = null;
                return;
            }
                ptr = ptr.getNext();
                prev = prev.getNext();
        }
        if (ptr.getNext() == null){
            String first = ptr.getStudent().getFirstName();
            String last = ptr.getStudent().getLastName();
            if(first.compareTo(firstName) == 0 && last.compareTo(lastName) == 0){
                ptr = null;
                prev.setNext(null);
                return;
            }

        }
    }

    else if(musicalChairs != null){

        if(musicalChairs.getNext() == musicalChairs){
            if(musicalChairs.getStudent().getFirstName().compareToIgnoreCase(firstName) == 0 && musicalChairs.getStudent().getLastName().compareToIgnoreCase(lastName) == 0){
            musicalChairs = null;
            return;
            }
        }

        else {
            SNode mcprev = new SNode();
            SNode mccurr = new SNode();
            mcprev = musicalChairs;
            mccurr = musicalChairs.getNext();
            SNode test = new SNode();

            while(!(mccurr.getStudent().getFirstName().compareToIgnoreCase(firstName) == 0 && mccurr.getStudent().getLastName().compareToIgnoreCase(lastName) == 0)){
                mccurr = mccurr.getNext();
                mcprev = mcprev.getNext();
            }
            if(mccurr == musicalChairs){
                musicalChairs = mcprev;
            }
            mcprev.setNext(mccurr.getNext());
            return;

        }

    }
    
        //checks if studentsSitting is working
        else if(musicalChairs == null){
        for(int i = 0; i < studentsSitting.length; i++){
            for(int j = 0; j < studentsSitting[0].length; j++){
                if(studentsSitting[i][j] != null){
                    count++;
                }
            }}

        if(count > 0){
            for(int i = 0; i < studentsSitting.length; i++){
                for(int j = 0; j < studentsSitting[0].length; j++){
                    if(seatingAvailability[i][j] == true && studentsSitting[i][j] != null){

                    String first = studentsSitting[i][j].getFirstName();
                    String last = studentsSitting[i][j].getLastName();

                    if(first.compareTo(firstName) == 0 && last.compareTo(lastName) == 0){
                        studentsSitting[i][j] = null;
                        return;
                    }}
                }
            }}
    }
}


    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
