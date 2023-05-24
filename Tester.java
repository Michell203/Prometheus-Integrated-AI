import android.provider.Settings;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.core.learning.stop.MaxErrorStop;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class Tester {

    static char[][] world = new char[20][20];
    char space = '\u0000';
    char wall = 'X';
    char robot = 'R';
    static int state = 0;
    int[] DIRECTIONS = {0,1,2,3}; // 0 = Down, 1 = Up, 2 = Left, 3 = Right
    static int dir = 0;

    public static void generate(char[][] arr){ // Generate world with a 25% chance of having an X at any given point
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[i].length; j++){
                int num = (int)(Math.random() * 4);
                if (num == 1 && arr[i][j] != 'R'){
                    arr[i][j] = 'X';
                } else {
                    arr[i][j] = '.';
                }
            }
        }
    }

    public static int[] findR(){
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                if(world[i][j] == 'R'){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{-1,-1};
    }

    public static void moveR(int x1, int y1, int x2, int y2){
        if (world[y2][x2] != 'X'){
            world[y2][x2] = 'R';
            world[y1][x1] = '.';
            state = 1;
        }
    }

    public static int IR_1(){ // Sensor at front
        int count = 0;
        int[] coords = findR();
        int row = coords[0];
        int col = coords[1];

        if(dir == 0 && row < 19){ // Facing down
            for(int i = row+1; i < world.length; i++){
                if(world[i][col] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 1 && row > 0){ // Facing up
            for(int i = row-1; i > -1; i--){
                if(i > -1 && world[i][col] != 'X' && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 2 && col > 0){ // Facing left
            for(int i = col-1; i > -1; i--){
                if(world[row][i] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 3){ // Facing right
            for(int i = col+1; i < world.length; i++){
                if(world[row][i] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }
        }
        return count;
    }
    public static int IR_2(){ // Sensor at left side
        int count = 0;
        int[] coords = findR();
        int row = coords[0];
        int col = coords[1];

        if(dir == 0 && row < 19){ // Facing down
            for(int i = col+1; i < world.length; i++){
                if(world[row][i] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 1 && row > 0){ // Facing up
            for(int i = col-1; i > -1; i--){
                if(world[row][i] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 2 && col > 0){ // Facing left
            for(int i = row+1; i < world.length; i++){
                if(world[i][col] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 3){ // Facing right
            for(int i = row-1; i > -1; i--){
                if(i > -1 && world[i][col] != 'X' && col < 20){
                    count++;
                } else {
                    break;
                }
            }
        }
        return count;
    }
    public static int IR_3(){ // Sensor at right side
        int count = 0;
        int[] coords = findR();
        int row = coords[0];
        int col = coords[1];

        if(dir == 0 && row < 19){ // Facing down
            for(int i = col-1; i > -1; i--){
                if(world[row][i] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 1 && row > 0){ // Facing up
            for(int i = col+1; i < world.length; i++){
                if(world[row][i] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 2 && col > 0){ // Facing left
            for(int i = row-1; i > -1; i--){
                if(i > -1 && world[i][col] != 'X' && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(dir == 3){ // Facing right
            for(int i = row+1; i < world.length; i++){
                if(world[i][col] != 'X' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }
        }
        return count;
    }

    public static int Touch_1(){
        int[] coords = findR();
        int row = coords[0];
        int col = coords[1];

        if(dir == 0){ // Facing down
            if(row == 19 || world[row+1][col] == 'X'){
                return 1;
            }


        } else if(dir == 1){ // Facing up
            if(row == 0 || world[row-1][col] == 'X'){
                return 1;
            }

        } else if(dir == 2){ // Facing left
            if(col == 0 || world[row][col-1] == 'X'){
                return 1;
            }

        } else if(dir == 3){ // Facing right
            if(col == 19 || world[row][col+1] == 'X'){
                return 1;
            }
        }

        return 0;

    }
    public static int Touch_2(){
        if(IR_2() == 0){
            return 1;
        }
        return 0;
    }
    public static int Touch_3(){
        if(IR_3() == 0){
            return 1;
        }
        return 0;
    }

    public static int state(){
        if(state == 1){
            return 0;
        }
        return 1;
    }

    public static void rotateLeft(){
        if(dir == 0){
            dir = 3;
        } else if(dir == 1){
            dir = 2;
        } else if(dir == 2){
            dir = 0;
        } else if(dir == 3){
            dir = 1;
        }
    }
    public static void rotateRight(){
        if(dir == 0){
            dir = 2;
        } else if(dir == 1){
            dir = 3;
        } else if(dir == 2){
            dir = 1;
        } else if(dir == 3){
            dir = 0;
        }
    }

    public static void trainRobot(){
        DataSet trainingSet = new DataSet(7,6);

//        {inputs: IR_1(), IR_2(), IR_3(), Touch_1(), Touch_2(), Touch_3(), state ->
//        forward_normal, turn-right, turn-left, stop, backup, touching}
        trainingSet.add(new DataSetRow(new double[]{1,0,1,0,1,0,0},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{2,1,1,0,0,0,0},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{3,2,1,0,0,0,0},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{4,1,0,0,0,1,0},
                new double[]{1,0,0,0,0,1}));
        trainingSet.add(new DataSetRow(new double[]{5,2,1,0,0,0,1},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{6,3,0,0,0,1,0},
                new double[]{1,0,0,0,0,1}));
        trainingSet.add(new DataSetRow(new double[]{8,5,2,0,0,0,1},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{12,3,5,0,0,0,0},
                new double[]{1,0,0,0,0,1}));
        trainingSet.add(new DataSetRow(new double[]{13,0,0,0,1,1,1},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{12,3,10,1,0,0,1},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{14,5,0,0,0,1,1},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{14,4,2,0,0,0,0},
                new double[]{1,0,0,0,0,1}));
        trainingSet.add(new DataSetRow(new double[]{15,0,0,0,1,1,0},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{16,3,10,0,0,0,1},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{17,10,0,0,0,1,0},
                new double[]{1,0,0,0,0,1}));
        trainingSet.add(new DataSetRow(new double[]{19,0,4,0,1,0,1},
                new double[]{1,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{20,8,2,0,0,0,0},
                new double[]{1,0,0,0,0,1}));
        trainingSet.add(new DataSetRow(new double[]{0,1,4,1,0,0,0},
                new double[]{0,1,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{0,3,10,1,0,0,1},
                new double[]{0,1,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{0,7,2,1,0,0,1},
                new double[]{0,0,1,0,0,0}));

        // If right is free, turn right
        trainingSet.add(new DataSetRow(new double[]{3,0,6,0,1,0,0},
                new double[]{0,1,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{0,2,3,1,0,0,0},
                new double[]{0,1,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{4,0,9,0,1,0,0},
                new double[]{0,1,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{7,8,15,0,0,0,0},
                new double[]{0,1,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{4,1,17,1,1,0,0},
                new double[]{0,1,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{2,4,10,0,0,0,0},
                new double[]{0,1,0,0,0,0}));

        // If left is free, turn left
        trainingSet.add(new DataSetRow(new double[]{1,4,2,1,0,1,0},
                new double[]{0,0,1,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{3,8,4,1,0,1,0},
                new double[]{0,0,1,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{6,15,8,1,0,1,1},
                new double[]{0,0,1,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{0,2,0,1,0,1,0},
                new double[]{0,0,1,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{2,9,6,1,0,1,0},
                new double[]{0,0,1,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{3,11,5,1,0,1,1},
                new double[]{0,0,1,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{9,18,4,1,0,1,0},
                new double[]{0,0,1,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{4,7,2,1,0,1,0},
                new double[]{0,0,1,0,0,0}));

        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,7,4,6);
        neuralNetwork.randomizeWeights();

        for(int i = 0; i < 500; i++){
            neuralNetwork.learn(trainingSet);
        }

        neuralNetwork.save("RobotAI.nnet");
    }

    public static void decide(int decision){
        int[] coords = findR();
        int row = coords[0];
        int col = coords[1];

        if(decision == 0){ // Move forward
            if(dir == 0){ // Down
                moveR(col,row,col,row+1);
                System.out.println("Moving Down");
            } else if(dir == 1){ // Up
                moveR(col,row,col,row-1);
                System.out.println("Moving Up");
            } else if(dir == 2){ // Left
                moveR(col,row,col-1,row);
                System.out.println("Moving Left");
            } else if(dir == 3){ // Right
                moveR(col,row,col+1,row);
                System.out.println("Moving Right");
            }

        } else if(decision == 1){
            System.out.println("Turning Right");
            rotateRight();

        } else if(decision == 2){
            System.out.println("Turning Left");
            rotateLeft();

        } else if(decision == 3){
            System.out.println("Stopping");

        } else if(decision == 4){
            System.out.println("Backing Up");
            if(dir == 0){ // Down
                moveR(col,row,col,row-1);
                System.out.println("Moving Down");
            } else if(dir == 1){ // Up
                moveR(col,row,col,row+1);
                System.out.println("Moving Up");
            } else if(dir == 2){ // Left
                moveR(col,row,col+1,row);
                System.out.println("Moving Left");
            } else if(dir == 3){ // Right
                moveR(col,row,col-1,row);
                System.out.println("Moving Right");
            }

        } else if(decision == 5){
            System.out.println("Touching");
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

    public static void tester(){
        for(int i=0; i<20; i++) {
            int distanceFront = IR_1();
            int distanceLeft = IR_2();
            int distanceRight = IR_3();

            int touchingFront = Touch_1();
            int touchingLeft = Touch_2();
            int touchingRight = Touch_3();

            int st = state();

            NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile("RobotAI.nnet");
            neuralNetwork.setInput(distanceFront,distanceLeft,distanceRight,touchingFront,touchingLeft,touchingRight,st);
            neuralNetwork.calculate();

            double[] classifications = neuralNetwork.getOutput();

            // Find index with max value
            int maxIndex = 0;
            double max = classifications[0];
            for(int n = 0; n < classifications.length; n++){
                if(max < classifications[n]){
                    maxIndex = n;
                    max = classifications[n];
                }
            }

            decide(maxIndex);
            printWorld(world);
            System.out.println("------------------------------------------");
        }
    }

    public static void main(String[] args) {
        generate(world);
        world[10][10] = 'R';
//        trainRobot();
//        NeuralNetwork brain = NeuralNetwork.createFromFile("RobotAI.nnet");
        tester();
//        printWorld(world);
//        System.out.println("---------------------------");
//        moveR(5,0,15,2);
//        for(int i = 0; i < 4; i++){
//            dir = i;
//            System.out.print("Front: " + IR_1() + ", Left: " + IR_2() + ", Right: " + IR_3() + "\n");
//            System.out.print(Touch_3() + ", ");
//        }

    }

}
