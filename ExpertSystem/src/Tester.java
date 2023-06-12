import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
            facts.addAll(getSensorData());					// array list of facts (appended)
//            facts.add(new Clause("r"));
            Clause[] factsArr = facts.toArray(new Clause[facts.size()]);

            Productions p = new Productions(rulebasefilename, factsArr);
            p.think();

            ArrayList<Clause> output = p.getMarkedFacts();			// runs to quiescence

//            decision = decide_how_to_move_Robot(output);		// See ass3
//            moveR(decision.x,decision.y,decision.x2,decision.y2);	// See ass3
            System.out.println("Marked Facts: " + p.getMarkedFacts());
//            print(decision);
            printBatt();
            printWorld(world);

            iter++;
            if(battery >= 0){
                battery--;
            }

            facts.clear(); // or, if useful, replace with: facts = loadFromFile(factsfilename);
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

    public static void printBatt(){
        System.out.print("Battery: ");
        for(int i = battery; i > 0; i--){
            System.out.print('▉');
        }
        System.out.println();
    }

    public static char direction(char[][] arr, char obj, int rRow, int rCol){
        int[] objCoords = findY(world, obj);

        int objRow = objCoords[0];
        int objCol = objCoords[1];

        if(objRow > rRow){
            return 'N';
        } else if(objRow < rRow){
            return 'S';
        } else if(objCol > rCol){
            return 'E';
        } else {
            return 'W';
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

        if(rRow == 0 || rRow == rows-1 || rCol == 0 || rCol == cols-1){
            facts.add(new Clause("ADJACENT(BORDER)"));
            System.out.println("ADJACENT(BORDER)");
        }

        if(aroundR(world,'X',4)){
            facts.add(new Clause("VIS(X)"));
            System.out.println("VIS(X)");

        }

        if(aroundR(world,'B',1)){
            facts.add(new Clause("ADJACENT(BG)"));
            System.out.println("ADJACENT(BG)");

        } else if(aroundR(world,'B',5)){
            facts.add(new Clause("PROX(BG)"));
            System.out.println("PROX(BG)");

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

    }

}
