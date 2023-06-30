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
    static boolean won = false;
    static boolean wait = false;

    public static void main(String[] args) throws Exception {
        String setupfilename    = "resources/setup.txt";
        String rulebasefilename = getFileName(setupfilename)[1];
        String factsfilename    = getFileName(setupfilename)[0];

        ArrayList<Clause> facts = new ArrayList<>();
        ArrayList<Rule> newRules = new ArrayList<>();
        generate(world);

        for(int i=0; i<30; i++) {
            System.out.println("Input: ");

            facts.addAll(getSensorData());					// array list of facts (appended)
//            facts.addAll(newFacts);
            Clause[] factsArr = facts.toArray(new Clause[facts.size()]);

            Productions p = new Productions(rulebasefilename, factsArr);
            p.addToRuleBase(newRules);
            p.think();
            ArrayList<Clause> output = p.getMarkedFacts();			// runs to quiescence

            printDir();
            printBatt();

            if(wait){
                System.out.println("Charging...");

                if(moveB() == 1){
                    System.out.println("===== GAME OVER =====");
                    break;
                }

                printWorld(world);
                wait = false;
                System.out.println("-------------------------");
                continue;
            }

//            decide(output);
            System.out.println("Marked Facts: " + p.getMarkedFacts());

            // Check to see if we won
            if(won){
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
            newRules.addAll(p.optimizer());
//            ArrayList<Clause> inp = new ArrayList<>();
//            ArrayList<Clause> cmds = new ArrayList<>();
//            inp.add(new Clause("O"));
//            inp.add(new Clause("P"));
//            inp.add(new Clause("D"));
//            cmds.add(new Clause("C"));
//            cmds.add(new Clause("T"));
//            p.objectify(inp,cmds,1);

            p.printRuleBase();

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

        if(mode.equals("move")){
            if(dir == 0 && rRow < 9){ // Facing Down
                if(world[rRow + 1][rCol] == 'X'){
                    won = true;
                }

                world[rRow][rCol] = world[rRow + 1][rCol];
                world[rRow + 1][rCol] = 'R';

            } else if(dir == 1 && rRow > 0){ // Facing Up
                if(world[rRow - 1][rCol] == 'X'){
                    won = true;
                }

                world[rRow][rCol] = world[rRow - 1][rCol];
                world[rRow - 1][rCol] = 'R';

            } else if(dir == 2 && rCol > 0){ // Facing Left
                if(world[rRow][rCol - 1] == 'X'){
                    won = true;
                }

                world[rRow][rCol] = world[rRow][rCol - 1];
                world[rRow][rCol - 1] = 'R';

            } else if(dir == 3 && rCol < 9){ // Facing Right
                if(world[rRow][rCol + 1] == 'X'){
                    won = true;
                }

                world[rRow][rCol] = world[rRow][rCol + 1];
                world[rRow][rCol + 1] = 'R';

            }

        } else if (mode.equals("turn")){
//            turn90DegRight(world, dir);
            while(direction(world,obj,rRow,rCol) != 'N'){
                turn90DegRight(world);
            }

        } else if (mode.equals("backup")){
            char dirB = direction(world,'B',rRow,rCol);

            if(dirB == 'E' || dirB == 'W' || dirB == 'S'){
                moveR(world,"move",'n');

            } else {
                if(dir == 0 && rRow < 9){ // Facing Down
                    if(world[rRow - 1][rCol] == 'X'){
                        won = true;
                    }

                    world[rRow][rCol] = world[rRow - 1][rCol];
                    world[rRow - 1][rCol] = 'R';

                } else if(dir == 1 && rRow > 0){ // Facing Up
                    if(world[rRow + 1][rCol] == 'X'){
                        won = true;
                    }

                    world[rRow][rCol] = world[rRow + 1][rCol];
                    world[rRow + 1][rCol] = 'R';

                } else if(dir == 2 && rCol > 0){ // Facing Left
                    if(world[rRow][rCol + 1] == 'X'){
                        won = true;
                    }

                    world[rRow][rCol] = world[rRow][rCol + 1];
                    world[rRow][rCol + 1] = 'R';

                } else if(dir == 3 && rCol < 9){ // Facing Right
                    if(world[rRow][rCol - 1] == 'X'){
                        won = true;
                    }

                    world[rRow][rCol] = world[rRow][rCol - 1];
                    world[rRow][rCol - 1] = 'R';

                }
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

    public static void printDir(){
        if(dir == 0){
            System.out.println("Direction of R: Down");

        } else if(dir == 1){
            System.out.println("Direction of R: Up");

        } else if(dir == 2){
            System.out.println("Direction of R: Left");

        } else {
            System.out.println("Direction of R: Right");
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
        if(dir == 0 && rRow == 9){ // Down
            return true;

        } else if(dir == 1 && rRow == 0){ // Up
            return true;

        } else if(dir == 2 && rCol == 0){ // Left
            return true;

        } else if(dir == 3 && rCol == 9) { // Right
            return true;

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
        if(type.equals("full")){
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
            if(objCol > rCol){
                return 'S';
            } else if (objCol < rCol) {
                return 'N';
            } else if(objRow < rRow){
                return 'E';
            } else {
                return 'W';
            }

        } else{ // Right
            if(objCol < rCol){
                return 'S';
            } else if(objCol > rCol){
                return 'N';
            } else if(objRow > rRow){
                return 'E';
            } else {
                return 'W';
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

        if(battery <= 1){
            facts.add(new Clause("LOWBATTERY"));
            System.out.println("LOWBATTERY");

            if(aroundR(world,'B',5)){
                facts.add(new Clause("PROX(BG," + dirB + ")"));
                System.out.println("PROX(BG," + dirB + ")");
                proxB = true;
            }

            return facts;

        }

        if(touchedFrontBorder(world,rRow,rCol)){
            facts.add(new Clause("TOUCHEDFRONT(BORDER)"));
            System.out.println("TOUCHEDFRONT(BORDER)");

            return facts;
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

//        if(battery <= 1){
//            facts.add(new Clause("LOWBATTERY"));
//            System.out.println("LOWBATTERY");
//
//        }else

        if(battery == 4 && iter > 0){
            facts.add(new Clause("BATTERYFULL"));
            System.out.println("BATTERYFULL");
        }

        return facts;
    }

    public static void decide(ArrayList<Clause> markedFacts){
        int[] rCoords = findY(world,'R');

        int rRow = rCoords[0];
        int rCol = rCoords[1];
//        printWorld(world);
        Clause commandC = null;
//        if(markedFacts.size() != 0) commandC = markedFacts.get(0);
        if(markedFacts.size() != 0 && (markedFacts.get(0).getPredicate().equals("#CHARGEFULL")
        || markedFacts.get(0).getPredicate().equals("#CHARGE_2_UNITS")) ){

            commandC = markedFacts.get(0);

        } else if (markedFacts.size() != 0){
            commandC = markedFacts.get(markedFacts.size()-1);
        }

        String commandS = commandC.getPredicate();
        ArrayList<String> termsList = commandC.getTerms();

        System.out.println("Command: " + commandS);

        if(commandS.equals("#MOVEFORWARD") && battery > 0){
            moveR(world,"move",'n');
            System.out.println("Decision: Move forward");

        } else if(commandS.equals("#MOVE") && battery > 0){
            String obj = termsList.get(0);
            String dirObj = termsList.get(1);
//            System.out.println("obj: " + obj + ", dirobj: " + dirObj );

            if(obj.equals("X")){
                if(!dirObj.equals("N")){
                    moveR(world, "turn", 'X');
                    System.out.println("Decision: Turn towards X");

                } else {
                    moveR(world, "move", 'n');
                    System.out.println("Decision: Move forward");
                }

            } else {
                if(!dirObj.equals("N")){
                    moveR(world, "move", 'n');
                    System.out.println("Decision: Move forward");

                } else {
                    turn90DegRight(world);
                    System.out.println("Decision: Turn 90 degrees right");
                }
            }

//            printWorld(world);

        } else if(commandS.equals("#CHARGE_2_UNITS")){
            charge("2 units");
            System.out.println("Decision: Charge 2 units");

        } else if(commandS.equals("#CHARGEFULL")){
            charge("full");
            System.out.println("Decision: Charge full");
            wait = true;

        } else if(commandS.equals("#TURN_R") && battery > 0){
            turn90DegRight(world);
            System.out.println("Decision: Turn 90 degrees right");

        } else if(commandS.equals("#RETRACTPANELS")){
            System.out.println("Decision: Retract Panels");
//            battery += 1;
        } else if(commandS.equals("#BACKUP") && battery > 0){
            System.out.println("Decision: Back up");
            String dirB = termsList.get(0);
            moveR(world,"backup",'n');
        }

    }

}
