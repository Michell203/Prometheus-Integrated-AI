import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class Main {

    public static double randVoltNum(){
        Random random = new Random();
        return random.nextDouble() * 2.5;
    }

    public static void main(String[] args) {
        System.out.print("Hello World!");

        DataSet trainingSet = new DataSet(1,1);

//        trainingSet.add(new DataSetRow(new double[]{1.45},
//                new double[]{1.0/30.0}));
//        trainingSet.add(new DataSetRow(new double[]{1.97},
//                new double[]{1.0/30.0}));

        trainingSet.add(new DataSetRow(new double[]{1.5},
                new double[]{1.0/30.0}));

        trainingSet.add(new DataSetRow(new double[]{2.0},
                new double[]{2.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{2.3},
                new double[]{3.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{2.2},
                new double[]{4.0/30.0}));
//        trainingSet.add(new DataSetRow(new double[]{2.0},
//                new double[]{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
        trainingSet.add(new DataSetRow(new double[]{1.8},
                new double[]{5.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{1.5},
                new double[]{6.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{1.2},
                new double[]{8.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{1.05},
                new double[]{12.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.89},
                new double[]{14.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.8},
                new double[]{16.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.7},
                new double[]{18.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.62},
                new double[]{20.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.6},
                new double[]{22.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.55},
                new double[]{24.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.47},
                new double[]{26.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.45},
                new double[]{28.0/30.0}));
        trainingSet.add(new DataSetRow(new double[]{0.37},
                new double[]{1.0}));

        MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,1,4,1);
        neuralNetwork.learn(trainingSet);
//        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile("DistancePredictor.nnet");

        testNeuralNetwork(neuralNetwork, trainingSet);

        neuralNetwork.save("DistancePredictor.nnet");

    }

    public static int roundToNearest(double x){
        double num = x / 5.0;
        return (int) Math.round(num) * 5;
    }

    public static int validityChecker(double x, double y){
        if(     (x >= 1.5 && x < 1.75  && y >= 2.5 && y < 7.5) ||
                (x >= 1.75 && x < 2.12  && y >= 7.5 && y < 12.5) ||
                ( ((x >= 2.12 && x < 2.3) || (x <= 2.3 && x > 2.2))  && y >= 12.5 && y < 17.5) ||
                (x >= 2.12 && x < 2.2  && y >= 17.5 && y < 22.5) ||
                (x >= 1.84 && x < 2.12  && y >= 22.5 && y < 27.5) ||
                (x >= 1.7 && x < 1.84  && y >= 27.5 && y < 32.5) ||
                (x >= 1.58 && x < 1.7  && y >= 32.5 && y < 37.5) ||
                (x >= 1.37 && x < 1.58  && y >= 37.5 && y < 42.5) ||
                (x >= 1.25 && x < 1.37  && y >= 42.5 && y < 47.5) ||
                (x >= 1.17 && x < 1.25  && y >= 47.5 && y < 52.5) ||
                (x >= 1.1 && x < 1.17  && y >= 52.5 && y < 57.5) ||
                (x >= 1.0 && x < 1.1  && y >= 57.5 && y < 62.5) ||
                (x >= 0.9 && x < 1.0  && y >= 62.5 && y < 67.5) ||
                (x >= 0.86 && x < 0.9  && y >= 67.5 && y < 72.5) ||
                (x >= 0.8 && x < 0.86  && y >= 72.5 && y < 77.5) ||
                (x >= 0.75 && x < 0.8  && y >= 77.5 && y < 82.5) ||
                (x >= 0.69 && x < 0.75  && y >= 82.5 && y < 87.5) ||
                (x >= 0.66 && x < 0.69  && y >= 87.5 && y < 92.5) ||
                (x >= 0.63 && x < 0.66  && y >= 92.5 && y < 97.5) ||
                (x >= 0.6 && x < 0.63  && y >= 97.5 && y < 102.5) ||
                (x >= 0.57 && x < 0.6  && y >= 102.5 && y < 107.5) ||
                (x >= 0.54 && x < 0.57  && y >= 107.5 && y < 112.5) ||
                (x >= 0.51 && x < 0.54  && y >= 112.5 && y < 117.5) ||
                (x >= 0.48 && x < 0.51  && y >= 117.5 && y < 122.5) ||
                (x >= 0.46 && x < 0.48  && y >= 122.5 && y < 127.5) ||
                (x >= 0.44 && x < 0.46  && y >= 127.5 && y < 132.5) ||
                (x >= 0.42 && x < 0.44  && y >= 132.5 && y < 137.5) ||
                (x >= 0.4 && x < 0.42  && y >= 137.5 && y < 142.5) ||
                (x >= 0.38 && x < 0.4  && y >= 142.5 && y < 147.5) ||
                (x >= 0.36 && x < 0.38 && y >= 147.5 && y < 152.5) ){

            return 1;

        }
        return 0;
    }

    public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
        int count = 0;

        for(int i = 0; i < 10; i++) {
            double num = randVoltNum();
            nnet.setInput(num);
            nnet.calculate();
            double[ ] networkOutput = nnet.getOutput();
            count += validityChecker(num,networkOutput[0]* 150);
            System.out.print("Input: " + Double.toString(num));
            System.out.println(" Output: " + Integer.toString(roundToNearest(networkOutput[0] * 150)));
        }

        System.out.print(count);

    }
}