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
    int state = 0;
    int[] DIRECTIONS = {0,1,2,3}; // 0 = Down, 1 = Up, 2 = Left, 3 = Right
    static int dir = 0;

    public static void Populate(char[][] arr){
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[i].length; j++){
                int num = (int)(Math.random() * 4);
                if (num == 1 && arr[i][j] != 'R'){
                    arr[i][j] = 'X';
                } else {
//                    arr[i][j] = '0';
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
            world[y1][x1] = '\u0000';
        }
    }

    public static int IR(){
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

    public static int Touch(){
        int[] coords = findR();
        int row = coords[0];
        int col = coords[1];

        if(row == 19 || row == 0 || col == 19 || col == 0){
            return 1;
        }

        for(int i = -1; i < 2; i++){ // checks perimeter of area around R
            for(int j = -1; j < 2; j++){
                if (world[row+i][col+j] == 'X'){
                    return 1;
                }
            }
        }

        return 0;

    }

    public static int state(){
        return 0;
    }

    public static void trainRobot(){
        DataSet trainingSet = new DataSet(3,6);

        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,1,3,1);
        neuralNetwork.randomizeWeights();

        for(int i = 0; i < 500; i++){
            neuralNetwork.learn(trainingSet);
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

    public static void main(String[] args) {
        Populate(world);
        world[3][5] = 'R';
//        printWorld(world);
//        System.out.println("---------------------------");
//        moveR(5,0,15,2);
//        Touch();
        printWorld(world);
        for(int i = 0; i < 4; i++){
            dir = i;
            System.out.print(IR() + ", ");
        }

    }

}
