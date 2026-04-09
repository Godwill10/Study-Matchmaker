package com.studymatchmaker.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReferenceDataService {

    private final Map<String, List<String>> universitiesByState = new LinkedHashMap<>();
    private final Map<String, List<String>> coursesByMajorLevel = new LinkedHashMap<>();
    private final Map<String, List<String>> generalCoursesByLevel = new LinkedHashMap<>();
    private final List<String> majors = new ArrayList<>();
    private final List<String> studyStyles = new ArrayList<>();
    private final List<String> academicLevels = new ArrayList<>();
    private final List<Map<String, String>> states = new ArrayList<>();

    public ReferenceDataService() {
        initStates();
        initUniversities();
        initMajors();
        initAcademicLevels();
        initStudyStyles();
        initCourses();
    }

    public List<Map<String, String>> getStates() { return states; }
    public List<String> getUniversities(String stateCode) { return universitiesByState.getOrDefault(stateCode, List.of()); }
    public List<String> getMajors() { return majors; }
    public List<String> getAcademicLevels() { return academicLevels; }
    public List<String> getStudyStyles() { return studyStyles; }
    public List<String> getCourses(String major, String level) {
        List<String> result = new ArrayList<>();
        // Add general education courses for the level
        result.addAll(generalCoursesByLevel.getOrDefault(level, List.of()));
        // Add major-specific courses for the level
        String key = major + ":" + level;
        result.addAll(coursesByMajorLevel.getOrDefault(key, List.of()));
        // Deduplicate while preserving order
        return result.stream().distinct().toList();
    }

    private void addState(String code, String label) {
        states.add(Map.of("code", code, "label", label));
    }

    private void initStates() {
        addState("AL", "Alabama");
        addState("AK", "Alaska");
        addState("AZ", "Arizona");
        addState("AR", "Arkansas");
        addState("CA", "California");
        addState("CO", "Colorado");
        addState("CT", "Connecticut");
        addState("DC", "District of Columbia");
        addState("DE", "Delaware");
        addState("FL", "Florida");
        addState("GA", "Georgia");
        addState("HI", "Hawaii");
        addState("ID", "Idaho");
        addState("IL", "Illinois");
        addState("IN", "Indiana");
        addState("IA", "Iowa");
        addState("KS", "Kansas");
        addState("KY", "Kentucky");
        addState("LA", "Louisiana");
        addState("ME", "Maine");
        addState("MD", "Maryland");
        addState("MA", "Massachusetts");
        addState("MI", "Michigan");
        addState("MN", "Minnesota");
        addState("MS", "Mississippi");
        addState("MO", "Missouri");
        addState("MT", "Montana");
        addState("NE", "Nebraska");
        addState("NV", "Nevada");
        addState("NH", "New Hampshire");
        addState("NJ", "New Jersey");
        addState("NM", "New Mexico");
        addState("NY", "New York");
        addState("NC", "North Carolina");
        addState("ND", "North Dakota");
        addState("OH", "Ohio");
        addState("OK", "Oklahoma");
        addState("OR", "Oregon");
        addState("PA", "Pennsylvania");
        addState("RI", "Rhode Island");
        addState("SC", "South Carolina");
        addState("SD", "South Dakota");
        addState("TN", "Tennessee");
        addState("TX", "Texas");
        addState("UT", "Utah");
        addState("VT", "Vermont");
        addState("VA", "Virginia");
        addState("WA", "Washington");
        addState("WV", "West Virginia");
        addState("WI", "Wisconsin");
        addState("WY", "Wyoming");
    }

    private void initUniversities() {
        universitiesByState.put("AL", List.of(
            "University of Alabama", "Auburn University", "University of Alabama at Birmingham",
            "University of South Alabama", "Alabama A&M University", "Troy University",
            "Samford University", "Jacksonville State University", "University of North Alabama",
            "Alabama State University", "University of Montevallo", "Spring Hill College"));

        universitiesByState.put("AK", List.of(
            "University of Alaska Anchorage", "University of Alaska Fairbanks",
            "University of Alaska Southeast", "Alaska Pacific University"));

        universitiesByState.put("AZ", List.of(
            "Arizona State University", "University of Arizona", "Northern Arizona University",
            "Grand Canyon University", "Embry-Riddle Aeronautical University", "Arizona Christian University"));

        universitiesByState.put("AR", List.of(
            "University of Arkansas", "Arkansas State University", "University of Central Arkansas",
            "Hendrix College", "Harding University", "University of Arkansas at Little Rock",
            "Arkansas Tech University", "John Brown University"));

        universitiesByState.put("CA", List.of(
            "Stanford University", "University of California, Berkeley", "UCLA",
            "University of Southern California", "California Institute of Technology",
            "UC San Diego", "UC Davis", "UC Irvine", "UC Santa Barbara",
            "UC Santa Cruz", "UC Riverside", "UC Merced",
            "San Jose State University", "San Diego State University", "Cal Poly San Luis Obispo",
            "CSU Fullerton", "CSU Long Beach", "CSU Northridge",
            "Santa Clara University", "University of San Francisco", "Loyola Marymount University",
            "Pepperdine University", "Chapman University", "Claremont McKenna College",
            "Pomona College", "Harvey Mudd College", "Occidental College"));

        universitiesByState.put("CO", List.of(
            "University of Colorado Boulder", "Colorado State University", "University of Denver",
            "Colorado School of Mines", "University of Colorado Denver",
            "Colorado College", "University of Northern Colorado", "Regis University",
            "United States Air Force Academy", "Fort Lewis College"));

        universitiesByState.put("CT", List.of(
            "Yale University", "University of Connecticut", "Wesleyan University",
            "Trinity College", "Connecticut College", "Quinnipiac University",
            "Fairfield University", "Sacred Heart University", "University of Hartford",
            "Central Connecticut State University", "University of New Haven"));

        universitiesByState.put("DC", List.of(
            "Georgetown University", "George Washington University", "Howard University",
            "American University", "Catholic University of America",
            "Gallaudet University", "Trinity Washington University"));

        universitiesByState.put("DE", List.of(
            "University of Delaware", "Delaware State University",
            "Wilmington University", "Wesley College", "Goldey-Beacom College"));

        universitiesByState.put("FL", List.of(
            "University of Florida", "Florida State University", "University of Miami",
            "University of Central Florida", "University of South Florida",
            "Florida International University", "Florida Atlantic University",
            "Florida A&M University", "Stetson University", "Rollins College",
            "University of Tampa", "Nova Southeastern University",
            "Embry-Riddle Aeronautical University", "University of North Florida",
            "Florida Gulf Coast University", "Jacksonville University"));

        universitiesByState.put("GA", List.of(
            "Georgia Institute of Technology", "University of Georgia", "Emory University",
            "Georgia State University", "Mercer University", "Georgia Southern University",
            "Kennesaw State University", "Augusta University", "Spelman College",
            "Morehouse College", "Clark Atlanta University", "Valdosta State University",
            "Savannah College of Art and Design", "Berry College", "Oglethorpe University"));

        universitiesByState.put("HI", List.of(
            "University of Hawaii at Manoa", "University of Hawaii at Hilo",
            "Hawaii Pacific University", "Brigham Young University-Hawaii",
            "Chaminade University of Honolulu"));

        universitiesByState.put("ID", List.of(
            "Boise State University", "University of Idaho", "Idaho State University",
            "Brigham Young University-Idaho", "College of Idaho",
            "Northwest Nazarene University", "Lewis-Clark State College"));

        universitiesByState.put("IL", List.of(
            "University of Chicago", "Northwestern University", "University of Illinois Urbana-Champaign",
            "University of Illinois Chicago", "DePaul University", "Loyola University Chicago",
            "Illinois Institute of Technology", "Illinois State University",
            "Southern Illinois University", "Northern Illinois University",
            "Western Illinois University", "Eastern Illinois University",
            "Bradley University", "Wheaton College", "Lake Forest College"));

        universitiesByState.put("IN", List.of(
            "Purdue University", "Indiana University Bloomington", "University of Notre Dame",
            "Butler University", "Ball State University", "Indiana State University",
            "Rose-Hulman Institute of Technology", "Valparaiso University",
            "University of Evansville", "DePauw University", "Wabash College",
            "Indiana University-Purdue University Indianapolis"));

        universitiesByState.put("IA", List.of(
            "University of Iowa", "Iowa State University", "University of Northern Iowa",
            "Drake University", "Grinnell College", "Cornell College",
            "Luther College", "Coe College", "Simpson College", "Dordt University"));

        universitiesByState.put("KS", List.of(
            "University of Kansas", "Kansas State University", "Wichita State University",
            "Emporia State University", "Pittsburg State University", "Baker University",
            "Washburn University", "Fort Hays State University", "Benedictine College"));

        universitiesByState.put("KY", List.of(
            "University of Kentucky", "University of Louisville", "Western Kentucky University",
            "Eastern Kentucky University", "Murray State University", "Northern Kentucky University",
            "Centre College", "Transylvania University", "Berea College",
            "Morehead State University", "Bellarmine University"));

        universitiesByState.put("LA", List.of(
            "Louisiana State University", "Tulane University", "University of Louisiana at Lafayette",
            "Louisiana Tech University", "University of New Orleans",
            "Southeastern Louisiana University", "McNeese State University",
            "Xavier University of Louisiana", "Loyola University New Orleans",
            "Dillard University", "Grambling State University"));

        universitiesByState.put("ME", List.of(
            "University of Maine", "Bowdoin College", "Bates College",
            "Colby College", "University of Southern Maine", "University of New England",
            "Husson University", "Maine Maritime Academy"));

        universitiesByState.put("MD", List.of(
            "Johns Hopkins University", "University of Maryland, College Park",
            "United States Naval Academy", "Towson University", "University of Maryland, Baltimore County",
            "Salisbury University", "Loyola University Maryland", "Morgan State University",
            "Goucher College", "McDaniel College", "St. Mary's College of Maryland",
            "Hood College", "Stevenson University", "Coppin State University"));

        universitiesByState.put("MA", List.of(
            "Harvard University", "Massachusetts Institute of Technology", "Boston University",
            "Boston College", "Tufts University", "Northeastern University",
            "Brandeis University", "University of Massachusetts Amherst",
            "Williams College", "Amherst College", "Wellesley College",
            "Smith College", "Mount Holyoke College", "Clark University",
            "Worcester Polytechnic Institute", "College of the Holy Cross",
            "Babson College", "Bentley University", "Suffolk University",
            "Emerson College", "Berklee College of Music"));

        universitiesByState.put("MI", List.of(
            "University of Michigan", "Michigan State University", "Wayne State University",
            "University of Michigan-Dearborn", "Western Michigan University",
            "Central Michigan University", "Eastern Michigan University",
            "Grand Valley State University", "Oakland University",
            "Michigan Technological University", "Kettering University",
            "Hope College", "Kalamazoo College", "Calvin University",
            "Ferris State University", "Saginaw Valley State University"));

        universitiesByState.put("MN", List.of(
            "University of Minnesota", "University of St Thomas", "Macalester College",
            "Carleton College", "St. Olaf College", "Augsburg University",
            "Minnesota State University, Mankato", "University of Minnesota Duluth",
            "St. Cloud State University", "Hamline University",
            "Bethel University", "Gustavus Adolphus College",
            "Concordia College", "College of Saint Benedict",
            "Saint John's University", "Winona State University"));

        universitiesByState.put("MS", List.of(
            "University of Mississippi", "Mississippi State University",
            "University of Southern Mississippi", "Jackson State University",
            "Mississippi University for Women", "Alcorn State University",
            "Delta State University", "Millsaps College", "Belhaven University",
            "Tougaloo College", "Mississippi Valley State University"));

        universitiesByState.put("MO", List.of(
            "Washington University in St. Louis", "University of Missouri",
            "Saint Louis University", "Missouri State University",
            "University of Missouri-Kansas City", "Missouri University of Science and Technology",
            "Truman State University", "Drury University", "Rockhurst University",
            "Webster University", "Southeast Missouri State University",
            "Northwest Missouri State University", "Lindenwood University"));

        universitiesByState.put("MT", List.of(
            "University of Montana", "Montana State University",
            "Montana Tech", "Carroll College", "Rocky Mountain College",
            "University of Great Falls", "Montana State University Billings"));

        universitiesByState.put("NE", List.of(
            "University of Nebraska-Lincoln", "Creighton University",
            "University of Nebraska at Omaha", "University of Nebraska at Kearney",
            "Nebraska Wesleyan University", "Doane University",
            "Hastings College", "Wayne State College", "Chadron State College"));

        universitiesByState.put("NV", List.of(
            "University of Nevada, Las Vegas", "University of Nevada, Reno",
            "Nevada State University", "Sierra Nevada University",
            "Touro University Nevada", "Great Basin College"));

        universitiesByState.put("NH", List.of(
            "Dartmouth College", "University of New Hampshire",
            "Saint Anselm College", "Plymouth State University",
            "Keene State College", "Franklin Pierce University",
            "Southern New Hampshire University", "New England College"));

        universitiesByState.put("NJ", List.of(
            "Princeton University", "Rutgers University", "Seton Hall University",
            "Stevens Institute of Technology", "New Jersey Institute of Technology",
            "Montclair State University", "Rowan University",
            "The College of New Jersey", "Rider University",
            "Fairleigh Dickinson University", "Drew University",
            "William Paterson University", "Stockton University",
            "Kean University", "Monmouth University", "Saint Peter's University"));

        universitiesByState.put("NM", List.of(
            "University of New Mexico", "New Mexico State University",
            "New Mexico Institute of Mining and Technology",
            "Eastern New Mexico University", "Western New Mexico University",
            "St. John's College", "New Mexico Highlands University"));

        universitiesByState.put("NY", List.of(
            "Columbia University", "New York University", "Cornell University",
            "University of Rochester", "Syracuse University", "Fordham University",
            "Stony Brook University", "University at Buffalo",
            "Binghamton University", "University at Albany",
            "Rensselaer Polytechnic Institute", "Rochester Institute of Technology",
            "CUNY City College", "CUNY Hunter College", "CUNY Baruch College",
            "Barnard College", "Vassar College", "Colgate University",
            "Hamilton College", "Skidmore College", "Marist College",
            "St. John's University", "Hofstra University", "Pace University",
            "Ithaca College", "Manhattan College", "Yeshiva University",
            "The New School", "Pratt Institute", "Cooper Union"));

        universitiesByState.put("NC", List.of(
            "Duke University", "University of North Carolina at Chapel Hill",
            "North Carolina State University", "Wake Forest University",
            "Davidson College", "Appalachian State University",
            "University of North Carolina at Charlotte", "East Carolina University",
            "Elon University", "University of North Carolina at Greensboro",
            "North Carolina A&T State University", "UNC Wilmington",
            "UNC Asheville", "Western Carolina University",
            "High Point University", "Campbell University", "Guilford College"));

        universitiesByState.put("ND", List.of(
            "University of North Dakota", "North Dakota State University",
            "Minot State University", "Dickinson State University",
            "Valley City State University", "University of Mary",
            "University of Jamestown"));

        universitiesByState.put("OH", List.of(
            "Ohio State University", "Case Western Reserve University",
            "University of Cincinnati", "Miami University", "Ohio University",
            "Kent State University", "University of Dayton", "Xavier University",
            "Bowling Green State University", "University of Toledo",
            "University of Akron", "Cleveland State University",
            "Wright State University", "Oberlin College", "Kenyon College",
            "Denison University", "College of Wooster", "Wittenberg University",
            "Capital University", "Otterbein University"));

        universitiesByState.put("OK", List.of(
            "University of Oklahoma", "Oklahoma State University",
            "University of Tulsa", "University of Central Oklahoma",
            "Oral Roberts University", "Oklahoma City University",
            "Northeastern State University", "Cameron University",
            "Southwestern Oklahoma State University", "East Central University"));

        universitiesByState.put("OR", List.of(
            "University of Oregon", "Oregon State University",
            "Portland State University", "University of Portland",
            "Willamette University", "Lewis & Clark College",
            "Reed College", "Linfield University", "George Fox University",
            "Pacific University", "Oregon Institute of Technology",
            "Western Oregon University", "Southern Oregon University"));

        universitiesByState.put("PA", List.of(
            "University of Pennsylvania", "Carnegie Mellon University",
            "Penn State University", "University of Pittsburgh",
            "Temple University", "Drexel University", "Villanova University",
            "Lehigh University", "Bucknell University", "Lafayette College",
            "Swarthmore College", "Haverford College", "Bryn Mawr College",
            "Dickinson College", "Franklin & Marshall College",
            "Gettysburg College", "Duquesne University", "La Salle University",
            "Saint Joseph's University", "Muhlenberg College"));

        universitiesByState.put("RI", List.of(
            "Brown University", "University of Rhode Island",
            "Providence College", "Rhode Island School of Design",
            "Bryant University", "Roger Williams University",
            "Salve Regina University", "Johnson & Wales University"));

        universitiesByState.put("SC", List.of(
            "University of South Carolina", "Clemson University",
            "College of Charleston", "Furman University", "Wofford College",
            "The Citadel", "Coastal Carolina University",
            "Francis Marion University", "South Carolina State University",
            "Presbyterian College", "Bob Jones University",
            "Winthrop University", "Charleston Southern University"));

        universitiesByState.put("SD", List.of(
            "University of South Dakota", "South Dakota State University",
            "South Dakota School of Mines and Technology",
            "Augustana University", "Northern State University",
            "Black Hills State University", "Dakota State University"));

        universitiesByState.put("TN", List.of(
            "Vanderbilt University", "University of Tennessee",
            "University of Memphis", "Tennessee State University",
            "Middle Tennessee State University", "East Tennessee State University",
            "Belmont University", "Rhodes College", "Sewanee: The University of the South",
            "Lipscomb University", "Tennessee Technological University",
            "Austin Peay State University", "Fisk University", "Lee University"));

        universitiesByState.put("TX", List.of(
            "University of Texas at Austin", "Texas A&M University", "Rice University",
            "Southern Methodist University", "Baylor University",
            "Texas Christian University", "University of Houston",
            "Texas Tech University", "University of Texas at Dallas",
            "University of Texas at San Antonio", "University of Texas at Arlington",
            "University of North Texas", "Texas State University",
            "Sam Houston State University", "Lamar University",
            "Stephen F. Austin State University", "Trinity University",
            "Abilene Christian University", "Texas Southern University",
            "Prairie View A&M University", "St. Edward's University",
            "University of the Incarnate Word", "Tarleton State University"));

        universitiesByState.put("UT", List.of(
            "University of Utah", "Brigham Young University", "Utah State University",
            "Weber State University", "Southern Utah University",
            "Westminster University", "Utah Valley University",
            "Dixie State University", "Utah Tech University"));

        universitiesByState.put("VT", List.of(
            "University of Vermont", "Middlebury College",
            "Norwich University", "Bennington College",
            "Saint Michael's College", "Champlain College",
            "Castleton University", "Green Mountain College"));

        universitiesByState.put("VA", List.of(
            "University of Virginia", "Virginia Tech", "College of William & Mary",
            "George Mason University", "Virginia Commonwealth University",
            "James Madison University", "Old Dominion University",
            "University of Richmond", "Washington and Lee University",
            "Virginia Military Institute", "Radford University",
            "Hampton University", "Norfolk State University",
            "Liberty University", "Roanoke College",
            "Christopher Newport University", "Longwood University",
            "Randolph-Macon College", "Hollins University"));

        universitiesByState.put("WA", List.of(
            "University of Washington", "Washington State University",
            "Gonzaga University", "Seattle University", "Pacific Lutheran University",
            "Whitman College", "University of Puget Sound",
            "Western Washington University", "Eastern Washington University",
            "Central Washington University", "Whitworth University",
            "Seattle Pacific University", "Evergreen State College",
            "Walla Walla University"));

        universitiesByState.put("WV", List.of(
            "West Virginia University", "Marshall University",
            "West Virginia State University", "Shepherd University",
            "Fairmont State University", "Concord University",
            "Bethany College", "West Liberty University",
            "Wheeling University", "Davis & Elkins College"));

        universitiesByState.put("WI", List.of(
            "University of Wisconsin-Madison", "Marquette University",
            "University of Wisconsin-Milwaukee", "University of Wisconsin-La Crosse",
            "University of Wisconsin-Eau Claire", "University of Wisconsin-Green Bay",
            "University of Wisconsin-Oshkosh", "University of Wisconsin-Stout",
            "University of Wisconsin-Whitewater", "University of Wisconsin-Platteville",
            "Lawrence University", "Beloit College", "Ripon College",
            "Carroll University", "Alverno College", "Milwaukee School of Engineering"));

        universitiesByState.put("WY", List.of(
            "University of Wyoming"));
    }

    private void initMajors() {
        majors.addAll(List.of(
            "Accounting",
            "Aerospace Engineering",
            "African American Studies",
            "Agricultural Science",
            "Anthropology",
            "Architecture",
            "Art History",
            "Biochemistry",
            "Biology",
            "Biomedical Engineering",
            "Business Administration",
            "Chemical Engineering",
            "Chemistry",
            "Civil Engineering",
            "Communications",
            "Computer Engineering",
            "Computer Science",
            "Criminal Justice",
            "Cybersecurity",
            "Data Science",
            "Economics",
            "Education",
            "Electrical Engineering",
            "English",
            "Environmental Science",
            "Film Studies",
            "Finance",
            "Graphic Design",
            "Health Sciences",
            "History",
            "Hospitality Management",
            "Human Resources",
            "Industrial Engineering",
            "Information Systems",
            "Information Technology",
            "International Relations",
            "Journalism",
            "Kinesiology",
            "Linguistics",
            "Management",
            "Marketing",
            "Mathematics",
            "Mechanical Engineering",
            "Music",
            "Neuroscience",
            "Nursing",
            "Nutrition",
            "Philosophy",
            "Physics",
            "Political Science",
            "Pre-Law",
            "Pre-Med",
            "Psychology",
            "Public Health",
            "Religious Studies",
            "Social Work",
            "Sociology",
            "Software Engineering",
            "Spanish",
            "Statistics",
            "Supply Chain Management",
            "Theater",
            "Urban Planning",
            "Women's & Gender Studies"
        ));
    }

    private void initAcademicLevels() {
        academicLevels.addAll(List.of("Freshman", "Sophomore", "Junior", "Senior", "Graduate"));
    }

    private void initStudyStyles() {
        studyStyles.addAll(List.of(
            "Solo",
            "Collaborative",
            "Hands-on",
            "Lecture-based",
            "Discussion-based",
            "Visual learner",
            "Reading/Writing",
            "Flashcards & Repetition",
            "Teaching others",
            "Problem-solving"
        ));
    }

    private void put(String major, String level, String... courses) {
        coursesByMajorLevel.put(major + ":" + level, List.of(courses));
    }

    private void initCourses() {
        // General education courses shared across all majors
        generalCoursesByLevel.put("Freshman", List.of(
            "English Composition", "College Writing", "Public Speaking",
            "Introduction to Psychology", "Introduction to Sociology",
            "US History", "World History", "Introduction to Philosophy",
            "First Year Seminar", "Health & Wellness"
        ));
        generalCoursesByLevel.put("Sophomore", List.of("Technical Writing", "Introduction to Ethics", "Research Methods"));
        generalCoursesByLevel.put("Junior", List.of());
        generalCoursesByLevel.put("Senior", List.of("Capstone Project", "Senior Seminar"));
        generalCoursesByLevel.put("Graduate", List.of("Graduate Research Seminar", "Thesis Research"));

        // ---- Computer Science ----
        put("Computer Science", "Freshman", "Intro to Computer Science", "Intro to Programming", "Calculus I", "Discrete Mathematics");
        put("Computer Science", "Sophomore", "Data Structures", "Object-Oriented Programming", "Calculus II", "Linear Algebra", "Web Development");
        put("Computer Science", "Junior", "Algorithms", "Database Systems", "Computer Networks", "Operating Systems", "Software Engineering");
        put("Computer Science", "Senior", "Artificial Intelligence", "Machine Learning", "Cybersecurity", "Cloud Computing", "Distributed Systems", "Compiler Design", "Mobile App Development");
        put("Computer Science", "Graduate", "Advanced Machine Learning", "Deep Learning", "Advanced Algorithms", "Computer Vision", "Natural Language Processing", "Quantum Computing", "Advanced Software Architecture");

        // ---- Software Engineering ----
        put("Software Engineering", "Freshman", "Intro to Programming", "Intro to Computer Science", "Calculus I", "Discrete Mathematics");
        put("Software Engineering", "Sophomore", "Data Structures", "Object-Oriented Programming", "Web Development", "Linear Algebra", "Version Control & DevOps");
        put("Software Engineering", "Junior", "Software Engineering", "Database Systems", "Algorithms", "Computer Networks", "Agile Methods", "Software Testing");
        put("Software Engineering", "Senior", "Cloud Computing", "Mobile App Development", "Distributed Systems", "Human-Computer Interaction", "Game Development", "DevOps & CI/CD");
        put("Software Engineering", "Graduate", "Advanced Software Architecture", "Microservices Design", "Advanced Algorithms", "Formal Methods", "Software Reliability");

        // ---- Data Science ----
        put("Data Science", "Freshman", "Intro to Computer Science", "Calculus I", "Introduction to Statistics", "Intro to Programming");
        put("Data Science", "Sophomore", "Data Structures", "Calculus II", "Linear Algebra", "Probability & Statistics", "Data Wrangling");
        put("Data Science", "Junior", "Machine Learning", "Database Systems", "Data Visualization", "Applied Statistics", "Algorithms");
        put("Data Science", "Senior", "Deep Learning", "Data Mining", "Big Data Analytics", "Natural Language Processing", "Ethics in Data Science");
        put("Data Science", "Graduate", "Advanced Machine Learning", "Deep Learning", "Reinforcement Learning", "Big Data Analytics", "Statistical Learning Theory");

        // ---- Cybersecurity ----
        put("Cybersecurity", "Freshman", "Intro to Computer Science", "Intro to Programming", "Calculus I", "Intro to Cybersecurity");
        put("Cybersecurity", "Sophomore", "Data Structures", "Computer Networks Fundamentals", "Linux Administration", "Discrete Mathematics");
        put("Cybersecurity", "Junior", "Network Security", "Operating Systems", "Cryptography", "Ethical Hacking", "Digital Forensics");
        put("Cybersecurity", "Senior", "Advanced Network Security", "Malware Analysis", "Security Architecture", "Incident Response", "Penetration Testing");
        put("Cybersecurity", "Graduate", "Advanced Cryptography", "Cyber Threat Intelligence", "Secure Software Design", "Advanced Digital Forensics", "Information Assurance");

        // ---- Computer Engineering ----
        put("Computer Engineering", "Freshman", "Intro to Computer Science", "Intro to Engineering", "Calculus I", "General Physics I");
        put("Computer Engineering", "Sophomore", "Data Structures", "Circuit Analysis", "Calculus II", "General Physics II", "Digital Logic Design");
        put("Computer Engineering", "Junior", "Computer Architecture", "Signals & Systems", "Embedded Systems", "Microprocessors", "Operating Systems");
        put("Computer Engineering", "Senior", "VLSI Design", "Real-Time Systems", "Robotics", "Computer Vision", "IoT Systems");
        put("Computer Engineering", "Graduate", "Advanced Computer Architecture", "Advanced Embedded Systems", "Autonomous Systems", "Hardware Security", "FPGA Design");

        // ---- Electrical Engineering ----
        put("Electrical Engineering", "Freshman", "Intro to Engineering", "Calculus I", "General Physics I", "Intro to Electrical Circuits");
        put("Electrical Engineering", "Sophomore", "Circuit Analysis", "Calculus II", "Differential Equations", "General Physics II", "Digital Logic Design");
        put("Electrical Engineering", "Junior", "Signals & Systems", "Electromagnetics", "Electronics", "Control Systems", "Power Systems");
        put("Electrical Engineering", "Senior", "Communication Systems", "VLSI Design", "Power Electronics", "Antenna Design", "Renewable Energy Systems");
        put("Electrical Engineering", "Graduate", "Advanced Electromagnetics", "Advanced Control Systems", "Photonics", "Advanced Power Systems", "Wireless Communications");

        // ---- Mechanical Engineering ----
        put("Mechanical Engineering", "Freshman", "Intro to Engineering", "Calculus I", "General Physics I", "Engineering Graphics");
        put("Mechanical Engineering", "Sophomore", "Statics", "Dynamics", "Calculus II", "Differential Equations", "Materials Science");
        put("Mechanical Engineering", "Junior", "Thermodynamics", "Fluid Mechanics", "Heat Transfer", "Machine Design", "Manufacturing Processes");
        put("Mechanical Engineering", "Senior", "Finite Element Analysis", "Vibrations", "Mechatronics", "Senior Design Project", "Numerical Methods");
        put("Mechanical Engineering", "Graduate", "Advanced Thermodynamics", "Computational Fluid Dynamics", "Advanced Materials", "Robotics", "Advanced Finite Element Methods");

        // ---- Civil Engineering ----
        put("Civil Engineering", "Freshman", "Intro to Engineering", "Calculus I", "General Physics I", "Engineering Graphics");
        put("Civil Engineering", "Sophomore", "Statics", "Dynamics", "Calculus II", "Surveying", "Materials Science");
        put("Civil Engineering", "Junior", "Structural Analysis", "Fluid Mechanics", "Geotechnical Engineering", "Transportation Engineering", "Environmental Engineering");
        put("Civil Engineering", "Senior", "Steel Design", "Concrete Design", "Hydrology", "Construction Management", "Senior Design Project");
        put("Civil Engineering", "Graduate", "Advanced Structural Analysis", "Advanced Geotechnical Engineering", "Infrastructure Systems", "Earthquake Engineering", "Advanced Transportation");

        // ---- Biology ----
        put("Biology", "Freshman", "General Biology I", "General Biology II", "General Chemistry I", "Calculus I");
        put("Biology", "Sophomore", "Genetics", "Organic Chemistry I", "Organic Chemistry II", "General Physics I", "Cell Biology");
        put("Biology", "Junior", "Molecular Biology", "Biochemistry", "Ecology", "Microbiology", "Evolutionary Biology");
        put("Biology", "Senior", "Immunology", "Developmental Biology", "Neurobiology", "Bioinformatics", "Senior Thesis");
        put("Biology", "Graduate", "Advanced Molecular Biology", "Genomics", "Advanced Ecology", "Computational Biology", "Dissertation Research");

        // ---- Chemistry ----
        put("Chemistry", "Freshman", "General Chemistry I", "General Chemistry II", "Calculus I", "General Physics I");
        put("Chemistry", "Sophomore", "Organic Chemistry I", "Organic Chemistry II", "Calculus II", "Analytical Chemistry");
        put("Chemistry", "Junior", "Physical Chemistry", "Inorganic Chemistry", "Biochemistry", "Instrumental Analysis");
        put("Chemistry", "Senior", "Advanced Organic Chemistry", "Advanced Physical Chemistry", "Materials Chemistry", "Senior Thesis");
        put("Chemistry", "Graduate", "Advanced Inorganic Chemistry", "Chemical Kinetics", "Spectroscopy", "Polymer Chemistry", "Dissertation Research");

        // ---- Biochemistry ----
        put("Biochemistry", "Freshman", "General Biology I", "General Chemistry I", "General Chemistry II", "Calculus I");
        put("Biochemistry", "Sophomore", "Organic Chemistry I", "Organic Chemistry II", "Cell Biology", "General Physics I");
        put("Biochemistry", "Junior", "Biochemistry I", "Biochemistry II", "Molecular Biology", "Genetics");
        put("Biochemistry", "Senior", "Enzymology", "Structural Biology", "Bioinformatics", "Senior Thesis");
        put("Biochemistry", "Graduate", "Advanced Biochemistry", "Protein Engineering", "Metabolomics", "Advanced Molecular Biology", "Dissertation Research");

        // ---- Physics ----
        put("Physics", "Freshman", "General Physics I", "General Physics II", "Calculus I", "Calculus II");
        put("Physics", "Sophomore", "Modern Physics", "Calculus III", "Differential Equations", "Linear Algebra", "Computational Physics");
        put("Physics", "Junior", "Classical Mechanics", "Electromagnetism", "Quantum Mechanics I", "Thermal Physics");
        put("Physics", "Senior", "Quantum Mechanics II", "Statistical Mechanics", "Astrophysics", "Particle Physics", "Senior Thesis");
        put("Physics", "Graduate", "Advanced Quantum Mechanics", "General Relativity", "Quantum Field Theory", "Condensed Matter Physics", "Dissertation Research");

        // ---- Mathematics ----
        put("Mathematics", "Freshman", "Calculus I", "Calculus II", "Intro to Proofs", "College Algebra");
        put("Mathematics", "Sophomore", "Calculus III", "Linear Algebra", "Discrete Mathematics", "Differential Equations");
        put("Mathematics", "Junior", "Abstract Algebra", "Real Analysis", "Probability & Statistics", "Number Theory");
        put("Mathematics", "Senior", "Complex Analysis", "Topology", "Numerical Methods", "Senior Thesis");
        put("Mathematics", "Graduate", "Advanced Real Analysis", "Advanced Linear Algebra", "Stochastic Processes", "Topology", "Dissertation Research");

        // ---- Statistics ----
        put("Statistics", "Freshman", "Introduction to Statistics", "Calculus I", "Calculus II", "Intro to Programming");
        put("Statistics", "Sophomore", "Probability & Statistics", "Linear Algebra", "Calculus III", "Statistical Computing");
        put("Statistics", "Junior", "Mathematical Statistics", "Regression Analysis", "Applied Statistics", "Experimental Design");
        put("Statistics", "Senior", "Bayesian Statistics", "Time Series Analysis", "Statistical Learning", "Senior Thesis");
        put("Statistics", "Graduate", "Advanced Statistics", "Stochastic Processes", "Statistical Learning Theory", "Econometrics", "Dissertation Research");

        // ---- Business Administration ----
        put("Business Administration", "Freshman", "Intro to Business", "Microeconomics", "Macroeconomics", "College Algebra");
        put("Business Administration", "Sophomore", "Financial Accounting", "Managerial Accounting", "Business Statistics", "Introduction to Management");
        put("Business Administration", "Junior", "Corporate Finance", "Operations Management", "Business Law", "Organizational Behavior", "International Business");
        put("Business Administration", "Senior", "Strategic Management", "Entrepreneurship", "Business Ethics", "Senior Thesis");
        put("Business Administration", "Graduate", "Advanced Corporate Finance", "Behavioral Economics", "Global Strategy", "Leadership & Organizations", "Dissertation Research");

        // ---- Finance ----
        put("Finance", "Freshman", "Intro to Business", "Microeconomics", "Macroeconomics", "Calculus I");
        put("Finance", "Sophomore", "Financial Accounting", "Managerial Accounting", "Business Statistics", "Intro to Finance");
        put("Finance", "Junior", "Corporate Finance", "Investment Analysis", "Financial Markets", "Risk Management");
        put("Finance", "Senior", "Portfolio Management", "Derivatives", "Financial Modeling", "Mergers & Acquisitions");
        put("Finance", "Graduate", "Advanced Corporate Finance", "Behavioral Finance", "Quantitative Finance", "Fixed Income Analysis", "Dissertation Research");

        // ---- Accounting ----
        put("Accounting", "Freshman", "Intro to Business", "Microeconomics", "Macroeconomics", "Calculus I");
        put("Accounting", "Sophomore", "Financial Accounting", "Managerial Accounting", "Business Statistics", "Business Law I");
        put("Accounting", "Junior", "Intermediate Accounting I", "Intermediate Accounting II", "Cost Accounting", "Tax Accounting");
        put("Accounting", "Senior", "Auditing", "Advanced Accounting", "Forensic Accounting", "Accounting Information Systems");
        put("Accounting", "Graduate", "Advanced Auditing", "International Accounting", "Tax Research", "Financial Statement Analysis", "Dissertation Research");

        // ---- Marketing ----
        put("Marketing", "Freshman", "Intro to Business", "Microeconomics", "Introduction to Marketing", "Introduction to Psychology");
        put("Marketing", "Sophomore", "Consumer Behavior", "Business Statistics", "Digital Marketing", "Marketing Research");
        put("Marketing", "Junior", "Brand Management", "Advertising Strategy", "Social Media Marketing", "Sales Management");
        put("Marketing", "Senior", "Advanced Marketing Strategy", "Marketing Analytics", "Global Marketing", "Senior Thesis");
        put("Marketing", "Graduate", "Strategic Marketing", "Digital Analytics", "Consumer Insights", "Product Management", "Dissertation Research");

        // ---- Economics ----
        put("Economics", "Freshman", "Microeconomics", "Macroeconomics", "Calculus I", "Introduction to Statistics");
        put("Economics", "Sophomore", "Intermediate Microeconomics", "Intermediate Macroeconomics", "Calculus II", "Probability & Statistics");
        put("Economics", "Junior", "Econometrics", "International Economics", "Labor Economics", "Public Economics");
        put("Economics", "Senior", "Game Theory", "Development Economics", "Financial Economics", "Senior Thesis");
        put("Economics", "Graduate", "Advanced Microeconomics", "Advanced Macroeconomics", "Econometrics", "Behavioral Economics", "Dissertation Research");

        // ---- Psychology ----
        put("Psychology", "Freshman", "Introduction to Psychology", "Introduction to Statistics", "General Biology I", "Introduction to Sociology");
        put("Psychology", "Sophomore", "Abnormal Psychology", "Developmental Psychology", "Research Methods", "Biopsychology");
        put("Psychology", "Junior", "Cognitive Psychology", "Social Psychology", "Personality Psychology", "Psychological Testing");
        put("Psychology", "Senior", "Clinical Psychology", "Neuropsychology", "Health Psychology", "Senior Thesis");
        put("Psychology", "Graduate", "Advanced Clinical Psychology", "Neuropsychology", "Psychopharmacology", "Advanced Research Methods", "Dissertation Research");

        // ---- Nursing ----
        put("Nursing", "Freshman", "Anatomy & Physiology I", "General Chemistry I", "Introduction to Nursing", "Nutrition");
        put("Nursing", "Sophomore", "Anatomy & Physiology II", "Microbiology", "Pathophysiology", "Pharmacology I");
        put("Nursing", "Junior", "Medical-Surgical Nursing", "Maternal-Child Nursing", "Psychiatric Nursing", "Pharmacology II");
        put("Nursing", "Senior", "Community Health Nursing", "Nursing Leadership", "Critical Care Nursing", "Senior Practicum");
        put("Nursing", "Graduate", "Advanced Pathophysiology", "Advanced Pharmacology", "Health Policy", "Clinical Practicum", "Dissertation Research");

        // ---- Pre-Med ----
        put("Pre-Med", "Freshman", "General Biology I", "General Biology II", "General Chemistry I", "General Chemistry II", "Calculus I");
        put("Pre-Med", "Sophomore", "Organic Chemistry I", "Organic Chemistry II", "General Physics I", "General Physics II", "Anatomy & Physiology I");
        put("Pre-Med", "Junior", "Biochemistry", "Genetics", "Microbiology", "Cell Biology", "Biostatistics");
        put("Pre-Med", "Senior", "Immunology", "Pharmacology", "Neuroscience", "Medical Ethics", "MCAT Preparation");
        put("Pre-Med", "Graduate", "Advanced Biochemistry", "Molecular Genetics", "Epidemiology", "Clinical Research", "Dissertation Research");

        // ---- Public Health ----
        put("Public Health", "Freshman", "Intro to Public Health", "General Biology I", "Introduction to Statistics", "Introduction to Sociology");
        put("Public Health", "Sophomore", "Epidemiology", "Biostatistics", "Environmental Health", "Health Behavior");
        put("Public Health", "Junior", "Health Policy", "Community Health", "Global Health", "Research Methods");
        put("Public Health", "Senior", "Program Planning & Evaluation", "Health Disparities", "Public Health Ethics", "Senior Practicum");
        put("Public Health", "Graduate", "Advanced Epidemiology", "Health Policy", "Biostatistics II", "Environmental Health Sciences", "Dissertation Research");

        // ---- Political Science ----
        put("Political Science", "Freshman", "Introduction to Political Science", "US Government", "Microeconomics", "World History");
        put("Political Science", "Sophomore", "Comparative Politics", "International Relations", "Political Theory", "Research Methods");
        put("Political Science", "Junior", "Constitutional Law", "Public Policy", "Political Campaigns", "Diplomacy & Foreign Policy");
        put("Political Science", "Senior", "International Law", "Political Economy", "Senior Thesis", "Washington Seminar");
        put("Political Science", "Graduate", "Advanced Political Theory", "Comparative Institutions", "Quantitative Methods", "International Security", "Dissertation Research");

        // ---- Criminal Justice ----
        put("Criminal Justice", "Freshman", "Introduction to Criminal Justice", "Introduction to Sociology", "English Composition", "Introduction to Psychology");
        put("Criminal Justice", "Sophomore", "Criminology", "Policing in America", "Corrections", "Research Methods");
        put("Criminal Justice", "Junior", "Criminal Law", "Juvenile Justice", "Victimology", "Forensic Science");
        put("Criminal Justice", "Senior", "Cybercrime", "White Collar Crime", "Criminal Justice Administration", "Senior Internship");
        put("Criminal Justice", "Graduate", "Advanced Criminological Theory", "Criminal Justice Policy", "Advanced Forensics", "Research Design", "Dissertation Research");

        // ---- English ----
        put("English", "Freshman", "English Composition", "Introduction to Literature", "College Writing", "World Literature");
        put("English", "Sophomore", "American Literature", "British Literature", "Creative Writing", "Literary Analysis");
        put("English", "Junior", "Shakespeare", "Post-Colonial Literature", "Poetry Workshop", "Rhetoric");
        put("English", "Senior", "Literary Theory", "Advanced Creative Writing", "Contemporary Fiction", "Senior Thesis");
        put("English", "Graduate", "Seminar in Literary Theory", "Advanced Rhetoric", "Creative Thesis", "Pedagogy of Writing", "Dissertation Research");

        // ---- History ----
        put("History", "Freshman", "US History", "World History", "Western Civilization", "English Composition");
        put("History", "Sophomore", "Ancient History", "Medieval History", "African American History", "Research Methods");
        put("History", "Junior", "Modern European History", "American Civil War", "History of East Asia", "Public History");
        put("History", "Senior", "Historiography", "Senior Thesis", "Oral History Methods", "Archival Research");
        put("History", "Graduate", "Historical Methods", "Seminar in American History", "Comparative World History", "Digital History", "Dissertation Research");

        // ---- Communications ----
        put("Communications", "Freshman", "Intro to Communications", "Public Speaking", "Media & Society", "English Composition");
        put("Communications", "Sophomore", "Digital Media", "Media Writing", "Visual Communication", "Communication Theory");
        put("Communications", "Junior", "Public Relations", "Journalism Ethics", "Strategic Communication", "Social Media Strategy");
        put("Communications", "Senior", "Crisis Communication", "Media Law", "Advanced Public Relations", "Senior Portfolio");
        put("Communications", "Graduate", "Communication Research", "Advanced Media Theory", "Organizational Communication", "Global Communication", "Dissertation Research");

        // ---- Education ----
        put("Education", "Freshman", "Foundations of Education", "Introduction to Psychology", "English Composition", "Child Development");
        put("Education", "Sophomore", "Educational Psychology", "Curriculum Design", "Diversity in Education", "Classroom Technology");
        put("Education", "Junior", "Methods of Teaching", "Classroom Management", "Assessment & Evaluation", "Student Teaching I");
        put("Education", "Senior", "Student Teaching II", "Special Education", "Educational Leadership", "Senior Portfolio");
        put("Education", "Graduate", "Curriculum Theory", "Advanced Educational Psychology", "Educational Research", "School Administration", "Dissertation Research");

        // ---- Neuroscience ----
        put("Neuroscience", "Freshman", "General Biology I", "General Chemistry I", "Introduction to Psychology", "Calculus I");
        put("Neuroscience", "Sophomore", "Neuroscience Fundamentals", "Organic Chemistry I", "Biopsychology", "Introduction to Statistics");
        put("Neuroscience", "Junior", "Cellular Neuroscience", "Cognitive Neuroscience", "Neuroanatomy", "Behavioral Neuroscience");
        put("Neuroscience", "Senior", "Neuropsychology", "Computational Neuroscience", "Neuropharmacology", "Senior Thesis");
        put("Neuroscience", "Graduate", "Advanced Neuroscience", "Neural Systems", "Brain Imaging Methods", "Neurodegenerative Disease", "Dissertation Research");

        // ---- Philosophy ----
        put("Philosophy", "Freshman", "Introduction to Philosophy", "Logic", "Ethics", "World History");
        put("Philosophy", "Sophomore", "Ancient Philosophy", "Modern Philosophy", "Epistemology", "Political Philosophy");
        put("Philosophy", "Junior", "Metaphysics", "Philosophy of Mind", "Philosophy of Science", "Existentialism");
        put("Philosophy", "Senior", "Advanced Ethics", "Philosophy of Language", "Continental Philosophy", "Senior Thesis");
        put("Philosophy", "Graduate", "Seminar in Metaphysics", "Advanced Logic", "Philosophy of Consciousness", "Hermeneutics", "Dissertation Research");

        // ---- Sociology ----
        put("Sociology", "Freshman", "Introduction to Sociology", "Introduction to Psychology", "Introduction to Statistics", "English Composition");
        put("Sociology", "Sophomore", "Social Stratification", "Race & Ethnicity", "Gender & Society", "Research Methods");
        put("Sociology", "Junior", "Urban Sociology", "Sociology of Education", "Social Movements", "Quantitative Methods");
        put("Sociology", "Senior", "Sociological Theory", "Global Sociology", "Senior Thesis", "Applied Sociology");
        put("Sociology", "Graduate", "Advanced Sociological Theory", "Advanced Research Methods", "Comparative Sociology", "Political Sociology", "Dissertation Research");

        // ---- Environmental Science ----
        put("Environmental Science", "Freshman", "Environmental Science", "General Biology I", "General Chemistry I", "Calculus I");
        put("Environmental Science", "Sophomore", "Ecology", "Geology", "GIS & Remote Sensing", "Introduction to Statistics");
        put("Environmental Science", "Junior", "Environmental Policy", "Conservation Biology", "Hydrology", "Soil Science");
        put("Environmental Science", "Senior", "Climate Change Science", "Environmental Impact Assessment", "Sustainability", "Senior Thesis");
        put("Environmental Science", "Graduate", "Advanced Ecology", "Environmental Modeling", "Watershed Science", "Environmental Law", "Dissertation Research");

        // ---- Information Technology ----
        put("Information Technology", "Freshman", "Intro to IT", "Intro to Programming", "College Algebra", "English Composition");
        put("Information Technology", "Sophomore", "Networking Fundamentals", "Database Fundamentals", "Systems Administration", "Web Development");
        put("Information Technology", "Junior", "IT Project Management", "Cloud Infrastructure", "Information Security", "Business Analysis");
        put("Information Technology", "Senior", "Enterprise Architecture", "IT Governance", "DevOps & Automation", "Senior Practicum");
        put("Information Technology", "Graduate", "Advanced Cloud Computing", "IT Strategy", "Cybersecurity Management", "Digital Transformation", "Dissertation Research");

        // ---- Information Systems ----
        put("Information Systems", "Freshman", "Intro to Information Systems", "Intro to Programming", "Microeconomics", "College Algebra");
        put("Information Systems", "Sophomore", "Database Management", "Systems Analysis & Design", "Business Statistics", "Web Development");
        put("Information Systems", "Junior", "Enterprise Systems", "IT Project Management", "Business Intelligence", "Data Analytics");
        put("Information Systems", "Senior", "IS Strategy", "Cybersecurity Management", "ERP Systems", "Senior Practicum");
        put("Information Systems", "Graduate", "Advanced Business Intelligence", "IT Governance", "Digital Innovation", "Enterprise Architecture", "Dissertation Research");

        // ---- Management / Supply Chain / HR / Hospitality - fall back to Business ----
        for (String bMajor : List.of("Management", "Supply Chain Management", "Human Resources", "Hospitality Management")) {
            put(bMajor, "Freshman", "Intro to Business", "Microeconomics", "Macroeconomics", "College Algebra");
            put(bMajor, "Sophomore", "Financial Accounting", "Managerial Accounting", "Business Statistics", "Introduction to Management");
            put(bMajor, "Junior", "Organizational Behavior", "Operations Management", "Business Law", "International Business");
            put(bMajor, "Senior", "Strategic Management", "Entrepreneurship", "Business Ethics", "Senior Thesis");
            put(bMajor, "Graduate", "Advanced Corporate Finance", "Global Strategy", "Leadership & Organizations", "Change Management", "Dissertation Research");
        }

        // ---- Pre-Law ----
        put("Pre-Law", "Freshman", "Introduction to Political Science", "US Government", "English Composition", "Introduction to Philosophy");
        put("Pre-Law", "Sophomore", "Constitutional Law", "Logic", "American Literature", "Research Methods");
        put("Pre-Law", "Junior", "Criminal Law", "Business Law", "International Law", "Rhetoric");
        put("Pre-Law", "Senior", "Legal Writing", "Civil Liberties", "Mock Trial", "LSAT Preparation");
        put("Pre-Law", "Graduate", "Advanced Constitutional Law", "Jurisprudence", "Legal Research", "Administrative Law", "Dissertation Research");
    }
}
