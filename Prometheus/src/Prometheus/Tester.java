package Prometheus;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.Perceptron;
import expertsystem.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Tester {

    public static void main(String[] args) throws Exception {
        char[][] world = new char[10][10];
        World w = new World(world,1,1,0);

        w.generateMap();
//        NN.trainNN();

        NN nn; // The general tree network
        Productions  es;  // The production system
        KNN knn = new KNN("./src/RuleBases/knn.txt"); // The knowledge node network

        ArrayList<Clause> sensors;   // classification strings from the GTN
        ArrayList<String> productions; // list of fired rules from KNN
        ArrayList<Clause> commands; // list of fired # commands

        META meta = new META();

        // Testing Purposes
//        w.printDir();
//        System.out.println("Space in front of R: " + w.IRFront() + ", Space left of R: " + w.IRLeft() + ", Space right of R: " + w.IRRight());
//        System.out.println("Input: " + w.IRFront() + ", " + w.IRLeft() + ", " + w.IRRight() + ", " + w.isDirB() + ", " + w.atB());
//        System.out.println(Arrays.toString(nn.sensors));
//        System.out.println(Arrays.toString(nn.classifications()));
//
//        w.displayMap();

        for(int i=0; i < 30; i++) {
            nn = new NN("RobotIR.nnet", "RobotB.nnet", w);

            w.printDir();
            System.out.println("Space in front of R: " + w.IRFront() + ", Space left of R: " + w.IRLeft() + ", Space right of R: " + w.IRRight());
            System.out.println("Input: " + w.IRFront() + ", " + w.IRLeft() + ", " + w.IRRight() + ", " + w.isDirB() + ", " + w.atB());
            System.out.println(Arrays.toString(nn.sensors));
            System.out.println(nn.classifications());

            w.displayMap();

            sensors     = nn.classifications(); // $classifications
            productions = knn.think(sensors, 10);
            es          = new Productions(productions, sensors.toArray(new Clause[sensors.size()]));

            es.printRuleBase();
            es.think();

            commands = es.getMarkedFacts(); // #commands
//            commands    = es.think(sensors, productions);

            meta.think(sensors,commands);

            dispatch(commands, w); // call's world's setters
//            badguys();	    // call's world's setters to update badbuy(s).
            System.out.println("Commands: " + commands);

            if(w.wonGame()){
                System.out.println("======== YOU WON ========");
                break;
            }

            if(w.lostGame()){
                System.out.println("======== YOU LOST ========");
                break;
            }

            System.out.println("====================");
        }
    }

    public static void dispatch(ArrayList<Clause> commands, World w){
        Clause command = commands.get(0);
        String commandS = command.getPredicate();

        if(commandS.equals("#STEP")){
            System.out.println("Decision: Moving Forward");
            w.step();

        } else if(commandS.equals("#STOP")){
            System.out.println("Decision: Stopping");
            w.stop();

        } else if(commandS.equals("#TURN")){
            System.out.println("Decision: Turning");
            String direction = command.getTerms().get(0);
            w.turn(direction);

        }
    }
}
