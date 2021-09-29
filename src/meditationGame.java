import java.util.*;
import java.io.*;

public class meditationGame {

    Player mainCharacter;
    Day day;

    String beginningText;
    String endingText;

    ArrayList<TestQuestion> easyList;
    ArrayList<TestQuestion> mediumList;
    ArrayList<TestQuestion> hardList;
    ArrayList<Meditation> meditationList;
    ArrayList<String> badWakeupAdjectives;
    ArrayList<String> medWakeupAdjectives;
    ArrayList<String> goodWakeupAdjectives;
    ArrayList<String> badBreakfastAdjectives;
    ArrayList<String> medBreakfastAdjectives;
    ArrayList<String> goodBreakfastAdjectives;
    ArrayList<String> badBreakfasts;
    ArrayList<String> medBreakfasts;
    ArrayList<String> goodBreakfasts;


    String[] testOutput;
    String[] meditationOutput;
    String[] dayBeginOutput;


    public class Player {
        String name;
        int mindfulnessScore;
        boolean meditatedYesterday;
        int daysMeditatedInARow;

        public Player(String userName){
            name = userName;
            meditatedYesterday = false;
            daysMeditatedInARow = 0;

        }

        public int increaseMindfulness(int increase){
            this.mindfulnessScore += increase;
            if(this.mindfulnessScore > 100) this.mindfulnessScore = 100;
            return this.mindfulnessScore;
        }

    }

    void initialize(String testQuestionsFileName, String meditationsFileName, String dayBeginFileName, String splashTextFileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        testOutput = readFileFromScanner(testQuestionsFileName);
        meditationOutput = readFileFromScanner(meditationsFileName);
        dayBeginOutput = readFileFromScanner(dayBeginFileName);
        String[] splashOutput = readFileFromScanner(splashTextFileName);
        beginningText = splashOutput[0].replaceAll(";", "\n");
        endingText = splashOutput[1].replaceAll(";", "\n");

        easyList = new ArrayList<>();
        mediumList = new ArrayList<>();
        hardList = new ArrayList<>();
        meditationList = new ArrayList<>();
        badWakeupAdjectives = new ArrayList<>();
        medWakeupAdjectives = new ArrayList<>();
        goodWakeupAdjectives = new ArrayList<>();
        badBreakfastAdjectives = new ArrayList<>();
        medBreakfastAdjectives = new ArrayList<>();
        goodBreakfastAdjectives = new ArrayList<>();
        badBreakfasts = new ArrayList<>();
        medBreakfasts = new ArrayList<>();
        goodBreakfasts = new ArrayList<>();

        ArrayList<ArrayList<String>> toAddLists = new ArrayList<>();
        toAddLists.add(badWakeupAdjectives);
        toAddLists.add(medWakeupAdjectives);
        toAddLists.add(goodWakeupAdjectives);
        toAddLists.add(badBreakfastAdjectives);
        toAddLists.add(medBreakfastAdjectives);
        toAddLists.add(goodBreakfastAdjectives);
        toAddLists.add(badBreakfasts);
        toAddLists.add(medBreakfasts);
        toAddLists.add(goodBreakfasts);
        String[] toAddFrom;

        for(int i = 0; i < meditationOutput.length; i++) meditationList.add(new Meditation(meditationOutput[i], scanner, i+1));

        for(int i = 0; i < testOutput.length; i++){
            if(testOutput[i].charAt(0) == 'e'){
                easyList.add(new TestQuestion(testOutput[i]));
            }
            else if(testOutput[i].charAt(0) == 'm'){
                mediumList.add(new TestQuestion(testOutput[i]));
            }
            else if(testOutput[i].charAt(0) == 'h'){
                hardList.add(new TestQuestion(testOutput[i]));
            }
        }

        for(int i = 0; i < dayBeginOutput.length; i++){
            ArrayList<String> toAdd = toAddLists.get(i);
            toAddFrom = dayBeginOutput[i].split(";");
            for(int j = 0; j < toAddFrom.length; j++){
                toAdd.add(toAddFrom[j]);
            }
        }



    }

    public class Day{
        int dayNumber;
        int time;
        Scanner s;

        public Day(Scanner scanner){
            dayNumber = 0;
            time = 8;
            s = scanner;
        }

        public void display(){
            String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            String timeString = (((time / 12) < 1) || time == 24) ? time + ":00 AM" : time + ":00 PM";
            if(time == 0) timeString = "12:00 AM";
            String ret = "Day " + (dayNumber + 1) + ": " + days[dayNumber] + ", " + timeString;
            System.out.println(ret);
            System.out.println("__________________________________________________________");
        }

        public void increaseTime(int hours){
            time += hours;
            if(time > 24){
                time = time % 24;
                dayNumber++;
            }
        }

        public void end(){
            time = 8;
            dayNumber++;
        }

        public void startDay(){
            display();
            String feeling;
            String food;
            String foodAdj;

            double random = Math.random();

            int level;

            if(mainCharacter.mindfulnessScore < 50) level = 0;
            else if (mainCharacter.mindfulnessScore < 75) level = 1;
            else level = 2;

            if(level == 0){
                feeling = badWakeupAdjectives.get((int) (random * badWakeupAdjectives.size()));
                food = badBreakfasts.get((int) (random * badBreakfasts.size()));
                foodAdj = badBreakfastAdjectives.get((int) (random * badBreakfastAdjectives.size()));
            }
            else if(level == 1){
                feeling = medWakeupAdjectives.get((int) (random * medWakeupAdjectives.size()));
                food = medBreakfasts.get((int) (random * medBreakfasts.size()));
                foodAdj = medBreakfastAdjectives.get((int) (random * medBreakfastAdjectives.size()));
            }
            else {
                feeling = goodWakeupAdjectives.get((int) (random * goodWakeupAdjectives.size()));
                food = goodBreakfasts.get((int) (random * goodBreakfasts.size()));
                foodAdj = goodBreakfastAdjectives.get((int) (random * goodBreakfastAdjectives.size()));
            }

            int hoursOfSleep = (int) Math.ceil(3.0 * (mainCharacter.mindfulnessScore / 100.0));
            hoursOfSleep += 5;


            System.out.println("You got around " + hoursOfSleep + " hours of sleep last night.");
            System.out.println("You wake up feeling " + feeling + ".");
            System.out.println("For breakfast, you make " + food + ". It tastes " + foodAdj + ".");
            increaseTime(4);
            System.out.println("Enter any key to continue.");
            if(s.hasNext()) s.next();
        }

        public void atSchool() throws InterruptedException {
            display();
            System.out.println("It's time to take a test at school.");
            System.out.println("Enter any key to start the test.");
            if(s.hasNext()) s.next();

            if (mainCharacter.meditatedYesterday) System.out.println("You feel more mindful than yesterday,\n" +
                    "and as a result you feel like the test questions are easier and you have more time.");
            else System.out.println("You didn't meditate yesterday, so these test questions feel a little harder.");
            takeTest(5, s, easyList, mediumList, hardList);
            increaseTime(4);
            System.out.println("Enter any key to continue.");
            if(s.hasNext()) s.next();
        }
    }


    public class TestQuestion{
        String question;
        String correctAnswer;
        String[] answerArray;

        public TestQuestion(String input){
            answerArray = new String[4];
            String[] inputArray = input.split(";");
            question = inputArray[1].replace('@','\n');
            correctAnswer = inputArray[2];
            answerArray[0] = correctAnswer;
            answerArray[1] = inputArray[3];
            answerArray[2] = inputArray[4];
            answerArray[3] = inputArray[5];
        }

        public String[] getAnswerArray(){
            ArrayList<String> list = new ArrayList<>(Arrays.asList(answerArray));
            Collections.shuffle(list);

            String[] ret = new String[list.size()];
            for(int i = 0; i < list.size(); i++) {
                ret[i] = list.get(i);
                answerArray[i] = ret[i];
            }
            return ret;
        }
        @Override
        public String toString(){
            String[] arr = getAnswerArray();
            String ret = question;
            ret = ret + "\na) " + arr[0] + "\nb) " + arr[1] + "\nc) " + arr[2] + "\nd) " + arr[3];
            return ret;
        }
    }

    public class Meditation{
        String title;
        String desc;
        String[] lines;
        Scanner scan;
        int place;

        public Meditation(String input, Scanner s, int number){
            lines = input.split(";");
            scan = s;
            title = lines[0];
            desc = lines[1];
            place = number;
        }

        public void meditate() throws InterruptedException {
            String toPrint;
            System.out.println(title);
            for(int i = 2; i < lines.length; i++){
                toPrint = lines[i];
                int charsInLine = lines[i].length();
                System.out.println(toPrint);
                Thread.sleep(charsInLine * 100);
            }
            double multiplier = 1 + ((mainCharacter.daysMeditatedInARow - 1) * 0.25);
            for(int i = 0; i < 5; i++)System.out.println();
            System.out.println(mainCharacter.name + ", Great job meditating!");
            int increase = (int) Math.ceil(Math.random() * 15.0 * multiplier);

            mainCharacter.increaseMindfulness(increase);
            System.out.println("Your mindfulness score went up by " + increase + " points!\nYour new mindfulness score is: " + mainCharacter.mindfulnessScore);
            if(mainCharacter.daysMeditatedInARow > 1) System.out.println("Because you've meditated for multiple days in a row, your score increased by more!\nKeep it up!");

        }
        @Override
        public String toString() {
            String ret = place + ") ";
            ret += title + "\n" + desc;
            return ret;
        }

    }



    public String[] readFileFromScanner(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        ArrayList<String> list = new ArrayList<>();

        while (sc.hasNextLine()){
            list.add(sc.nextLine());
        }

        String[] ret = new String[list.size()];
        for(int i = 0; i < list.size(); i++) ret[i] = list.get(i);
        return ret;
    }

    public double takeTest(int questionsToAsk, Scanner s,
                        ArrayList<TestQuestion> easy,
                        ArrayList<TestQuestion> medium,
                        ArrayList<TestQuestion> hard) throws InterruptedException {
        double select;
        char response;
        ArrayList<TestQuestion> primaryList;
        ArrayList<TestQuestion> secondaryList;
        ArrayList<TestQuestion> addingList;
        HashSet<TestQuestion> asked = new HashSet<>();

        TestQuestion currentQuestion;
        int questionsAnswered = 0;
        int score = 0;

        if(mainCharacter.mindfulnessScore >= 90){
            primaryList = easy;
            secondaryList = easy;
        }
        else if(mainCharacter.mindfulnessScore >= 80){
            primaryList = easy;
            secondaryList = medium;
        }
        else if(mainCharacter.mindfulnessScore >= 70){
            primaryList = medium;
            secondaryList = easy;
        }
        else if(mainCharacter.mindfulnessScore >= 60){
            primaryList = medium;
            secondaryList = medium;
        }
        else if(mainCharacter.mindfulnessScore >= 50){
            primaryList = medium;
            secondaryList = hard;
        }
        else if(mainCharacter.mindfulnessScore >= 40){
            primaryList = hard;
            secondaryList = medium;
        }
        else{
            primaryList = hard;
            secondaryList = hard;
        }

        while(questionsAnswered < questionsToAsk){

            select = Math.random();
            if(select > 0.7) addingList = secondaryList;
            else addingList = primaryList;
            currentQuestion = addingList.get((int) Math.floor(addingList.size() * select));
            while(asked.contains(currentQuestion)){
                select = Math.random();
                currentQuestion = addingList.get((int) Math.floor(addingList.size() * select));
            }

            System.out.println("Question #" + (questionsAnswered + 1));
            System.out.println(currentQuestion);
            System.out.println();

            if(s.hasNext()) {
                response = s.next().charAt(0);
                if(response == 'a' || response == 'b' || response == 'c' || response == 'd') {
                    if (currentQuestion.answerArray[(response - 97)].equals(currentQuestion.correctAnswer)) score++;
                }
            }

            asked.add(currentQuestion);
            questionsAnswered++;

        }

        System.out.println(mainCharacter.name + ", you scored " + ((score*100)/questionsToAsk) + "%!");
        double scoreDouble = score * 1.0;
        scoreDouble /= questionsToAsk;

        return scoreDouble;
    }

    public void runMeditation(Scanner s, ArrayList<Meditation> list) throws InterruptedException {
        if(mainCharacter.meditatedYesterday) mainCharacter.daysMeditatedInARow++;

        else {
            mainCharacter.daysMeditatedInARow = 1;
            mainCharacter.meditatedYesterday = true;
        }

        System.out.println("You've meditated " + mainCharacter.daysMeditatedInARow + " days in a row!");

        String input = "";
        int index;
        boolean fail = true;

        System.out.println("Which meditation would you like to try?");
        for (int i = 0; i < list.size(); i++) System.out.println(list.get(i));
        while(fail = true){
            if(s.hasNext()) input = s.next();
            for(int i = 1; i < list.size() + 1; i++) if (input.equals(Integer.toString(i))) fail = false;
            if(!fail) break;
            System.out.println("Please enter a valid number!");
        }


        index = Integer.parseInt(input) - 1;
        if(index < list.size()) list.get(index).meditate();
        System.out.println("Enter f to finish meditating");
        if(s.hasNext()) s.next();
    }


    public void runGame(String testQuestionsFilename, String meditationFileName, String beginDayFileName, String splashTextFileName) throws FileNotFoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        initialize(testQuestionsFilename, meditationFileName, beginDayFileName, splashTextFileName);

        day = new Day(scanner);

        System.out.println(beginningText);

        System.out.println("Please enter your name:");
        mainCharacter = new Player(scanner.nextLine());

        mainCharacter.mindfulnessScore = 35;


        while(day.dayNumber < 6) {
            day.startDay();
            if(day.dayNumber > 0) day.atSchool();
            day.display();
            System.out.println("Would you like to meditate? (y)es or (n)o");

            String prompt = scanner.next();

            if (prompt.equals("y")) runMeditation(scanner, meditationList);
            else{
                mainCharacter.meditatedYesterday = false;
                mainCharacter.daysMeditatedInARow = 0;
            }
            day.end();
        }

        System.out.println("Congratulations, " + mainCharacter.name + "! Your final score was " + mainCharacter.mindfulnessScore + ".");
        if(mainCharacter.mindfulnessScore == 100) System.out.println("CONGRATS on a PERFECT score! You rock, mindfulness master!!!");
        else if(mainCharacter.mindfulnessScore > 90 && mainCharacter.mindfulnessScore < 100) System.out.println("You were so close to a PERFECT 100!");
        else System.out.println("Keep trying for a perfect score!!");

        System.out.println(endingText);


    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        meditationGame m = new meditationGame();
        m.runGame("./data/test.txt", "./data/meditations.txt", "./data/beginningDay.txt", "./data/splash.txt");
    }
}
