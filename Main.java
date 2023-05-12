import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;
import java.util.Arrays;
import java.util.Random;

public class Main {

    public static double randVoltNum(){
        Random random = new Random();
        return random.nextDouble() * 2.5;
    }

    public static void main(String[] args) {
//        System.out.print("Hello World!");

//        DataSet trainingSet = new DataSet(1,31);
//
//        trainingSet.add(new DataSetRow(new double[]{1.5},
//                new double[]{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{2.0},
//                new double[]{0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{2.25},
//                new double[]{0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
////        trainingSet.add(new DataSetRow(new double[]{2.3},
////                new double[]{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
////        trainingSet.add(new DataSetRow(new double[]{2.2},
////                new double[]{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
////        trainingSet.add(new DataSetRow(new double[]{2.0},
////                new double[]{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{1.8},
//                new double[]{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{1.5},
//                new double[]{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{1.2},
//                new double[]{0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{1.05},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.89},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.8},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.7},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.62},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.6},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.55},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.47},
//                new double[]{0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.45},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0}));
//        trainingSet.add(new DataSetRow(new double[]{0.37},
//                new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}));

        DataSet trainingSet = new DataSet(1,1);

        trainingSet.add(new DataSetRow(new double[]{1.5},
                new double[]{0}));
        trainingSet.add(new DataSetRow(new double[]{2.0},
                new double[]{1.0/30.0}));
//        trainingSet.add(new DataSetRow(new double[]{2.25},
//                new double[]{0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
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

        MultiLayerPerceptron mlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,1,7,1);
        mlPerceptron.learn(trainingSet);

        testNeuralNetwork(mlPerceptron, trainingSet);

        mlPerceptron.save("DistancePredictor.nnet");


    }

    public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

        for(int i = 0; i < 10; i++) {
            double num = randVoltNum();
            nnet.setInput(num);
            nnet.calculate();
            double[ ] networkOutput = nnet.getOutput();
            System.out.print("Input: " + Double.toString(num));
            System.out.println(" Output: " + Double.toString(networkOutput[0] * 150));
        }

    }
}