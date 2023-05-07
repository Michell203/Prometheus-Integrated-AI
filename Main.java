import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.print("Hello World!");

        DataSet trainingSet = new DataSet(2,1);

        trainingSet.add(new DataSetRow(new double[]{0,0},
                new double[]{0}));
        trainingSet.add(new DataSetRow(new double[]{0,1},
                new double[]{1}));
        trainingSet.add(new DataSetRow(new double[]{1,0},
                new double[]{1}));
        trainingSet.add(new DataSetRow(new double[]{1,1},
                new double[]{0}));

        MultiLayerPerceptron miPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH,2,3,1);
        miPerceptron.learn(trainingSet);

        testNeuralNetwork(miPerceptron, trainingSet);

        miPerceptron.save("miPerceptron.nnet");


    }

    public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

        for(DataSetRow dataRow : testSet.getRows()) {
            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[ ] networkOutput = nnet.getOutput();
            System.out.print("Input: " + Arrays.toString(dataRow.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput) );
        }

    }
}