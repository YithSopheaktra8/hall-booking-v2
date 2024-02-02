import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HallBooking {
    public static Integer row;
    public static Integer column;
    public static String[][] morningHall;
    public static String[][] afternoonHall;
    public static String[][] nightHall;
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        initializeHall();
        availableHall();

        char ch;
        do {
            mainMenu();
            ch = validateInputChar("> Choose option : ","+".repeat(60)+"\n# Option must be alphabet from A-F\n"+"+".repeat(60),"[a-fA-F]+",input);
            switch (ch){
                case 'b' -> showAllHall();
                case 'c' -> showTimeMenu();
                case 'd' -> availableHall();
                case 'f' -> {
                    System.out.println("Good bye See you again!!");
                    System.exit(0);
                }
                default -> System.out.println("+".repeat(60)+"\n# Please input option from A-F\n"+"+".repeat(60));
            }
        }while (true);
    }
    // init hall
    public static void initializeHall(){
        row = validateInputNumber("> Config total rows in hall : ","only number from 1-9",input);
        column = validateInputNumber("> Config total rows in hall : ","only number from 1-9",input);
        morningHall = new String[row][column];
        afternoonHall = new String[row][column];
        nightHall = new String[row][column];
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
    public static String[] singleAndMultipleSelect(){
        Scanner input = new Scanner(System.in);
        String[] stringArray = new String[0];
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
                    <F> Exit
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
