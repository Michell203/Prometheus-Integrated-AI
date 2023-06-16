import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Tester {
    static char[][] world = new char[10][10]; // X for object, B for bad guy, R for robot
    static int battery = 4;
    static int iter = 0;
    static int dir = 0; // 0 = Down, 1 = Up, 2 = Left, 3 = Right
    static boolean panels = false;

    public static void main(String[] args) throws Exception {
        String setupfilename    = "resources/setup.txt";
        String rulebasefilename = getFileName(setupfilename)[1];
        String factsfilename    = getFileName(setupfilename)[0];

        ArrayList<Clause> facts = new ArrayList<>();
        generate(world);

        for(int i=0; i<20; i++) {
            System.out.println("Input: ");

            facts.addAll(getSensorData());					// array list of facts (appended)
            Clause[] factsArr = facts.toArray(new Clause[facts.size()]);

            Productions p = new Productions(rulebasefilename, factsArr);
            p.think();
            ArrayList<Clause> output = p.getMarkedFacts();			// runs to quiescence

            System.out.println("Direction of R: " + dir);
            printBatt();
            decide(output);		// See ass3
            System.out.println("Marked Facts: " + p.getMarkedFacts());

            // Check to see if we won
            int[] xCoords = findY(world, 'X');
            if(xCoords[0] == -1){
                System.out.println("===== YOU WON =====");
                break;
            }

            if(moveB() == 1){
                System.out.println("===== GAME OVER =====");
                break;
            }

            printWorld(world);

            iter++;
            if(battery >= 0){
                battery--;
            }

            facts.clear(); // or, if useful, replace with: facts = loadFromFile(factsfilename);
            System.out.println("-------------------------");
        }
    }

    public static void generate(char[][] arr){ // Generate world with X, R, and B
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[i].length; j++){
                    arr[i][j] = '.';
            }
        }

        int count = 0;

        while(count < 3){
            if(count == 0){
                int n1 = (int)(Math.random() * 10);
                int n2 = (int)(Math.random() * 10);
                arr[n1][n2] = 'X';
                count++;

            } else if(count == 1){
                int n1 = (int)(Math.random() * 10);
                int n2 = (int)(Math.random() * 10);
                if(arr[n1][n2] == 'X'){
                    continue;
                }
                arr[n1][n2] = 'R';
                count++;

            } else if(count == 2){
                int n1 = (int)(Math.random() * 10);
                int n2 = (int)(Math.random() * 10);
                if(arr[n1][n2] == 'X' || arr[n1][n2] == 'R'){
                    continue;
                }
                arr[n1][n2] = 'B';
                count++;
            }
        }

    }

    public static int moveB(){
        int[] bCoords = findY(world, 'B');

        int rows = world.length;
        int cols = world[0].length;

        int bRow = bCoords[0];
        int bCol = bCoords[1];

        int direction = (int)(Math.random() * 4);  // 0: up, 1: down, 2: left, 3: right

        switch (direction) {
            case 0:  // Move up
                if (bRow > 0 && world[bRow - 1][bCol] != 'R') {
                    if(world[bRow - 1][bCol] == 'R'){
                        return 1;
                    }

                    world[bRow][bCol] = world[bRow - 1][bCol];
                    world[bRow - 1][bCol] = 'B';
                    return 0;

                }
//                break;

            case 1:  // Move down
                if (bRow < world.length - 1) {
                    if(world[bRow + 1][bCol] == 'R'){
                        return 1;
                    }

                    world[bRow][bCol] = world[bRow + 1][bCol];
                    world[bRow + 1][bCol] = 'B';
                    return 0;

                }
//                break;

            case 2:  // Move left
                if (bCol > 0 && world[bRow][bCol - 1] != 'R') {
                    if(world[bRow][bCol - 1] == 'R'){
                        return 1;
                    }

                    world[bRow][bCol] = world[bRow][bCol - 1];
                    world[bRow][bCol - 1] = 'B';
                    return 0;

                }
//                break;

            case 3:  // Move right
                if (bCol < world[0].length - 1 && world[bRow][bCol + 1] != 'R') {
                    if(world[bRow][bCol + 1] == 'R'){
                        return 1;
                    }

                    world[bRow][bCol] = world[bRow][bCol + 1];
                    world[bRow][bCol + 1] = 'B';
                    return 0;

                }
//                break;
        }

        return 0;
    }

    public static void moveR(char[][] arr, String mode, char obj){

        int[] rCoords = findY(arr, 'R');

        int rows = arr.length;
        int cols = arr[0].length;

        int rRow = rCoords[0];
        int rCol = rCoords[1];

        if(Objects.equals(mode, "move")){
            if(dir == 0 && rRow < 9){ // Facing Down
                if(world[rRow + 1][rCol] == 'X'){
                    System.out.println("===== YOU WON =====");
                }

                world[rRow][rCol] = world[rRow + 1][rCol];
                world[rRow + 1][rCol] = 'R';

            } else if(dir == 1 && rRow > 0){ // Facing Up
                if(world[rRow - 1][rCol] == 'X'){
                    System.out.println("===== YOU WON =====");
                }

                world[rRow][rCol] = world[rRow - 1][rCol];
                world[rRow - 1][rCol] = 'R';

            } else if(dir == 2 && rCol < 9){ // Facing Left
                if(world[rRow][rCol - 1] == 'X'){
                    System.out.println("===== YOU WON =====");
                }

                world[rRow][rCol] = world[rRow][rCol - 1];
                world[rRow][rCol - 1] = 'R';

            } else if(dir == 3 && rCol > 0){ // Facing Right
                if(world[rRow][rCol + 1] == 'X'){
                    System.out.println("===== YOU WON =====");
                }

                world[rRow][rCol] = world[rRow][rCol + 1];
                world[rRow][rCol + 1] = 'R';

            }

        } else if (Objects.equals(mode, "turn")){
//            turn90DegRight(world, dir);
            while(direction(world,obj,rRow,rCol) != 'N'){
                turn90DegRight(world);
            }

        }

    }

    public static void printWorld(char[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String[] getFileName(String s) throws FileNotFoundException { // 0: Facts, 1: Rule Base
        File f = new File(s);
        Scanner myReader = new Scanner(f);
        String[] names = new String[2];
        int i = 0;

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            names[i] = data;
            i++;
        }
        myReader.close();

        return names;
    }

    public static Clause[] loadFactsFromFile(String fname) throws Exception {
        File f = new File(fname);
        Scanner myReader = new Scanner(f);
        List<Clause> factsList = new ArrayList<Clause>();

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            factsList.add(new Clause(data));
            System.out.println(data);
        }
        myReader.close();

        Clause[] facts = new Clause[factsList.size()];
        factsList.toArray(facts);

        return facts;
    }

    public static int[] findY(char[][] world, char y){
        int rows = world.length;
        int cols = world[0].length;
        int[] coords = new int[2];

        coords[0] = -1;
        coords[1] = -1;

        // Find the position of 'R' in the world
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (world[i][j] == y) {
                    coords[0] = i; // Row
                    coords[1] = j; // Col
                    break;
                }
            }
        }

        return coords;
    }

    public static boolean aroundR(char[][] arr, char obj, int spaces){
        int[] rCoords = findY(arr, 'R');

        int rows = arr.length;
        int cols = arr[0].length;

        int rRow = rCoords[0];
        int rCol = rCoords[1];

        for (int i = rRow - spaces; i <= rRow + spaces; i++) {
            for (int j = rCol - spaces; j <= rCol + spaces; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && arr[i][j] == obj) {
                    return true;
                }
            }
        }

        return false;
    }

    public static int distanceOfObj(char obj){
        int[] rCoords = findY(world, 'R');
        int[] objCoords = findY(world, obj);

        int objRow = objCoords[0];
        int objCol = objCoords[1];

        int rRow = rCoords[0];
        int rCol = rCoords[1];

        int rowDistance = Math.abs(rRow - objRow);
        int colDistance = Math.abs(rCol - objCol);
        return rowDistance + colDistance;
    }

    public static boolean touchedFrontBorder(char[][] arr, int rRow, int rCol){
        if(dir == 0){ // Down
            if (rRow == 9) return true;

        } else if(dir == 1){ // Up
            if (rRow == 0) return true;

        } else if(dir == 2){ // Left
            if (rCol == 9) return true;

        } else { // Right
            if (rCol == 0) return true;

        }

        return false;
    }

    public static void printBatt(){
        System.out.print("Battery: ");
        for(int i = battery; i > 0; i--){
            System.out.print('▉');
        }
        System.out.println();
    }

    public static void charge(String type){
        if(Objects.equals(type, "full")){
            battery = 5;

        } else {
            if(battery == 3){
                battery += 2;
            } else {
                battery += 3;
            }
        }
    }

    public static void turn90DegRight(char[][] arr){
        if(dir == 0){ // Down to Left
            dir = 2;
        } else if(dir == 1){ // Up to Right
            dir = 3;
        } else if(dir == 2){ // Left to Up
            dir = 1;
        } else { // Right to Down
            dir = 0;
        }
    }

    public static char direction(char[][] arr, char obj, int rRow, int rCol){ // Direction is relative
        int[] objCoords = findY(world, obj);

        int objRow = objCoords[0];
        int objCol = objCoords[1];

        // 0 = Down, 1 = Up, 2 = Left, 3 = Right

        if(dir == 0){ // Down
            if(objRow > rRow){
                return 'N';
            } else if(objRow < rRow){
                return 'S';
            } else if(objCol < rCol){
                return 'E';
            } else {
                return 'W';
            }

        } else if(dir == 1){ // Up
            if(objRow > rRow){
                return 'S';
            } else if(objRow < rRow){
                return 'N';
            } else if(objCol > rCol){
                return 'E';
            } else {
                return 'W';
            }

        } else if(dir == 2){ // Left
            if(objRow < rRow){
                return 'E';
            } else if(objRow > rRow){
                return 'W';
            } else if(objCol > rCol){
                return 'S';
            } else {
                return 'N';
            }

        } else{ // Right
            if(objRow < rRow){
                return 'W';
            } else if(objRow > rRow){
                return 'N';
            } else if(objCol < rCol){
                return 'S';
            } else {
                return 'N';
            }
        }

    }

    public static ArrayList<Clause> getSensorData() throws Exception {
        ArrayList<Clause> facts = new ArrayList<>();
        int[] rCoords = findY(world, 'R');

        int rows = world.length;
        int cols = world[0].length;

        int rRow = rCoords[0];
        int rCol = rCoords[1];

        char dirX = direction(world, 'X', rRow, rCol);
        char dirB = direction(world, 'B', rRow, rCol);

        boolean visX = false;
        boolean proxB = false;

//        if(rRow == 0 || rRow == rows-1 || rCol == 0 || rCol == cols-1){
//            facts.add(new Clause("ADJACENT(BORDER)"));
//            System.out.println("ADJACENT(BORDER)");
//        }

        if(touchedFrontBorder(world,rRow,rCol)){
            facts.add(new Clause("TOUCHEDFRONT(BORDER)"));
            System.out.println("TOUCHEDFRONT(BORDER)");
        }

        if(aroundR(world,'X',4)){
            facts.add(new Clause("VIS(X," + dirX + ")"));
            System.out.println("VIS(X," + dirX + ")");
            visX = true;

        }

        if(aroundR(world,'B',1)){
            facts.add(new Clause("ADJACENT(BG," + dirB + ")"));
            System.out.println("ADJACENT(BG," + dirB + ")");

        } else if(aroundR(world,'B',5)){
            facts.add(new Clause("PROX(BG," + dirB + ")"));
            System.out.println("PROX(BG," + dirB + ")");
            proxB = true;

        }

//        if(visX && proxB && Objects.equals(dirB, dirX)){
//            facts.add(new Clause("$EQUAL(" + 0 + "," + 0 + ")"));
//            System.out.println("$EQUAL(0,0)");
//
//        } else if(visX && proxB && !Objects.equals(dirB, dirX)){
//            facts.add(new Clause("$EQUAL(" + 0 + "," + 1 + ")"));
//            System.out.println("$EQUAL(0,1)");
//        }

        if(visX && proxB){
            facts.add(new Clause("$GREATER(" + distanceOfObj('X') + "," + distanceOfObj('B') + ")"));
            System.out.println("$GREATER(" + distanceOfObj('X') + "," + distanceOfObj('B') + ")");
        }

        if(battery <= 1){
            facts.add(new Clause("LOWBATTERY"));
            System.out.println("LOWBATTERY");

        }else if(battery == 4 && iter > 0){
            facts.add(new Clause("BATTERYFULL"));
            System.out.println("BATTERYFULL");
        }

        return facts;
    }

    public static void decide(ArrayList<Clause> markedFacts){
//        printWorld(world);
        Clause commandC = null;
//        if(markedFacts.size() != 0) commandC = markedFacts.get(0);
        if(markedFacts.size() != 0) commandC = markedFacts.get(markedFacts.size()-1);

        String commandS = commandC.getPredicate();
        ArrayList<String> termsList = commandC.getTerms();

        System.out.println("Command: " + commandS);

        if(Objects.equals(commandS, "#MOVEFORWARD")){
            moveR(world,"move",'n');
//            String obj = termsList.get(0);
//            String dirObj = termsList.get(1);
//            if(dirObj)
            System.out.println("Decision: Move forward");
        } else if(Objects.equals(commandS, "#MOVE")){
            String obj = termsList.get(0);
            String dirObj = termsList.get(1);
            System.out.println("obj: " + obj + ", dirobj: " + dirObj );

            if(Objects.equals(obj, "X")){
                if(!Objects.equals(dirObj, "N")){
                    moveR(world, "turn", 'X');
                    System.out.println("Decision: Turn towards X");

                } else {
                    moveR(world, "move", 'n');
                    System.out.println("Decision: Move forward");
                }

            } else {
                if(!Objects.equals(dirObj, "N")){
                    moveR(world, "move", 'n');
                    System.out.println("Decision: Move forward");

                } else {
                    turn90DegRight(world);
                    System.out.println("Decision: Turn 90 degrees right");
                }
            }

//            printWorld(world);

        } else if(Objects.equals(commandS, "#CHARGE_2_UNITS")){
            charge("2 units");
            System.out.println("Decision: Charge 2 units");

        } else if(Objects.equals(commandS, "#CHARGEFULL")){
            charge("full");
            System.out.println("Decision: Charge full");

        } else if(Objects.equals(commandS, "#TURN_R")){
            turn90DegRight(world);
            System.out.println("Decision: Turn 90 degrees right");

        } else if(Objects.equals(commandS, "#RETRACTPANELS")){
            System.out.println("Decision: Retract Panels");
//            battery += 1;
        }

    }

}
