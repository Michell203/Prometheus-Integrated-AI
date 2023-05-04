import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

public class Main {
    public static void main(String[] args) {
        System.out.print("Hello World!");

        //Perceptron network
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);

        //Training set
        DataSet trainingSet = new DataSet(2,1);

        //Training data to training set (Logical XOR)
        double[] input1 = new double[]{0,0};
        double[] output1 = new double[]{0};
        trainingSet.add(new DataSetRow(input1, output1));

        double[] input2 = new double[]{0,1};
        double[] output2 = new double[]{1};
        trainingSet.add(new DataSetRow(input2, output2));

        double[] input3 = new double[]{1,0};
        double[] output3 = new double[]{1};
        trainingSet.add(new DataSetRow(input3, output3));

        double[] input4 = new double[]{1,1};
        double[] output4 = new double[]{0};
        trainingSet.add(new DataSetRow(input4, output4));

        //Learn training set
        neuralNetwork.learn(trainingSet);

        //Save trained network to file
        neuralNetwork.save("xor_perceptron.nnet");

        //Load saved network
//        NeuralNetwork neuralNetworkLoad = NeuralNetwork.createFromFile("xor_perceptron.nnet");

//        //Give inputs
//        neuralNetworkLoad.setInput(1,0);

//        //Calculate output
//        neuralNetworkLoad.calculate();

//        //Get output
//        double[] output = neuralNetworkLoad.getOutput();
//        System.out.print(output);

    }
}