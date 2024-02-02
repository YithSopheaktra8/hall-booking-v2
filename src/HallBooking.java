import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HallBooking {
    public static int hallRow;
    public static int hallColumn;
    public static String[][] morningHall;
    public static String[][] afternoonHall;
    public static String[][] nightHall;
    public static String[] bookingHistory = new String[50];
    public static Scanner input = new Scanner(System.in);
    public static int historyIndex = 0;

    public static void main(String[] args) {
        initializeHall();
        availableHall();

        char ch;
        do {
            mainMenu();
            ch = validateInputChar("> Choose option : ","+".repeat(60)+"\n# Option must be alphabet from A-G\n"+"+".repeat(60),"[a-gA-G]+",input);
            switch (ch){
                case 'a' -> booking();
                case 'b' -> showAllHall();
                case 'c' -> showTimeMenu();
                case 'd' -> availableHall();
                case 'e' -> displayBookingHistory();
                case 'f' -> searchUserByName();
                case 'g' -> {
                    System.out.println("Good bye See you again!!");
                    System.exit(0);
                }
                default -> System.out.println("+".repeat(60)+"\n# Please input option from A-F\n"+"+".repeat(60));
            }
        }while (true);
    }
    // init hall
    public static void initializeHall(){
        hallRow = validateInputNumber("> Config total rows in hall : ","only number from 1-9",input);
        hallColumn = validateInputNumber("> Config total columns in hall : ","only number from 1-9",input);
        morningHall = new String[hallRow][hallColumn];
        afternoonHall = new String[hallRow][hallColumn];
        nightHall = new String[hallRow][hallColumn];
    }
    // Show All Hall
    public static void showAllHall(){
        // morning
        System.out.println("# Hall information");
        System.out.println("+".repeat(60));
        System.out.println("# Hall - Morning");
        loopHall(morningHall);
        //  afternoon
        System.out.println("# Hall - Afternoon");
        loopHall(afternoonHall);
        //  night
        System.out.println("# Hall - Night");
        loopHall(nightHall);
    }
    // get hall by user Input
    public static String[][] getHall(Character choice){
        if(choice.equals('a')){
            return morningHall;
        } else if (choice.equals('b')) {
            return afternoonHall;
        }else
            return nightHall;
    }
    // loop through hall
    public static void loopHall(String[][] hall){
        for (int i = 0; i<hall.length; i++){
            for (int j= 0; j<hall[i].length; j++){
                char alphabet = (char) ('A' + i ) ;
                System.out.print("  |"+alphabet+"-"+(1+j)+"::"+hall[i][j]+"|\t");
            }
            System.out.println();
        }
        System.out.println("+".repeat(60));
    }
    // available hall
    public static void availableHall(){
        initHallToAvailable(morningHall);
        initHallToAvailable(afternoonHall);
        initHallToAvailable(nightHall);
    }
    // make all hall to available
    public static void initHallToAvailable(String[][] hall){
        for (String[] strings : hall) {
            Arrays.fill(strings, "AV");
        }
    }
    // booking hall
    public static void booking(){
        Scanner scanner = new Scanner(System.in);
        showTimeMenu();
        Character choice = validateInputChar("> Please select show time (A | B | C): ","Please input A-B-C","[a-cA-C]+",scanner);
        String[][] hall = getHall(choice);
        loopHall(hall);
        String[] userInput = singleAndMultipleSelect();
        String seat = Arrays.toString(userInput);
        boolean isTrue = true;
        boolean validateInputUser = true;
        char isSure = 0;
        String userName = "";
        for (String input : userInput) {
            String getUserInput = input.replaceAll("-", "");
            int number = Integer.parseInt(getUserInput.replaceAll("[^0-9]", ""));
            char letter = getUserInput.replaceAll("[^a-zA-Z]", "").charAt(0);
            int convertFromCharToInt = Character.getNumericValue(letter);
            char convertFromIntToChar = (char) ((char) hallRow +65);
            int rowIntWord = Character.getNumericValue(convertFromIntToChar);
            if(convertFromCharToInt < rowIntWord && number <= hallColumn){
                for (int i = 0; i < hall.length; i++) {
                    for (int j = 0; j < hall[i].length; j++) {
                        char alphabet = (char) ('A' + i);
                        if (alphabet == letter && (number - 1 ) == j) {
                            if (hall[i][j].equals("BO")) {
                                System.out.println("+".repeat(60));
                                System.out.println("!! ["+alphabet+"-"+(1+j)+"] already booked!");
                                System.out.println("!! ["+alphabet+"-"+(1+j)+"] cannot be booked because of unavailability!");
                                System.out.println("+".repeat(60));
                                isTrue = false;
                            } else {
                                if(validateInputUser){
                                    userName = validateInputString("> Please enter userName : ", "!! Name can not be special character!","[a-zA-Z\\s]+", scanner);
                                    isSure = validateInputChar("> Are you sure to book? (Y/N) : ", "!! please input Y or N ","[yYNn]+", scanner);
                                    validateInputUser = false;
                                }
                                if (isSure == 'y') {
                                    hall[i][j] = "BO";
                                    // Update the booking history array
                                    String history = addToHistory(seat, userName , choice);
                                    bookingHistory[historyIndex] = history;
                                }else {
                                    System.out.println("+".repeat(60));
                                    System.out.println("> Cancel Booking...........");
                                    System.out.println("> Done cancel");
                                    System.out.println("+".repeat(60));
                                    isTrue = false;
                                }
                            }
                        }
                    }
                }
            }else {
                String[] rowInAlphabet = showRowAlphabet(hallRow);
                String[] column = showColumn(hallColumn);
                System.out.println("+".repeat(60));
                System.out.println("! seat not found!!");
                System.out.println("! seat available from "+ Arrays.toString(rowInAlphabet)+" and "+ Arrays.toString(column));
                System.out.println("+".repeat(60));
                isTrue = false;
                break;
            }

        }
        if(isTrue){
            System.out.println("+".repeat(60));
            System.out.println("# " + seat + " booked successfully.");
            System.out.println("+".repeat(60));
            historyIndex++;
        }
    }

    // show total column
    public static String[] showColumn(int column){
        String[] array = new String[column];
        for (int i =0; i<column; i++){
            array[i] = String.valueOf(i+1);
        }
        return array;
    }
    // show total row in alphabet
    public static String[] showRowAlphabet(int row){
        String[] array = new String[row];
        for (int i = 0; i<row; i++){
            array[i] =  String.valueOf((char) ('A' + i));
        }
        return array;
    }
    // add to history
    public static String addToHistory(String seat, String userName , Character choice){
        UUID uuid = UUID.randomUUID();
        String uniqueID =uuid.toString();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y hh:mm");
        String formattedDateTime = localDateTime.format(formatter);
        char hall = Character.toUpperCase(choice);
        return String.format(
                "#SEATS: " + seat  +
                        "\n#BookingID : " +uniqueID+
                        "\n#HALL         #USER.NAME               #CREATED AT" +
                        "\nHALL %-8s %-23s  %-20s  "
                ,hall,userName.toUpperCase(),formattedDateTime);
    }
    // single and multiple select method
    public static String[] singleAndMultipleSelect(){
        Scanner input = new Scanner(System.in);
        // Input strings dynamically
        while (true) {
            System.out.print("""
                    # INSTRUCTION
                    # SINGLE : C-1
                    # Multiple (Separate by comma (,)) : C-1,C-2
                    """);
            String userInput = validateInputString("> Please select available seat : ","# !! Please input base on Instruction !","([a-zA-Z]-[1-9],)*[a-zA-Z]-[1-9]",input).toUpperCase();
            // Split the input string by commas
            String[] substrings = userInput.split(",");
            
            // Check if the user wants to stop (pressing Enter key)
            if (!(userInput.isEmpty())) {
                return substrings;
            }
        }
    }

    // show booking history
    public static void displayBookingHistory() {
        boolean isFound = false;
        System.out.println("+".repeat(60));
        System.out.println("# Booking History:");
        for (String s : bookingHistory) {
            if (!Objects.equals(s, "") && s != null) {
                System.out.println("-".repeat(60));
                System.out.println(s);
                System.out.println("-".repeat(60));
                System.out.println("+".repeat(60));
                isFound = true;
            }
        }
        if(!isFound){
            System.out.println("-".repeat(60));
            System.out.println("                There is no history");
            System.out.println("-".repeat(60));
            System.out.println("+".repeat(60));
        }
    }

    //  validate char
    public static Character validateInputChar(String message, String error, String stringPattern , Scanner input){
        while (true){
            System.out.print(message);
            String choice = input.nextLine();
            Pattern pattern = Pattern.compile(stringPattern);
            Matcher matcher = pattern.matcher(choice);
            if(matcher.matches())
                return Character.toLowerCase(choice.charAt(0));
            else
                System.out.println(error);
        }
    }

    //    validate number
    public static Integer validateInputNumber(String message, String error, Scanner input){
        while (true){
            System.out.print(message);
            String choice = input.nextLine();
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher(choice);
            if(matcher.matches()){
                if(!(Integer.parseInt(choice) == 0))
                    return Integer.parseInt(choice);
                else
                    System.out.println("Can not be ZERO");
            }
            else
                System.out.println(error);
        }
    }

    // search user when they already booked
    public static void searchUserByName(){
        System.out.println("-".repeat(60));
        System.out.print("""
                    # INSTRUCTION
                    # EXAMPLE USERNAME : YITH SOPHEAKTRA
                    # YOU CAN INPUT ONLY Y OR YI OR S OR SO
                    # IT WILL LIST ALL THE RESULT BASE ON YOUR INPUT
                    """);
        System.out.println("-".repeat(60));
        String userInput = validateInputString("> Please enter userName : ", "!! Name can not be special character!","[a-zA-Z\\s]+", input).toUpperCase();
        boolean isFound = false;
        for (String history : bookingHistory) {
            if (history != null) {
                String regex = "HALL\\s+[a-zA-Z]+\\s+.*" + Pattern.quote(userInput);
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(history);
                if (matcher.find()) {
                    System.out.println("-".repeat(60));
                    System.out.println(history);
                    System.out.println("-".repeat(60));
                    System.out.println("+".repeat(60));
                    isFound = true;
                }
            }

        }
        if(!isFound){
            System.out.println("! No data found!");
        }
    }

    // validate String
    public static String validateInputString(String message, String error, String patternString, Scanner input ){
        while (true){
            System.out.print(message);
            String choice = input.nextLine();
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(choice);
            if(matcher.matches()){
                return choice;
            }
            else
                System.out.println(error);
        }
    }

    public static void mainMenu () {
        System.out.print("""
                    [[ Application Menu ]]
                    <A> Booking
                    <B> Hall
                    <C> Showtime
                    <D> Reboot Showtime
                    <E> History
                    <F> SearchByUsername
                    <G> Exit
                    """);
    }

    public static void showTimeMenu () {
        System.out.print("""
                    +++++++++++++++++++++++++++++++++++++++++++++++
                    # Daily Showtime of CSTAD HALL:
                    # A) Morning (10:00AM - 12:30PM)
                    # B) Afternoon (03:00PM - 50:30PM)
                    # C) Night (07:00PM - 09:30PM)
                    ++++++++++++++++++++++++++++++++++++++++++++++
                    """);
    }
}
